package org.librarease.app.data.remote

import org.librarease.app.data.remote.response.BookListResponse
import org.librarease.app.data.remote.response.LibraryListResponse
import org.librarease.app.data.remote.response.MembershipListResponse
import org.librarease.app.data.remote.response.SubscriptionListResponse
import org.librarease.app.data.remote.response.SubscriptionResponse
import retrofit2.http.GET
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

    @GET("memberships")
    suspend fun getMemberships(
        @Query("library_id") libraryId: String,
    ): MembershipListResponse

    @GET("subscriptions")
    suspend fun getUserSubscriptions(): SubscriptionListResponse

    companion object {
        const val BASE_URL = "https://librarease.org/api/v1/"
    }
}