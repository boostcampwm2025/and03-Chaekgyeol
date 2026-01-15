package com.boostcamp.and03.ui.screen.bookdetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.boostcamp.and03.R
import com.boostcamp.and03.ui.component.And03AppBar
import com.boostcamp.and03.ui.screen.bookdetail.component.CharacterCard
import com.boostcamp.and03.ui.screen.bookdetail.component.DropdownMenuContainer
import com.boostcamp.and03.ui.screen.bookdetail.component.MemoCard
import com.boostcamp.and03.ui.screen.bookdetail.component.QuoteCard
import com.boostcamp.and03.ui.screen.bookdetail.component.SquareAddButton
import com.boostcamp.and03.ui.screen.bookdetail.model.BookDetailTab
import com.boostcamp.and03.ui.screen.bookdetail.model.CharacterUiModel
import com.boostcamp.and03.ui.screen.bookdetail.model.MemoType
import com.boostcamp.and03.ui.screen.bookdetail.model.MemoUiModel
import com.boostcamp.and03.ui.theme.And03Padding
import com.boostcamp.and03.ui.theme.And03Spacing
import com.boostcamp.and03.ui.theme.And03Theme
import kotlinx.collections.immutable.persistentListOf

@Composable
fun BookDetailRoute(
    bookId: String,
    navigateToBack: () -> Unit,
    navigateToCanvas: (memoId: String) -> Unit,
    viewModel: BookDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    BookDetailScreen(
        uiState = uiState,
        navigateToBack = navigateToBack,
        navigateToCanvas = navigateToCanvas,
        onRetryClick = { viewModel.loadAllData() }
    )
}

@Composable
private fun BookDetailScreen(
    uiState: BookDetailUiState,
    navigateToBack: () -> Unit,
    navigateToCanvas: (memoId: String) -> Unit,
    onRetryClick: () -> Unit
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = BookDetailTab.entries

    Scaffold(
        topBar = {
            And03AppBar(
                title = stringResource(R.string.book_detail_app_bar_title),
                onBackClick = navigateToBack,
            ) {
                IconButton(onClick = {}) {
                    Icon(
                        painter = painterResource(R.drawable.ic_more_vert_filled),
                        contentDescription = stringResource(
                            R.string.content_description_more_button
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (uiState.errorMessage != null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(And03Spacing.SPACE_M),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = uiState.errorMessage)
                        Button(onClick = onRetryClick) {
                            Text(stringResource(R.string.retry_btn_txt))
                        }
                    }
                }
            } else {
                BookInfoSection(
                    thumbnail = uiState.thumbnail,
                    title = uiState.title,
                    author = uiState.author,
                    publisher = uiState.publisher
                )

                SecondaryTabRow(
                    selectedTabIndex = selectedTabIndex,
                    containerColor = And03Theme.colors.surface,
                    contentColor = And03Theme.colors.onSurface,
                    indicator = {
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(selectedTabIndex),
                            color = And03Theme.colors.primary
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    tabs.forEachIndexed { index, tab ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = { selectedTabIndex = index },
                            text = { Text(text = tab.title) }
                        )
                    }
                }

                when (tabs[selectedTabIndex]) {
                    BookDetailTab.CHARACTER -> CharacterTab(uiState)
                    BookDetailTab.QUOTE -> QuoteTab(uiState)
                    BookDetailTab.MEMO -> MemoTab(
                        uiState = uiState,
                        onClickAddCanvas = { },
                        onClickAddText = { },
                        onClickMemo = { memo ->
                            if (memo.memoType == MemoType.CANVAS) {
                                navigateToCanvas(memo.id)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun BookInfoSection(
    thumbnail: String,
    title: String,
    author: String,
    publisher: String
) {
    Row(
        modifier = Modifier.padding(And03Padding.PADDING_XL),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = thumbnail,
            contentDescription = null,
            modifier = Modifier
                .width(80.dp)
                .aspectRatio(2f / 3f),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(And03Spacing.SPACE_M))
        Column(
            modifier = Modifier.padding(vertical = And03Padding.PADDING_2XL),
        ) {
            Text(
                text = title,
                style = And03Theme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(And03Spacing.SPACE_S))
            Text(
                text = author,
                style = And03Theme.typography.bodyMedium,
                color = And03Theme.colors.secondary
            )
            Spacer(modifier = Modifier.height(And03Spacing.SPACE_XS))
            Text(
                text = publisher,
                style = And03Theme.typography.bodySmall,
                color = And03Theme.colors.secondary
            )
        }
    }
}

@Composable
private fun CharacterTab(uiState: BookDetailUiState) {
    Column(
        modifier = Modifier.padding(And03Padding.PADDING_L),
        verticalArrangement = Arrangement.spacedBy(And03Spacing.SPACE_M),
        horizontalAlignment = Alignment.End
    ) {
        SquareAddButton(onClick = { })
        uiState.characters.forEach { character ->
            CharacterCard(
                name = character.name,
                role = character.role,
                iconColor = character.iconColor,
                description = character.description,
                onClick = { },
                onEditClick = { },
                onDeleteClick = { }
            )
        }
    }
}

@Composable
private fun QuoteTab(uiState: BookDetailUiState) {
    Column(
        modifier = Modifier.padding(And03Padding.PADDING_L),
        verticalArrangement = Arrangement.spacedBy(And03Spacing.SPACE_M),
        horizontalAlignment = Alignment.End
    ) {
        SquareAddButton(onClick = { })
        uiState.quotes.forEach { quote ->
            QuoteCard(
                quote = quote,
                onClick = {}
            )
        }
    }
}

@Composable
private fun MemoTab(
    uiState: BookDetailUiState,
    onClickAddCanvas: () -> Unit,
    onClickAddText: () -> Unit,
    onClickMemo: (MemoUiModel) -> Unit,
) {
    Column(
        modifier = Modifier.padding(And03Padding.PADDING_L),
        verticalArrangement = Arrangement.spacedBy(And03Spacing.SPACE_M),
        horizontalAlignment = Alignment.End
    ) {
        DropdownMenuContainer(
            trigger = { onClick ->
                SquareAddButton(onClick = onClick)
            },
            menuContent = { closeMenu ->
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.book_detail_add_canvas_memo)) },
                    onClick = {
                        closeMenu()
                        onClickAddCanvas()
                    }
                )

                DropdownMenuItem(
                    text = { Text(stringResource(R.string.book_detail_add_text_memo)) },
                    onClick = {
                        closeMenu()
                        onClickAddText()
                    }
                )
            }
        )

        uiState.memos.forEach { memo ->
            MemoCard(
                type = memo.memoType,
                title = memo.title,
                contentPreview = memo.content ?: "",
                pageLabel = stringResource(
                    id = R.string.book_detail_memo_page_range,
                    memo.startPage,
                    memo.endPage
                ),
                date = memo.date,
                onClick = { onClickMemo(memo) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BooklistScreenPreview() {
    val previewState = BookDetailUiState(
        thumbnail = "https://i.pinimg.com/736x/8f/20/41/8f2041520696507bc2bfd2f5648c8da3.jpg",
        title = "Harry Potter and the Philosopher's Stone",
        author = "J.K. Rowling",
        publisher = "Bloomsbury Publishing",
        characters = persistentListOf(
            CharacterUiModel(
                name = "해리 포터",
                role = "주인공",
                iconColor = Color(0xFF1E88E5),
                description = "호그와트의 마법사 학생으로 볼드모트와 맞서 싸우는 주인공"
            ),
            CharacterUiModel(
                name = "헤르미온느 그레인저",
                role = "조연",
                iconColor = Color(0xFF8E24AA),
                description = "뛰어난 마법 실력을 가진 해리의 절친한 친구"
            )
        )
    )

    And03Theme {
        BookDetailScreen(
            uiState = previewState,
            navigateToBack = {},
            navigateToCanvas = {},
            onRetryClick = {}
        )
    }
}