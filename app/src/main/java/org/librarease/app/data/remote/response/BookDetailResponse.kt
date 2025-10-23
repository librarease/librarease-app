package org.librarease.app.data.remote.response

data class BookDetailResponse(
    val data: Data
)

data class Data(
    val author: String?,
    val code: String?,
    val cover: String?,
    val created_at: String?,
    val id: String?,
    val library: LibraryListResponse.Library,
    val library_id: String?,
    val stats: Stats?,
    val title: String?,
    val updated_at: String?,
    val year: Int?
)

data class Stats(
    val borrow_count: Int?,
    val borrowing: BorrowingResponse?
)