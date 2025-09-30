package org.librarease.app.presentation.auth.sign_up

import android.util.Log
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
import org.librarease.app.presentation.main.PushTokenState
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

    private val _fcmTokenState = MutableStateFlow<PushTokenState>(PushTokenState.Idle)
    val fcmTokenState: StateFlow<PushTokenState> = _fcmTokenState


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
            repository.signUpWithEmailAndPassword(email.trim(), password)
            _signUpState.value = Resource.Success(Unit)
            _isLoading.value = false
        } catch (e: Exception) {
            Log.e("SignUpViewModel", "Sign up failed: ${e.message}", e)
            _signUpState.value = Resource.Failure(e)
            _isLoading.value = false
        }
    }

    fun sendFcmToken(token: String) {
        viewModelScope.launch {
            _fcmTokenState.value = PushTokenState.Loading
            try {
                val success = repository.sendFcmToken(token)
                if (success) {
                    _fcmTokenState.value = PushTokenState.Success
                    Log.d("##token", "Push token successfully ")
                } else {
                    _fcmTokenState.value = PushTokenState.Error("Failed to send push token")
                    Log.e("###token", "Failed to send push token")
                }
            } catch (e: Exception) {
                _fcmTokenState.value = PushTokenState.Error(e.message ?: "Unknown error")
                Log.e("##token", "Failed to push token ",)
            }
        }
    }
}