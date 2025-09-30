package org.librarease.app.presentation.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.librarease.app.core.Resource
import org.librarease.app.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.librarease.app.domain.model.BookItem
import org.librarease.app.domain.model.Library
import org.librarease.app.domain.repository.LibrareaseRepository
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val repo: AuthRepository,
    private val librareaseRepo: LibrareaseRepository
): ViewModel() {
    private val _authState = MutableStateFlow(repo.currentUser != null)
    val authState: StateFlow<Boolean> = _authState.asStateFlow()

    private val _booksList = MutableStateFlow<List<BookItem>>(emptyList())
    val bookList: StateFlow<List<BookItem>> = _booksList.asStateFlow()

    private val _filteredBooksList = MutableStateFlow<List<BookItem>>(emptyList())
    val filteredBooksList: StateFlow<List<BookItem>> = _filteredBooksList.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _libraryList = MutableStateFlow<List<Library>>(emptyList())
    val libraryList: StateFlow<List<Library>> = _libraryList.asStateFlow()

    // Pagination state
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _hasMoreBooks = MutableStateFlow(true)
    val hasMoreBooks: StateFlow<Boolean> = _hasMoreBooks.asStateFlow()

    private val _currentPage = MutableStateFlow(1)
    val currentPage: StateFlow<Int> = _currentPage.asStateFlow()

    private val _allBooksList = MutableStateFlow<List<BookItem>>(emptyList())
    val allBooksList: StateFlow<List<BookItem>> = _allBooksList.asStateFlow()

    private val _filteredAllBooksList = MutableStateFlow<List<BookItem>>(emptyList())
    val filteredAllBooksList: StateFlow<List<BookItem>> = _filteredAllBooksList.asStateFlow()

    private val PAGE_SIZE = 20

    init {
        getAuthState()
        getBookList(20)
        getLibraryList(5)
    }

    private fun getAuthState() = viewModelScope.launch {
        repo.getAuthState().collect { isUserSignIn ->
            _authState.value = isUserSignIn
        }
    }

    fun getBookList(limit: Int) {
        try {
            viewModelScope.launch {
                val books = librareaseRepo.getBooks(limit)
                _booksList.value = books
                _filteredBooksList.value = books
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun loadInitialBooks() {
        if (_allBooksList.value.isEmpty()) {
            loadMoreBooks(true)
        }
    }

    fun loadMoreBooks(isFirstLoad: Boolean = false) {
        if (_isLoading.value || (!_hasMoreBooks.value && !isFirstLoad)) return

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val page = if (isFirstLoad) 1 else _currentPage.value
                val newBooks = librareaseRepo.getBooksPaginated(PAGE_SIZE, page)

                if (newBooks.isEmpty()) {
                    _hasMoreBooks.value = false
                } else {
                    if (isFirstLoad) {
                        _allBooksList.value = newBooks
                        _filteredAllBooksList.value = newBooks
                        _currentPage.value = 2 // Next page to load
                    } else {
                        val currentBooks = _allBooksList.value.toMutableList()
                        currentBooks.addAll(newBooks)
                        _allBooksList.value = currentBooks

                        // Apply current search filter to the new combined list
                        filterAllBooks(_searchQuery.value)

                        _currentPage.value = _currentPage.value + 1
                    }

                    // If we received fewer items than the page size, there are no more items
                    if (newBooks.size < PAGE_SIZE) {
                        _hasMoreBooks.value = false
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getLibraryList(limit: Int) {
        try {
            viewModelScope.launch {
                println("Loading libraries with limit: $limit")
                val libraries = librareaseRepo.getLibraries(limit)
                println("Loaded ${libraries.size} libraries")
                libraries.forEach { library ->
                    println("Library: ${library.name} (ID: ${library.id})")
                }
                _libraryList.value = libraries
            }
        } catch (e: Exception) {
            println("Error loading libraries: ${e.message}")
            e.printStackTrace()
        }
    }

    fun signOut() = repo.signOut()

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        filterBooks(query)
        filterAllBooks(query)
    }

    private fun filterBooks(query: String) {
        if (query.isBlank()) {
            _filteredBooksList.value = _booksList.value
            return
        }

        val lowercaseQuery = query.lowercase()
        _filteredBooksList.value = _booksList.value.filter { book ->
            book.title.lowercase().contains(lowercaseQuery) ||
                    book.author.lowercase().contains(lowercaseQuery)
        }
    }

    private fun filterAllBooks(query: String) {
        if (query.isBlank()) {
            _filteredAllBooksList.value = _allBooksList.value
            return
        }

        val lowercaseQuery = query.lowercase()
        _filteredAllBooksList.value = _allBooksList.value.filter { book ->
            book.title.lowercase().contains(lowercaseQuery) ||
                    book.author.lowercase().contains(lowercaseQuery)
        }
    }
}

    sealed class PushTokenState {
        object Idle : PushTokenState()
        object Loading : PushTokenState()
        object Success : PushTokenState()
        data class Error(val message: String) : PushTokenState()
    }

