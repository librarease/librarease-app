package org.librarease.app.data.local.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.librarease.app.data.local.entity.BookDetailEntity

@Dao
interface BookDetailDao {
    
    @Query("SELECT * FROM book_details WHERE id = :bookId")
    suspend fun getBookDetail(bookId: String): BookDetailEntity?
    
    @Query("SELECT * FROM book_details WHERE id = :bookId")
    fun getBookDetailFlow(bookId: String): Flow<BookDetailEntity?>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookDetail(bookDetail: BookDetailEntity)
    
    @Update
    suspend fun updateBookDetail(bookDetail: BookDetailEntity)
    
    @Delete
    suspend fun deleteBookDetail(bookDetail: BookDetailEntity)
    
    @Query("DELETE FROM book_details")
    suspend fun deleteAllBookDetails()
    
    @Query("DELETE FROM book_details WHERE cachedAt < :timestamp")
    suspend fun deleteOldBookDetails(timestamp: Long)
}
