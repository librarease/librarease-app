package org.librarease.app.data.remote.response

import com.google.gson.annotations.SerializedName

data class BookListResponse(
    @SerializedName("data")
    val bookList: List<Data?>?
)
