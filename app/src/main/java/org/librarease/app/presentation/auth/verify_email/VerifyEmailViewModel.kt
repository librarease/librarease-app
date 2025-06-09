package org.librarease.app.presentation.auth.verify_email

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

typealias ReloadUserResponse = Resource<Unit>

@HiltViewModel
class VerifyEmailViewModel @Inject constructor(
    private val repo: AuthRepository
): ViewModel() {

    val isEmailVerified get() = repo.currentUser?.isEmailVerified == true

    private val _reloadUserState = MutableStateFlow<ReloadUserResponse>(Resource.Idle)
    val reloadUserState: StateFlow<ReloadUserResponse> = _reloadUserState.asStateFlow()

    private val _isEmailVerifiedState = MutableStateFlow<Boolean>(isEmailVerified)
    val isEmailVerifiedState: StateFlow<Boolean> = _isEmailVerifiedState.asStateFlow()

    fun reloadUser() = viewModelScope.launch {
        try {
            _reloadUserState.value = Resource.Loading
            _reloadUserState.value = Resource.Success(repo.reloadUser())
            _isEmailVerifiedState.value = isEmailVerified
        } catch (e: Exception) {
            _reloadUserState.value = Resource.Failure(e)
        }
    }
}