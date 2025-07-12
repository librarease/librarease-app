package org.librarease.app.data.mapper

import org.librarease.app.data.remote.response.BookListResponse
import org.librarease.app.data.remote.response.LibraryListResponse
import org.librarease.app.domain.model.BookItem
import org.librarease.app.domain.model.Library

object ResponseMapper {

    val bookListMapper: (BookListResponse) -> List<BookItem> = { response ->
        val responseData = response.bookList
        val bookList = ArrayList<BookItem>()
        
        responseData?.forEach { book ->
            book?.let {
                if(it.title != null) {
                    bookList.add(
                        BookItem(
                            title = it.title,
                            author = it.author ?: "",
                            cover = it.cover ?: ""
                        )
                    )
                }
            }
        }
        bookList
    }
    
    val libraryListMapper: (LibraryListResponse) -> List<Library> = { response ->
        val responseData = response.libraryList
        val libraryList = ArrayList<Library>()
        
        responseData?.forEach { library ->
            library?.let {
                if (it.name != null) {
                    libraryList.add(
                        Library(
                            name = it.name,
                            phoneNo = it.phoneNo,
                            email = it.email
                        )
                    )
                }
            }
        }
        
        libraryList
    }
}