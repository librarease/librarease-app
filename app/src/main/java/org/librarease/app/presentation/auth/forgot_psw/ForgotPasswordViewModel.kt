package org.librarease.app.presentation.auth.forgot_psw

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.librarease.app.core.EMPTY_STRING
import org.librarease.app.core.Resource
import org.librarease.app.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

typealias PasswordResetEmailResponse = Resource<Unit>

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val repo: AuthRepository
): ViewModel() {

    private val _email = MutableStateFlow(TextFieldValue(EMPTY_STRING))
    val email: StateFlow<TextFieldValue> = _email.asStateFlow()

    private val _passwordResetEmailState = MutableStateFlow<PasswordResetEmailResponse>(Resource.Idle)
    val passwordResetEmailState: StateFlow<PasswordResetEmailResponse> = _passwordResetEmailState.asStateFlow()

    fun onEmailChange(newPsw: TextFieldValue) {
        _email.value = newPsw
    }

    fun onSendPasswordResetEmail(email: String) = viewModelScope.launch {
        try {
            _passwordResetEmailState.value = Resource.Loading
            _passwordResetEmailState.value = Resource.Success(repo.sendPasswordResetEmail(email))
        } catch (e: Exception) {
            _passwordResetEmailState.value = Resource.Failure(e)
        }
    }
}