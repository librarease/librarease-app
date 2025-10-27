package org.librarease.app.data.remote.service

import org.librarease.app.data.remote.response.BookDetailResponse
import org.librarease.app.data.remote.response.BookListResponse
import org.librarease.app.data.remote.response.LibraryListResponse
import org.librarease.app.data.remote.response.MembershipListResponse
import org.librarease.app.data.remote.response.SubscriptionListResponse
import org.librarease.app.data.remote.response.SubscriptionResponse

interface LibrareaseNetworkService {
    suspend fun getBooks(limit: Int, search: String? = null): BookListResponse
    
    suspend fun getBooksPaginated(limit: Int, page: Int, search: String? = null): BookListResponse

    suspend fun getBookById(id: String): BookDetailResponse

    suspend fun getLibraries(page: Int? = null): LibraryListResponse
    
    suspend fun getLibraryById(id: String): LibraryListResponse.Library
    
    suspend fun getMemberships(libraryId: String): MembershipListResponse

    suspend fun getUserSubscriptions(): SubscriptionListResponse

    suspend fun sendFcmToken(token: String): Boolean
}