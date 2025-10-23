package org.librarease.app.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import org.librarease.app.data.local.dao.BookDao
import org.librarease.app.data.local.dao.BookDetailDao
import org.librarease.app.data.local.entity.BookDetailEntity
import org.librarease.app.data.local.entity.BookEntity

@Database(
    entities = [
        BookEntity::class,
        BookDetailEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class LibrareaseDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
    abstract fun bookDetailDao(): BookDetailDao
}