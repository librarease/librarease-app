package org.librarease.app.data.remote.service

import org.librarease.app.data.remote.response.BookListResponse
import org.librarease.app.data.remote.response.LibraryListResponse
import org.librarease.app.data.remote.response.MembershipListResponse
import org.librarease.app.data.remote.response.SubscriptionListResponse
import org.librarease.app.data.remote.response.SubscriptionResponse

interface LibrareaseNetworkService {
    suspend fun getBooks(limit: Int): BookListResponse
    
    suspend fun getBooksPaginated(limit: Int, page: Int): BookListResponse

    suspend fun getLibraries(limit: Int): LibraryListResponse
    
    suspend fun getLibraryById(id: String): LibraryListResponse.Library
    
    suspend fun getMemberships(libraryId: String): MembershipListResponse

    suspend fun getUserSubscriptions(): SubscriptionListResponse
}