package org.librarease.app.data.remote.service

import org.librarease.app.data.remote.response.BookListResponse
import org.librarease.app.data.remote.response.LibraryListResponse

interface LibrareaseNetworkService {
    suspend fun getBooks(limit: Int): BookListResponse

    suspend fun getLibraries(limit: Int): LibraryListResponse
}