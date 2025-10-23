package org.librarease.app.data.local.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.librarease.app.data.local.entity.BookEntity

@Dao
interface BookDao {
    
    @Query("SELECT * FROM books ORDER BY cachedAt DESC")
    fun getAllBooksFlow(): Flow<List<BookEntity>>
    
    @Query("SELECT * FROM books ORDER BY cachedAt DESC")
    suspend fun getAllBooks(): List<BookEntity>
    
    @Query("SELECT * FROM books ORDER BY cachedAt DESC LIMIT :limit")
    suspend fun getBooks(limit: Int): List<BookEntity>
    
    @Query("SELECT * FROM books ORDER BY cachedAt DESC LIMIT :limit OFFSET :offset")
    suspend fun getBooksPaginated(limit: Int, offset: Int): List<BookEntity>
    
    @Query("SELECT * FROM books WHERE id = :bookId")
    suspend fun getBookById(bookId: String): BookEntity?
    
    @Query("SELECT * FROM books WHERE id = :bookId")
    fun getBookByIdFlow(bookId: String): Flow<BookEntity?>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: BookEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooks(books: List<BookEntity>)
    
    @Update
    suspend fun updateBook(book: BookEntity)
    
    @Delete
    suspend fun deleteBook(book: BookEntity)
    
    @Query("DELETE FROM books")
    suspend fun deleteAllBooks()
    
    @Query("SELECT COUNT(*) FROM books")
    suspend fun getBooksCount(): Int
    
    @Query("DELETE FROM books WHERE cachedAt < :timestamp")
    suspend fun deleteOldBooks(timestamp: Long)
}
