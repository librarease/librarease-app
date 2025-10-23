package org.librarease.app.data.remote.response

data class ReturningResponse(
    val borrowing_id: String,
    val fine: Int,
    val id: String,
    val returned_at: String,
    val staff_id: String
)