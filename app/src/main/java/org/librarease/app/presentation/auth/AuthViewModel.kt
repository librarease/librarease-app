package org.librarease.app.presentation.auth

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow

class AuthViewModel : ViewModel() {

    private val _idTokenState = MutableStateFlow<String?>(null)
    val idTokenState: MutableStateFlow<String?> get() = _idTokenState

    fun fetchFirebaseToken() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            user.getIdToken(true)
                .addOnSuccessListener { result ->
                    _idTokenState.value = result.token
                }
                .addOnFailureListener { e ->
                    _idTokenState.value = null // or handle error state
                    Log.e("AuthViewModel", "Token fetch failed", e)
                }
        } else {
            _idTokenState.value = null // user not logged in
        }
    }
}