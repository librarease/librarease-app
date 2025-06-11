package org.librarease.app.data.remote

import org.librarease.app.data.remote.response.BookListResponse
import org.librarease.app.data.remote.response.LibraryListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface LibrareaseApi {
    @GET("libraries")
    suspend fun getLibraries(
        @Query("limit") limit: Int? = null,
        @Query("page") page: Int? = null
    ): LibraryListResponse

    @GET("books")
    suspend fun getBooks(
        @Query("limit") limit: Int? = null,
        @Query("page") page: Int? = null
    ): BookListResponse

    companion object {
        const val BASE_URL = "https://librarease.org/api/v1/"
    }
}