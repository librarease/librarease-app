package org.librarease.app.data.repository

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import org.librarease.app.data.local.dao.BookDao
import org.librarease.app.data.local.dao.BookDetailDao
import org.librarease.app.data.local.dao.LibraryDao
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
    private val bookDetailDao: BookDetailDao,
    private val libraryDao: LibraryDao
): LibrareaseRepository {

    override suspend fun getBooks(limit: Int, search: String?): List<BookItem> = withContext(Dispatchers.IO) {
        if (!search.isNullOrBlank()) {
            return@withContext try {
                val response = networkService.getBooksPaginated(limit, 1, search)
                ResponseMapper.bookListMapper(response)
            } catch (e: Exception) {
                Log.e("LibrareaseRepo", "Failed to search books", e)
                emptyList()
            }
        }

        val cachedBooks = bookDao.getBooks(limit)
        
        if (cachedBooks.isNotEmpty()) {
            val books = cachedBooks.map { it.toDomain() }

            try {
                val response = networkService.getBooksPaginated(limit, 1, null)
                val freshBooks = ResponseMapper.bookListMapper(response)
                bookDao.insertBooks(freshBooks.map { it.toEntity() })
            } catch (e: Exception) {
                Log.e("LibrareaseRepo", "Failed to refresh books from network", e)
            }
            
            return@withContext books
        } else {
            try {
                val response = networkService.getBooksPaginated(limit, 1, null)
                val books = ResponseMapper.bookListMapper(response)
                bookDao.insertBooks(books.map { it.toEntity() })
                return@withContext books
            } catch (e: Exception) {
                Log.e("LibrareaseRepo", "Failed to fetch books from network", e)
                throw e
            }
        }
    }

    override suspend fun getBooksPaginated(limit: Int, page: Int, search: String?): List<BookItem> = withContext(Dispatchers.IO) {
        if (!search.isNullOrBlank()) {
            return@withContext try {
                val response = networkService.getBooksPaginated(limit, page, search)
                ResponseMapper.bookListMapper(response)
            } catch (e: Exception) {
                Log.e("LibrareaseRepo", "Failed to search books", e)
                emptyList()
            }
        }

        // Normal cache-first logic for non-search requests
        val offset = (page - 1) * limit
        val cachedBooks = bookDao.getBooksPaginated(limit, offset)
        
        if (cachedBooks.isNotEmpty() && cachedBooks.size == limit) {
            val books = cachedBooks.map { it.toDomain() }
            
            try {
                val response = networkService.getBooksPaginated(limit, page, null)
                val freshBooks = ResponseMapper.bookListMapper(response)
                bookDao.insertBooks(freshBooks.map { it.toEntity() })
            } catch (e: Exception) {
                Log.e("LibrareaseRepo", "Failed to refresh paginated books", e)
            }
            
            return@withContext books
        } else {
            try {
                val response = networkService.getBooksPaginated(limit, page, null)
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
        val cachedDetail = bookDetailDao.getBookDetail(id)
        
        if (cachedDetail != null) {
            val bookDetail = cachedDetail.toDomain()
            
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

    override suspend fun getLibraries(page: Int?): List<Library> = withContext(Dispatchers.IO) {
        val cachedLibraries = libraryDao.getLibraries(1000)
        
        if (cachedLibraries.isNotEmpty()) {
            val libraries = cachedLibraries.map { it.toDomain() }
            
            try {
                val response = networkService.getLibraries(page)
                val freshLibraries = ResponseMapper.libraryListMapper(response)
                libraryDao.insertLibraries(freshLibraries.map { it.toEntity() })
            } catch (e: Exception) {
                Log.e("LibrareaseRepo", "Failed to refresh libraries", e)
            }
            
            return@withContext libraries
        } else {
            try {
                val response = networkService.getLibraries(page)
                val libraries = ResponseMapper.libraryListMapper(response)
                libraryDao.insertLibraries(libraries.map { it.toEntity() })
                return@withContext libraries
            } catch (e: Exception) {
                Log.e("LibrareaseRepo", "Failed to fetch libraries", e)
                throw e
            }
        }
    }
    
    override suspend fun getLibraryById(id: String): Library = withContext(Dispatchers.IO) {
        val cachedLibrary = libraryDao.getLibraryById(id)
        
        if (cachedLibrary != null) {
            val library = cachedLibrary.toDomain()
            
            try {
                val response = networkService.getLibraryById(id)
                val freshLibrary = ResponseMapper.libraryMapper(response)
                libraryDao.insertLibrary(freshLibrary.toEntity())
            } catch (e: Exception) {
                Log.e("LibrareaseRepo", "Failed to refresh library detail", e)
            }
            
            return@withContext library
        } else {
            try {
                val response = networkService.getLibraryById(id)
                val library = ResponseMapper.libraryMapper(response)
                libraryDao.insertLibrary(library.toEntity())
                return@withContext library
            } catch (e: Exception) {
                Log.e("LibrareaseRepo", "Failed to fetch library detail", e)
                throw e
            }
        }
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