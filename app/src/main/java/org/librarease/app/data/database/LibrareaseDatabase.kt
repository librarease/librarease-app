package org.librarease.app.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import org.librarease.app.data.local.dao.BookDao
import org.librarease.app.data.local.dao.BookDetailDao
import org.librarease.app.data.local.dao.LibraryDao
import org.librarease.app.data.local.entity.BookDetailEntity
import org.librarease.app.data.local.entity.BookEntity
import org.librarease.app.data.local.entity.LibraryEntity

@Database(
    entities = [
        BookEntity::class,
        BookDetailEntity::class,
        LibraryEntity::class
    ],
    version = 4,
    exportSchema = false
)
abstract class LibrareaseDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
    abstract fun bookDetailDao(): BookDetailDao
    abstract fun libraryDao(): LibraryDao
}