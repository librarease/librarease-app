package org.librarease.app.domain.repository

import org.librarease.app.domain.model.BookItem
import org.librarease.app.domain.model.Library
import org.librarease.app.domain.model.Membership

interface LibrareaseRepository {

    suspend fun getBooks(limit: Int): List<BookItem>
    
    suspend fun getBooksPaginated(limit: Int, page: Int): List<BookItem>

    suspend fun getLibraries(limit: Int): List<Library>
    
    suspend fun getLibraryById(id: String): Library
    
    suspend fun getMemberships(libraryId: String, limit: Int): List<Membership>
}