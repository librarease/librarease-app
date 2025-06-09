package org.librarease.app.presentation.auth.sign_up

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

typealias SignUpResponse = Resource<Unit>
typealias EmailVerificationResponse = Resource<Unit>

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val repository: AuthRepository
): ViewModel() {

    private val _email = MutableStateFlow(TextFieldValue(EMPTY_STRING))
    val email: StateFlow<TextFieldValue> = _email.asStateFlow()

    private val _password = MutableStateFlow(TextFieldValue(EMPTY_STRING))
    val password: StateFlow<TextFieldValue> = _password.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _signUpState = MutableStateFlow<SignUpResponse>(Resource.Idle)
    val signUpState: StateFlow<SignUpResponse> = _signUpState.asStateFlow()

    private val _emailVerificationState = MutableStateFlow<EmailVerificationResponse>(Resource.Idle)
    val emailVerificationState: StateFlow<EmailVerificationResponse> = _emailVerificationState.asStateFlow()

    fun onEmailChange(newEmail: TextFieldValue) {
        _email.value = newEmail
    }

    fun onPasswordChange(newPsw: TextFieldValue) {
        _password.value = newPsw
    }

    fun onSignUpWithEmailAndPassword(email: String, password: String) = viewModelScope.launch {
        _isLoading.value = true
        try {
            _signUpState.value = Resource.Loading
            _signUpState.value = Resource.Success(repository.signUpWithEmailAndPassword(email, password))
        } catch (e: Exception) {
            _signUpState.value = Resource.Failure(e)
            _isLoading.value = false
        }
    }

    fun sendEmailVerification() = viewModelScope.launch {
        try {
            _emailVerificationState.value = Resource.Loading
            _emailVerificationState.value = Resource.Success(repository.sendEmailVerification())
        } catch (e: Exception) {
            _emailVerificationState.value = Resource.Failure(e)
        } finally {
            _isLoading.value = false
        }
    }
}