package org.librarease.app.domain.repository

import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import org.librarease.app.data.remote.response.SubscriptionResponse

interface AuthRepository {
    val currentUser: FirebaseUser?

    suspend fun signUpWithEmailAndPassword(email: String, password: String)

    suspend fun sendEmailVerification()

    suspend fun signInWithEmailAndPassword(email: String, password: String)

    suspend fun deleteUser()

    suspend fun reloadUser()

    suspend fun sendPasswordResetEmail(email: String)

    fun signOut()

    fun getAuthState(): Flow<Boolean>

    suspend fun getUserSubscriptions(userId: String): Result<List<SubscriptionResponse>>
}