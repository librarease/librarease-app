package org.librarease.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.librarease.app.data.local.entity.LibraryEntity

@Dao
interface LibraryDao {
    @Query("SELECT * FROM libraries ORDER BY cachedAt DESC LIMIT :limit")
    suspend fun getLibraries(limit: Int): List<LibraryEntity>
    
    @Query("SELECT * FROM libraries WHERE id = :id")
    suspend fun getLibraryById(id: String): LibraryEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLibraries(libraries: List<LibraryEntity>)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLibrary(library: LibraryEntity)
    
    @Query("DELETE FROM libraries")
    suspend fun clearAll()
}
