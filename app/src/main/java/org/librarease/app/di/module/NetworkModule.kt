package org.librarease.app.di.module

import android.util.Log
import com.auth0.android.jwt.JWT
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.serialization.json.JsonObject
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import org.json.JSONObject
import org.librarease.app.data.local.model.LibrareaseClaim
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

object JwtUtils {
    fun getUserIdFromToken(token: String): String? {
        return try {
            val jwt = JWT(token)
            val claim = jwt.getClaim("librarease").asObject(LibrareaseClaim::class.java)
            Log.d("##JWT_DEBUG", "user id = ${claim?.id}")

            if(claim?.id.isNullOrEmpty()) {
                return null
            }
            claim.id
        } catch (e: Exception) {
            Log.e("##JWT_DEBUG", "Error parsing token", e)
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
                .addOnSuccessListener { result ->
                    val token = result.token
                    cont.resume(token) {}
                }
                .addOnFailureListener { cont.resume(null) {} }
        }
    }

    suspend fun getUserID(): String? {
        val token = getToken() ?: return null
        return JwtUtils.getUserIdFromToken(token)
    }
}

class AuthInterceptor @Inject constructor(
    private val tokenProvider: FirebaseTokenProvider
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking { tokenProvider.getToken() }
        val requestBuilder = chain.request().newBuilder()
        token?.let {
            android.util.Log.d("AuthInterceptor", "Adding token to request: Bearer ${it.take(20)}...")
            requestBuilder.addHeader("Authorization", "Bearer $it")
        } ?: run {
            android.util.Log.w("AuthInterceptor", "No token available for request")
        }
        return chain.proceed(requestBuilder.build())
    }
}




