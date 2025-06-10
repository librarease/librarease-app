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
import javax.inject.Inject

typealias DeleteUserResponse = Resource<Unit>

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repo: AuthRepository
): ViewModel() {
    private val _authState = MutableStateFlow(repo.currentUser != null)
    val authState: StateFlow<Boolean> = _authState.asStateFlow()

    private val _deleteUserState = MutableStateFlow<DeleteUserResponse>(Resource.Idle)
    val deleteUserState: StateFlow<DeleteUserResponse> = _deleteUserState.asStateFlow()

    init {
        getAuthState()
    }

    private fun getAuthState() = viewModelScope.launch {
        repo.getAuthState().collect { isUserSignIn ->
            _authState.value = isUserSignIn
        }
    }

    fun signOut() = repo.signOut()

    fun deleteUser() = viewModelScope.launch {
        try {
            _deleteUserState.value = Resource.Loading
            _deleteUserState.value = Resource.Success(repo.deleteUser())
        } catch (e: Exception) {
            _deleteUserState.value = Resource.Failure(e)
        }
    }
}
