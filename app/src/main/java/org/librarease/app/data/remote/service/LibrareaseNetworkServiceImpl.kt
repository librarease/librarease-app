package org.librarease.app.data.remote.service

import android.util.Log
import org.librarease.app.data.remote.LibrareaseApi
import org.librarease.app.data.remote.request.FcmTokenRequest
import org.librarease.app.data.remote.response.BookDetailResponse
import org.librarease.app.data.remote.response.BookListResponse
import org.librarease.app.data.remote.response.LibraryListResponse
import org.librarease.app.data.remote.response.MembershipListResponse
import org.librarease.app.data.remote.response.SubscriptionListResponse
import org.librarease.app.data.remote.response.SubscriptionResponse
import org.librarease.app.di.module.FirebaseTokenProvider
import retrofit2.HttpException
import javax.inject.Inject

class LibrareaseNetworkServiceImpl @Inject constructor (
    private val api: LibrareaseApi,
    private val tokenProvider: FirebaseTokenProvider
): LibrareaseNetworkService {
    override suspend fun getBooks(limit: Int, search: String?): BookListResponse {
        return try {
            api.getBooks(limit = limit, search = search)
        } catch (e: Exception) {
            e.printStackTrace()
            BookListResponse(emptyList())
        }
    }
    
    override suspend fun getBooksPaginated(limit: Int, page: Int, search: String?): BookListResponse {
        return try {
            api.getBooks(limit = limit, page = page, search = search)
        } catch (e: Exception) {
            e.printStackTrace()
            BookListResponse(emptyList())
        }
    }

    override suspend fun getBookById(id: String): BookDetailResponse {
        return api.getBookById(id)
    }

    override suspend fun getLibraries(page: Int?): LibraryListResponse {
        return try {
            api.getLibraries(page = page)
        } catch (e: Exception) {
            e.printStackTrace()
            LibraryListResponse(emptyList())
        }
    }
    
    override suspend fun getLibraryById(id: String): LibraryListResponse.Library {
        return api.getLibraryById(id)
    }
    
    override suspend fun getMemberships(libraryId: String): MembershipListResponse {
        return try {
            api.getMemberships(libraryId = libraryId)
        } catch (e: Exception) {
            e.printStackTrace()
            MembershipListResponse(emptyList())
        }
    }

    override suspend fun getUserSubscriptions(): SubscriptionListResponse {
        return try {
            val userId = tokenProvider.getUserID()
                ?: throw Exception("User id not found in token ")
            Log.d("###subs", "Calling API with userId: $userId")
            val response = api.getUserSubscriptions(userId)
            Log.d("###subs", "API response: ${response.subscriptions}")
            response
        } catch (e: Exception) {
            Log.e("###subs", "Error fetching subscriptions", e)
        } as SubscriptionListResponse
    }

    override suspend fun sendFcmToken(token: String): Boolean {
        return try {
            val request = FcmTokenRequest(
                token = token,
                provider = "fcm"
            )
            val response = api.sendFcmToken(request)
            Log.d("##network", "Push token response: ${response.code()}")
            response.isSuccessful
        } catch (e: Exception) {
            Log.e("###network", "Error sending push token", e)
            false
        }
    }

}