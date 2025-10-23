package org.librarease.app.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.librarease.app.data.database.LibrareaseDatabase
import org.librarease.app.data.local.dao.BookDao
import org.librarease.app.data.local.dao.BookDetailDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideLibrareaseDatabase(
        @ApplicationContext context: Context
    ): LibrareaseDatabase {
        return Room.databaseBuilder(
            context,
            LibrareaseDatabase::class.java,
            "librarease_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideBookDao(database: LibrareaseDatabase): BookDao {
        return database.bookDao()
    }

    @Provides
    @Singleton
    fun provideBookDetailDao(database: LibrareaseDatabase): BookDetailDao {
        return database.bookDetailDao()
    }
}
