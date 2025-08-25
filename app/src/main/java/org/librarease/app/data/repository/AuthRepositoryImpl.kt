package org.librarease.app.data.repository

import org.librarease.app.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import org.librarease.app.data.remote.LibrareaseApi
import org.librarease.app.data.remote.response.SubscriptionResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val api: LibrareaseApi
) : AuthRepository {
    override val currentUser get() = auth.currentUser

    override suspend fun signUpWithEmailAndPassword(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).await()
    }

    override suspend fun sendEmailVerification() {
        currentUser?.sendEmailVerification()?.await()
    }

    override suspend fun signInWithEmailAndPassword(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).await()
    }

    override suspend fun deleteUser() {
        currentUser?.delete()?.await()
    }

    override suspend fun reloadUser() {
        currentUser?.reload()?.await()
    }

    override suspend fun sendPasswordResetEmail(email: String) {
        auth.sendPasswordResetEmail(email).await()
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

    override suspend fun getUserSubscriptions(userId: String): Result<List<SubscriptionResponse>> {
        return try {
            val response = api.getUserSubscriptions(userId)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}