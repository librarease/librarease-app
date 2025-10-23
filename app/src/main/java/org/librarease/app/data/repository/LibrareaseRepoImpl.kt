package org.librarease.app.data.repository

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import org.librarease.app.data.local.dao.BookDao
import org.librarease.app.data.local.dao.BookDetailDao
import org.librarease.app.data.local.mapper.toDomain
import org.librarease.app.data.local.mapper.toEntity
import org.librarease.app.data.mapper.ResponseMapper
import org.librarease.app.data.remote.service.LibrareaseNetworkService
import org.librarease.app.domain.model.BookDetail
import org.librarease.app.domain.model.BookItem
import org.librarease.app.domain.model.Library
import org.librarease.app.domain.model.Membership
import org.librarease.app.domain.model.Subscription
import org.librarease.app.domain.repository.LibrareaseRepository
import javax.inject.Inject

class LibrareaseRepoImpl @Inject constructor(
    private val networkService: LibrareaseNetworkService,
    private val bookDao: BookDao,
    private val bookDetailDao: BookDetailDao
): LibrareaseRepository {

    override suspend fun getBooks(limit: Int): List<BookItem> = withContext(Dispatchers.IO) {

        val cachedBooks = bookDao.getBooks(limit)
        
        if (cachedBooks.isNotEmpty()) {

            val books = cachedBooks.map { it.toDomain() }

            try {
                val response = networkService.getBooks(limit)
                val freshBooks = ResponseMapper.bookListMapper(response)
                bookDao.insertBooks(freshBooks.map { it.toEntity() })
            } catch (e: Exception) {
                Log.e("LibrareaseRepo", "Failed to refresh books from network", e)
            }
            
            return@withContext books
        } else {
            try {
                val response = networkService.getBooks(limit)
                val books = ResponseMapper.bookListMapper(response)
                bookDao.insertBooks(books.map { it.toEntity() })
                return@withContext books
            } catch (e: Exception) {
                Log.e("LibrareaseRepo", "Failed to fetch books from network", e)
                throw e
            }
        }
    }

    override suspend fun getBooksPaginated(limit: Int, page: Int): List<BookItem> = withContext(Dispatchers.IO) {
        val offset = (page - 1) * limit
        
        val cachedBooks = bookDao.getBooksPaginated(limit, offset)
        
        if (cachedBooks.isNotEmpty() && cachedBooks.size == limit) {
            val books = cachedBooks.map { it.toDomain() }
            
            try {
                val response = networkService.getBooksPaginated(limit, page)
                val freshBooks = ResponseMapper.bookListMapper(response)
                bookDao.insertBooks(freshBooks.map { it.toEntity() })
            } catch (e: Exception) {
                Log.e("LibrareaseRepo", "Failed to refresh paginated books", e)
            }
            
            return@withContext books
        } else {
            try {
                val response = networkService.getBooksPaginated(limit, page)
                val books = ResponseMapper.bookListMapper(response)
                bookDao.insertBooks(books.map { it.toEntity() })
                return@withContext books
            } catch (e: Exception) {
                Log.e("LibrareaseRepo", "Failed to fetch paginated books", e)
                throw e
            }
        }
    }

    override suspend fun getBookById(id: String): BookDetail = withContext(Dispatchers.IO) {
        // Try cache first
        val cachedDetail = bookDetailDao.getBookDetail(id)
        
        if (cachedDetail != null) {
            // Return cached data
            val bookDetail = cachedDetail.toDomain()
            
            // Refresh in background
            try {
                val response = networkService.getBookById(id)
                val freshDetail = ResponseMapper.bookDetailMapper(response)
                bookDetailDao.insertBookDetail(freshDetail.toEntity())
            } catch (e: Exception) {
                Log.e("LibrareaseRepo", "Failed to refresh book detail", e)
            }
            
            return@withContext bookDetail
        } else {
            // Fetch from network
            try {
                val response = networkService.getBookById(id)
                val bookDetail = ResponseMapper.bookDetailMapper(response)
                bookDetailDao.insertBookDetail(bookDetail.toEntity())
                return@withContext bookDetail
            } catch (e: Exception) {
                Log.e("LibrareaseRepo", "Failed to fetch book detail", e)
                throw e
            }
        }
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
    
    override suspend fun getMemberships(libraryId: String): List<Membership> {
        val response = networkService.getMemberships(libraryId)
        return ResponseMapper.membershipListMapper(response)
    }
    
    override suspend fun getUserSubscriptions(): List<Subscription> {
        val response = networkService.getUserSubscriptions()
        return ResponseMapper.subscriptionListMapper(response)
    }
}