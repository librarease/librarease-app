package org.librarease.app.presentation.books

import org.librarease.app.domain.model.BookItem

/**
 * Immutable UI state for All Books Screen
 * Following data flow best practices with single source of truth
 */
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
