package org.librarease.app.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.librarease.app.core.Resource
import org.librarease.app.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val _libraryList = MutableStateFlow<List<Library>>(emptyList())
    val libraryList: StateFlow<List<Library>> = _libraryList.asStateFlow()

    init {
        getAuthState()
        getBookList(20)
        getLibraryList(5 )
    }

    private fun getAuthState() = viewModelScope.launch {
        repo.getAuthState().collect { isUserSignIn ->
            _authState.value = isUserSignIn
        }
    }

    fun getBookList(limit: Int) {
        try {
           viewModelScope.launch {
               _booksList.value = librareaseRepo.getBooks(limit)
           }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
     fun getLibraryList(limit: Int) {
        try {
            viewModelScope.launch {
                _libraryList.value = librareaseRepo.getLibraries(limit)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun signOut() = repo.signOut()

}
