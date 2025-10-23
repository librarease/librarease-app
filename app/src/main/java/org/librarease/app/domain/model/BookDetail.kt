package org.librarease.app.domain.model

data class BookDetail(
    val id: String,
    val title: String,
    val author: String,
    val cover: String?,
    val code: String,
    val year: Int,
    val library: Library,
    val borrowCount: Int?,
    val currentBorrowing: BorrowingInfo?
)

data class BorrowingInfo(
    val id: String,
    val borrowedAt: String,
    val dueAt: String,
    val isReturned: Boolean,
    val returnedAt: String?,
    val fine: Int?
)
