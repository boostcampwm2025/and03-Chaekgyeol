package com.boostcamp.and03.ui.screen.booklist.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import com.boostcamp.and03.ui.screen.booklist.model.BookUiModel
import com.boostcamp.and03.ui.theme.And03Padding
import com.boostcamp.and03.ui.theme.And03Spacing

@Composable
fun BookGrid(
    books: List<BookUiModel>,
    onBookClick: (BookUiModel) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        verticalArrangement = Arrangement.spacedBy(And03Spacing.SPACE_L),
        horizontalArrangement = Arrangement.spacedBy(And03Spacing.SPACE_M),
        contentPadding = PaddingValues(
            bottom = And03Padding.PADDING_L
        )
    ) {
        items(books) { book ->
            BookItem(
                title = book.title,
                authors = book.authors,
                thumbnail = book.thumbnail,
                onClick = { onBookClick(book) }
            )
        }
    }
}
