package org.librarease.app.data.remote

import okhttp3.Response
import org.librarease.app.data.remote.request.FcmTokenRequest
import org.librarease.app.data.remote.response.BookDetailResponse
import org.librarease.app.data.remote.response.BookListResponse
import org.librarease.app.data.remote.response.LibraryListResponse
import org.librarease.app.data.remote.response.MembershipListResponse
import org.librarease.app.data.remote.response.SubscriptionListResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface LibrareaseApi {
    @GET("libraries")
    suspend fun getLibraries(
        @Query("limit") limit: Int? = null,
        @Query("page") page: Int? = null
    ): LibraryListResponse

    @GET("libraries/{id}")
    suspend fun getLibraryById(
        @Path("id") id: String
    ): LibraryListResponse.Library

    @GET("books")
    suspend fun getBooks(
        @Query("limit") limit: Int? = null,
        @Query("page") page: Int? = null
    ): BookListResponse

    @GET("books/{id}")
    suspend fun getBookById(
        @Path("id") id: String
    ): BookDetailResponse

    @GET("memberships")
    suspend fun getMemberships(
        @Query("library_id") libraryId: String,
    ): MembershipListResponse

    @GET("subscriptions")
    suspend fun getUserSubscriptions(
        @Query("user_id") userID: String
    ): SubscriptionListResponse

    @POST("users/me/push-token")
    suspend fun sendFcmToken(
        @Body request: FcmTokenRequest
    ): retrofit2.Response<Unit>

    companion object {
        const val BASE_URL = "https://librarease.org/api/v1/"
    }
}