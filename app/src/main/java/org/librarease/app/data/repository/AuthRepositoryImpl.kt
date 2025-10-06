package org.librarease.app.data.repository

import android.util.Log
import org.librarease.app.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import org.librarease.app.data.remote.service.LibrareaseNetworkService
import org.librarease.app.data.remote.service.LibrareaseNetworkServiceImpl
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val networkService: LibrareaseNetworkService
) : AuthRepository {
    override val currentUser get() = auth.currentUser

    override suspend fun signUpWithEmailAndPassword(email: String, password: String) {
        try {
            val result = auth.createUserWithEmailAndPassword(email.trim(), password).await()
            Log.d("AuthRepository", "User created successfully: ${result.user?.uid}")
        } catch (e: Exception) {
            Log.e("AuthRepository", "Failed to create user with email $email: ${e.message}", e)
            throw e
        }
    }

    override suspend fun signInWithEmailAndPassword(email: String, password: String) {
        try { val result = auth.signInWithEmailAndPassword(email, password).await()
            Log.d("AuthRepository", "User sign in successfully: ${result.user?.uid}")
        } catch (e: Exception) {
            throw e
        }

    }

    override suspend fun deleteUser() {
        currentUser?.delete()?.await()
    }

    override suspend fun reloadUser() {
        currentUser?.reload()?.await()
    }

    override suspend fun sendPasswordResetEmail(email: String) {
        try {
            Log.d("AuthRepository", "Attempting to send password reset email to: $email")
            auth.sendPasswordResetEmail(email.trim()).await()
            Log.d("AuthRepository", "Password reset email sent successfully to: $email")
        } catch (e: Exception) {
            Log.e("AuthRepository", "Failed to send password reset email to $email: ${e.message}", e)
            throw Exception("Failed to send password reset email: ${e.message}")
        }
    }

    override fun signOut() = auth.signOut()

    override fun getAuthState() = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser != null)
        }
        auth.addAuthStateListener(authStateListener)
        awaitClose {
            auth.removeAuthStateListener(authStateListener)
        }
    }

    override suspend fun sendFcmToken(fcmToken: String): Boolean {
        return networkService.sendFcmToken(fcmToken)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getFcmToken(): String? = suspendCancellableCoroutine { continuation ->
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if(task.isSuccessful) {
                continuation.resume(task.result, null)
            } else {
                continuation.resume(null)
            }
        }
    }

}