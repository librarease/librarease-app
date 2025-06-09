package org.librarease.app.data.remote

import retrofit2.http.GET


interface LibrareaseApi {
    @GET("libraries?")
    suspend fun getLibraries()


    companion object {
        const val BASE_URL = "https://librarease.org/"
    }
}