package org.librarease.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class BookEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val author: String,
    val cover: String,
    val code: String,
    val year: Int,
    val libraryId: String,
    val libraryName: String?,
    val cachedAt: Long = System.currentTimeMillis()
)
