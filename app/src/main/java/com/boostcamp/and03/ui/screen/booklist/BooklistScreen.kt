package com.boostcamp.and03.ui.screen.booklist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.boostcamp.and03.R
import com.boostcamp.and03.ui.component.And03AppBar
import com.boostcamp.and03.ui.component.SearchTextField
import com.boostcamp.and03.ui.screen.booklist.component.BookCountText
import com.boostcamp.and03.ui.screen.booklist.component.BookGrid
import com.boostcamp.and03.ui.screen.booklist.model.BookUiModel
import com.boostcamp.and03.ui.theme.And03Padding
import com.boostcamp.and03.ui.theme.And03Spacing
import com.boostcamp.and03.ui.theme.And03Theme
import kotlinx.collections.immutable.persistentListOf

@Composable
fun BooklistRoute(
    viewModel: BooklistViewModel = hiltViewModel(),
    onBookClick: (BookUiModel) -> Unit,
    onAddBookClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.resetSearch()
        viewModel.loadBooks()
    }

    BooklistScreen(
        uiState = uiState,
        onSearch = viewModel::onSearchQueryChange,
        onBookClick = onBookClick,
        onAddBookClick = onAddBookClick
    )
}

@Composable
private fun BooklistScreen(
    uiState: BooklistUiState,
    onSearch: (String) -> Unit,
    onBookClick: (BookUiModel) -> Unit,
    onAddBookClick: () -> Unit,
) {
    val searchState = remember { TextFieldState(uiState.searchQuery) }

    LaunchedEffect(Unit) {
        snapshotFlow { searchState.text.toString() }
            .collect { query ->
                onSearch(query)
            }
    }

    Scaffold(
        topBar = {
            And03AppBar(
                title = stringResource(R.string.book_list_title),
                actions = {
                    IconButton(onClick = onAddBookClick) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_add_filled),
                            contentDescription = stringResource(R.string.content_description_add_book_button)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = And03Padding.PADDING_L)
        ) {
            SearchTextField(
                state = searchState,
                onSearch = { onSearch(searchState.text.toString()) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(And03Spacing.SPACE_L))

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = DividerDefaults.Thickness,
                color = DividerDefaults.color
            )

            Spacer(modifier = Modifier.height(And03Spacing.SPACE_M))

            BookCountText(count = uiState.filteredBooks.size)

            Spacer(modifier = Modifier.height(And03Spacing.SPACE_M))

            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                uiState.allBooks.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.book_list_empty_book),
                            style = And03Theme.typography.bodyLarge,
                            color = And03Theme.colors.onSurfaceVariant
                        )
                    }
                }

                else -> {
                    BookGrid(
                        books = uiState.filteredBooks,
                        onBookClick = onBookClick
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BooklistScreenPreview() {
    val previewState = BooklistUiState(
        allBooks = persistentListOf(
            BookUiModel(
                id = "",
                title = "객체지향의 사실과 오해",
                authors = persistentListOf("조영호"),
                publisher = "위키북스",
                thumbnail = "",
                totalPage = 200,
                isbn = ""
            ),
            BookUiModel(
                id = "",
                title = "클린 아키텍처",
                authors = persistentListOf("로버트 C. 마틴"),
                publisher = "인사이트",
                thumbnail = "",
                totalPage = 200,
                isbn = ""
            )
        )
    )

    And03Theme {
        BooklistScreen(
            uiState = previewState,
            onSearch = {},
            onBookClick = {},
            onAddBookClick = {}
        )
    }
}
