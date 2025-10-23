package org.librarease.app.domain.repository

import org.librarease.app.domain.model.BookDetail
import org.librarease.app.domain.model.BookItem
import org.librarease.app.domain.model.Library
import org.librarease.app.domain.model.Membership
import org.librarease.app.domain.model.Subscription

interface LibrareaseRepository {

    suspend fun getBooks(limit: Int): List<BookItem>
    
    suspend fun getBooksPaginated(limit: Int, page: Int): List<BookItem>

    suspend fun getBookById(id: String): BookDetail

    suspend fun getLibraries(limit: Int): List<Library>
    
    suspend fun getLibraryById(id: String): Library
    
    suspend fun getMemberships(libraryId: String): List<Membership>
    
    suspend fun getUserSubscriptions(): List<Subscription>
}