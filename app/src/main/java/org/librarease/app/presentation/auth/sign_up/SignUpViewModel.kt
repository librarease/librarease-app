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

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val repository: AuthRepository
): ViewModel() {

    private val _email = MutableStateFlow(TextFieldValue(EMPTY_STRING))
    val email: StateFlow<TextFieldValue> = _email.asStateFlow()

    private val _password = MutableStateFlow(TextFieldValue(EMPTY_STRING))
    val password: StateFlow<TextFieldValue> = _password.asStateFlow()
    
    private val _fullName = MutableStateFlow(TextFieldValue(EMPTY_STRING))
    val fullName: StateFlow<TextFieldValue> = _fullName.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _signUpState = MutableStateFlow<SignUpResponse>(Resource.Idle)
    val signUpState: StateFlow<SignUpResponse> = _signUpState.asStateFlow()


    fun onEmailChange(newEmail: TextFieldValue) {
        _email.value = newEmail
    }

    fun onPasswordChange(newPsw: TextFieldValue) {
        _password.value = newPsw
    }
    
    fun onFullNameChange(newFullName: TextFieldValue) {
        _fullName.value = newFullName
    }

    fun onSignUpWithEmailAndPassword(email: String, password: String, fullName: String) = viewModelScope.launch {
        _isLoading.value = true
        try {
            _signUpState.value = Resource.Loading
            // For now, we're still using the existing repository method that doesn't use fullName
            // In a real implementation, you'd want to update the repository to store the full name
            val result = repository.signUpWithEmailAndPassword(email, password)
            // TODO: Store the full name in the user profile after successful sign-up
            _signUpState.value = Resource.Success(result)
        } catch (e: Exception) {
            _signUpState.value = Resource.Failure(e)
            _isLoading.value = false
        }
    }

}