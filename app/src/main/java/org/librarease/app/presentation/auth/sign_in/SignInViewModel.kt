package org.librarease.app.presentation.auth.sign_in

import android.content.Context
import android.util.Log
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
import org.librarease.app.core.EMPTY_STRING
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.librarease.app.core.PushTokenWorker
import org.librarease.app.core.Resource
import org.librarease.app.core.logErrorMessage
import org.librarease.app.domain.repository.AuthRepository
import javax.inject.Inject
import kotlin.io.path.Path

typealias SignInResponse = Resource<Unit>

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val repo: AuthRepository,
): ViewModel() {

    private val _email = MutableStateFlow(TextFieldValue(EMPTY_STRING))
    val email: StateFlow<TextFieldValue> = _email.asStateFlow()

    private val _password = MutableStateFlow(TextFieldValue(EMPTY_STRING))
    val password: StateFlow<TextFieldValue> = _password.asStateFlow()

    private val _signInState = MutableStateFlow<SignInResponse>(Resource.Idle)
    val signInState: StateFlow<SignInResponse> = _signInState.asStateFlow()

    fun onEmailChange(newEmail: TextFieldValue) { _email.value = newEmail }
    fun onPasswordChange(newPassword: TextFieldValue) { _password.value = newPassword }

    fun signInWithEmailAndPassword(email: String, password: String) = viewModelScope.launch {
        _signInState.value = Resource.Loading
        try {
            val user = repo.signInWithEmailAndPassword(email, password)
            _signInState.value = Resource.Success(user)
        } catch (e: Exception) {
            Log.e("###login", "Sign in failed", e)
            _signInState.value = Resource.Failure(e)
        }
    }

    fun onSignInSuccess(context: Context) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("SignInViewModel", "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }

            val token = task.result
            Log.d("SignInViewModel", "Sign in successful, enqueuing FCM token worker.")
            Log.d("##signin", "success to push token: $token")
            PushTokenWorker.enqueue(context.applicationContext, token)
        }
    }
}