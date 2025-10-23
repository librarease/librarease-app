package org.librarease.app.data.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "book_details")
data class BookDetailEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val author: String,
    val cover: String?,
    val code: String,
    val year: Int,
    val libraryId: String,
    val libraryName: String?,
    val libraryPhoneNo: String?,
    val libraryEmail: String?,
    val libraryLogo: String?,
    val borrowCount: Int?,
    val currentBorrowingId: String?,
    val currentBorrowingBorrowedAt: String?,
    val currentBorrowingDueAt: String?,
    val currentBorrowingReturnedAt: String?,
    val currentBorrowingIsReturned: Boolean?,
    val currentBorrowingFine: Int?,
    val cachedAt: Long = System.currentTimeMillis()
)
