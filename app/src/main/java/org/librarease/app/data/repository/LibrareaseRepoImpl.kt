package org.librarease.app.data.repository

import org.librarease.app.domain.model.BookItem
import org.librarease.app.domain.model.Library
import org.librarease.app.domain.repository.LibrareaseRepository

class LibrareaseRepoImpl(

): LibrareaseRepository {
    override suspend fun getBooks(limit: Int): List<BookItem> {
        TODO("Not yet implemented")
    }

    override suspend fun getLibraries(limit: Int): List<Library> {
        TODO("Not yet implemented")
    }

}