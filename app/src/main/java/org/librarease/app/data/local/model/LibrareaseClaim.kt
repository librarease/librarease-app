package org.librarease.app.data.local.model

import com.google.gson.annotations.SerializedName

data class LibrareaseClaim(
    @SerializedName("admin_libs")
    val adminLibs: List<String>?,
    val id: String?,
    @SerializedName("staff_libs")
    val staffLibs: List<String>?,
    val role: String?
)
