package org.librarease.app.presentation.books

import org.librarease.app.domain.model.BookItem

data class BooksUiState(
    val books: List<BookItem> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val error: String? = null,
    val hasMoreBooks: Boolean = true,
    val searchQuery: String = "",
    val currentPage: Int = 1
) {
    val isEmpty: Boolean
        get() = books.isEmpty() && !isLoading && error == null
    
    val isInitialLoad: Boolean
        get() = books.isEmpty() && isLoading
}
