package org.librarease.app.data.remote.service

import org.librarease.app.data.remote.LibrareaseApi
import org.librarease.app.data.remote.response.BookListResponse
import org.librarease.app.data.remote.response.LibraryListResponse
import retrofit2.HttpException

class LibrareaseNetworkServiceImpl(
    private val api: LibrareaseApi
): LibrareaseNetworkService {
    override suspend fun getBooks(limit: Int): BookListResponse {
        return try {
            api.getBooks(limit = limit)
        } catch (e: Exception) {
            e.printStackTrace()
            BookListResponse(emptyList())
        }
    }

    override suspend fun getLibraries(limit: Int): LibraryListResponse {
        return try {
            api.getLibraries(limit = limit)
        } catch (e: Exception) {
            e.printStackTrace()
            LibraryListResponse(emptyList())
        }
    }
}