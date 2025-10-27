package org.librarease.app.presentation.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.librarease.app.core.Resource
import org.librarease.app.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.librarease.app.domain.model.BookItem
import org.librarease.app.domain.model.Library
import org.librarease.app.domain.repository.LibrareaseRepository
import org.librarease.app.presentation.books.BooksUiState
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val repo: AuthRepository,
    private val librareaseRepo: LibrareaseRepository
): ViewModel() {
    private val _authState = MutableStateFlow(repo.currentUser != null)
    val authState: StateFlow<Boolean> = _authState.asStateFlow()

    private val _userName = MutableStateFlow<String?>(null)
    val userName: StateFlow<String?> = _userName.asStateFlow()

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
    
    // Search debounce job
    private var searchJob: Job? = null
    private val SEARCH_DEBOUNCE_DELAY = 1000L //1 seconds like Chrome

    private val _allBooksList = MutableStateFlow<List<BookItem>>(emptyList())
    val allBooksList: StateFlow<List<BookItem>> = _allBooksList.asStateFlow()

    private val _filteredAllBooksList = MutableStateFlow<List<BookItem>>(emptyList())
    val filteredAllBooksList: StateFlow<List<BookItem>> = _filteredAllBooksList.asStateFlow()

    private val _booksUiState = MutableStateFlow(BooksUiState())
    val booksUiState: StateFlow<BooksUiState> = _booksUiState.asStateFlow()

    private val PAGE_SIZE = 20

    init {
        getAuthState()
    }

    private fun getAuthState() = viewModelScope.launch {
        repo.getAuthState().collect { isUserSignIn ->
            _authState.value = isUserSignIn
            if (isUserSignIn) {
                _userName.value = repo.currentUser?.displayName ?: repo.currentUser?.email?.substringBefore("@")
            } else {
                _userName.value = null
            }
        }
    }

    private fun getBookList(limit: Int) {
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
        if (_booksUiState.value.books.isEmpty() && !_booksUiState.value.isLoading) {
            loadMoreBooks(true)
        }
    }

    fun loadMoreBooks(isFirstLoad: Boolean = false) {
        val currentState = _booksUiState.value
        if (currentState.isLoading || (!currentState.hasMoreBooks && !isFirstLoad)) return

        viewModelScope.launch {
            // Only show loading if there's no cached data
            if (currentState.books.isEmpty()) {
                _booksUiState.update { 
                    it.copy(
                        isLoading = isFirstLoad,
                        isLoadingMore = !isFirstLoad,
                        error = null
                    )
                }
            }
            
            try {
                val page = if (isFirstLoad) 1 else currentState.currentPage
                val searchQuery = if (currentState.searchQuery.isBlank()) null else currentState.searchQuery
                val newBooks = librareaseRepo.getBooksPaginated(PAGE_SIZE, page, searchQuery)

                _booksUiState.update { state ->
                    if (newBooks.isEmpty()) {
                        state.copy(
                            isLoading = false,
                            isLoadingMore = false,
                            hasMoreBooks = false
                        )
                    } else {
                        val updatedBooks = if (isFirstLoad) {
                            newBooks
                        } else {
                            state.books + newBooks
                        }
                        
                        state.copy(
                            books = updatedBooks,
                            isLoading = false,
                            isLoadingMore = false,
                            hasMoreBooks = newBooks.size >= PAGE_SIZE,
                            currentPage = if (isFirstLoad) 2 else state.currentPage + 1
                        )
                    }
                }

                _allBooksList.value = _booksUiState.value.books
                _filteredAllBooksList.value = _booksUiState.value.books
                _isLoading.value = false
                _hasMoreBooks.value = _booksUiState.value.hasMoreBooks
                _currentPage.value = _booksUiState.value.currentPage
                
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error loading books", e)
                _booksUiState.update { 
                    it.copy(
                        isLoading = false,
                        isLoadingMore = false,
                        error = e.message ?: "Unknown error occurred"
                    )
                }
                _isLoading.value = false
            }
        }
    }

    fun loadLibraries() {
        if (_libraryList.value.isEmpty()) {
            viewModelScope.launch {
                try {
                    val libraries = librareaseRepo.getLibraries(null)
                    _libraryList.value = libraries
                } catch (e: Exception) {
                    Log.e("MainViewModel", "Error loading libraries", e)
                }
            }
        }
    }

    fun signOut() = repo.signOut()

    fun updateSearchQuery(query: String) {
        searchJob?.cancel()
        
        _booksUiState.update { it.copy(searchQuery = query) }
        _searchQuery.value = query

        if (query.isBlank()) {
            _booksUiState.update { 
                it.copy(
                    currentPage = 1,
                    hasMoreBooks = true,
                    books = emptyList()
                )
            }
            loadMoreBooks(isFirstLoad = true)
            return
        }
        
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            
            _booksUiState.update {
                it.copy(isLoading = true, error = null, books = emptyList())
            }
            
            try {
                val searchResults = librareaseRepo.getBooksPaginated(PAGE_SIZE, 1, query)
                
                _booksUiState.update { state ->
                    state.copy(
                        books = searchResults,
                        isLoading = false,
                        hasMoreBooks = searchResults.size >= PAGE_SIZE,
                        currentPage = 2
                    )
                }
                
                _allBooksList.value = searchResults
                _filteredAllBooksList.value = searchResults
                _isLoading.value = false
                
            } catch (e: Exception) {
                _booksUiState.update { 
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Search failed"
                    )
                }
                _isLoading.value = false
            }
        }
    }
    
    fun clearBooksError() {
        _booksUiState.update { it.copy(error = null) }
    }
}


