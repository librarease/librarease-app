package org.librarease.app.data.remote.response

import com.google.gson.annotations.SerializedName

data class LibraryListResponse(
    @SerializedName("data")
    val libraryList: List<Library?>?
) {
    data class Library(
        @SerializedName("name")
        val name: String?,

        @SerializedName("phone")
        val phoneNo: String?,

        @SerializedName("email")
        val email: String?
    )
}