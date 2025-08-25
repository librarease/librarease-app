package org.librarease.app.presentation.auth

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import okhttp3.internal.connection.Exchange

class FirebaseTokenProvider: TokenProvider {
    override suspend fun getToken(): String? {
        return try {
            FirebaseAuth.getInstance().currentUser?.getIdToken(false)?.await()?.token
        } catch (e: Exception) {
            null
        }
    }
}