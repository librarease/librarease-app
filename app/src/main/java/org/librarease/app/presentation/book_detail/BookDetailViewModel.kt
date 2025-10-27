package org.librarease.app.presentation.book_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.librarease.app.domain.model.BookDetail
import org.librarease.app.domain.repository.LibrareaseRepository
import javax.inject.Inject

@HiltViewModel
class BookDetailViewModel @Inject constructor(
    private val repository: LibrareaseRepository
) : ViewModel() {


    private val _bookDetail = MutableStateFlow<BookDetail?>(null)
    val bookDetail: StateFlow<BookDetail?> = _bookDetail.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    private var currentBookId: String? = null
    
    fun loadBookDetail(bookId: String) {
        if (currentBookId == bookId && _bookDetail.value != null) {
            return
        }
        
        currentBookId = bookId
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val book = repository.getBookById(bookId)
                _bookDetail.value = book
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to load book details"
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}
