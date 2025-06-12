package org.librarease.app.data.remote.response

import com.google.gson.annotations.SerializedName

data class BookListResponse(
    @SerializedName("data")
    val bookList: List<Book?>?
) {
    data class Book(
        @SerializedName("title")
        val title: String?,

        @SerializedName("author")
        val author: String?,

        @SerializedName("cover")
        val cover: String?,
    )
}
