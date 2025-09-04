package org.librarease.app.di.module

import com.auth0.android.jwt.JWT
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

object JwtUtils {
    fun getUserFromToken(token: String): String? {
        return try {
            val jwt = JWT(token)

            jwt.getClaim("user_id").asString() ?:
            jwt.getClaim("sub").asString() ?:
            jwt.getClaim("uid").asString() ?:
            jwt.subject
        } catch (e: Exception) {
            null
        }
    }
}
class FirebaseTokenProvider @Inject constructor() {
    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun getToken(): String? = suspendCancellableCoroutine { cont ->
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            cont.resume(null) {}
        } else {
            user.getIdToken(true)
                .addOnSuccessListener { result -> cont.resume(result.token) {} }
                .addOnFailureListener { cont.resume(null) {} }
        }
    }
    suspend fun getUserID(): String? {
        val token = getToken() ?: return null
        return JwtUtils.getUserFromToken(token)
    }
}

class AuthInterceptor @Inject constructor(
    private val tokenProvider: FirebaseTokenProvider
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking { tokenProvider.getToken() } // Block just for interceptor
        val requestBuilder = chain.request().newBuilder()
        token?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }
        return chain.proceed(requestBuilder.build())
    }
}



