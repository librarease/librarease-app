package org.librarease.app.domain.model

data class BookItem(
    val id: String,
    val title: String,
    val author: String,
    val cover: String,
    val code: String,
    val year: Int,
    val libraryId: String,
    val libraryName: String?
)
