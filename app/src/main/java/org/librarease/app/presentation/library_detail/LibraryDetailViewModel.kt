package org.librarease.app.presentation.library_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.librarease.app.domain.model.Library
import org.librarease.app.domain.model.Membership
import org.librarease.app.domain.repository.LibrareaseRepository
import javax.inject.Inject

@HiltViewModel
class LibraryDetailViewModel @Inject constructor(
    private val repository: LibrareaseRepository
) : ViewModel() {
    
    private val _memberships = MutableStateFlow<List<Membership>>(emptyList())
    val memberships: StateFlow<List<Membership>> = _memberships.asStateFlow()
    
    private val _library = MutableStateFlow<Library?>(null)
    val library: StateFlow<Library?> = _library.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    fun loadLibraryData(libraryId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                println("Loading library data for ID: $libraryId")
                
                // Try to get library data from API
                try {
                    val library = repository.getLibraryById(libraryId)
                    _library.value = library
                    println("Library loaded from API: ${library.name}")
                } catch (e: Exception) {
                    println("Failed to load library from API, creating placeholder: ${e.message}")
                    // Create a placeholder library if API fails
                    val placeholderLibrary = Library(
                        id = libraryId,
                        name = "Library $libraryId",
                        phoneNo = null,
                        email = null,
                        logo = null
                    )
                    _library.value = placeholderLibrary
                }
                
                // Load memberships
                val membershipList = repository.getMemberships(libraryId, 50)
                _memberships.value = membershipList
                println("Memberships loaded: ${membershipList.size} items")
                
            } catch (e: Exception) {
                println("Error loading library data: ${e.message}")
                e.printStackTrace()
                _memberships.value = emptyList()
                _library.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }
} 