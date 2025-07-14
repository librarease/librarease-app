package org.librarease.app.data.repository

import android.util.Log
import org.librarease.app.data.mapper.ResponseMapper
import org.librarease.app.data.remote.service.LibrareaseNetworkService
import org.librarease.app.domain.model.BookItem
import org.librarease.app.domain.model.Library
import org.librarease.app.domain.model.Membership
import org.librarease.app.domain.repository.LibrareaseRepository
import javax.inject.Inject

class LibrareaseRepoImpl @Inject constructor(
    private val networkService: LibrareaseNetworkService
): LibrareaseRepository {
    
    override suspend fun getBooks(limit: Int): List<BookItem> {
        val response = networkService.getBooks(limit)
        return ResponseMapper.bookListMapper(response)
    }
    
    override suspend fun getBooksPaginated(limit: Int, page: Int): List<BookItem> {
        val response = networkService.getBooksPaginated(limit, page)
        return ResponseMapper.bookListMapper(response)
    }

    override suspend fun getLibraries(limit: Int): List<Library> {
        val response = networkService.getLibraries(limit)
        return ResponseMapper.libraryListMapper(response)
    }
    
    override suspend fun getLibraryById(id: String): Library {
        val response = networkService.getLibraryById(id)
        Log.d("##response", "getLibraryById: ${response.name}")
        return ResponseMapper.libraryMapper(response)
    }
    
    override suspend fun getMemberships(libraryId: String, limit: Int): List<Membership> {
        val response = networkService.getMemberships(libraryId, limit)
        return ResponseMapper.membershipListMapper(response)
    }
}