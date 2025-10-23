package org.librarease.app.data.remote.response

data class BorrowingResponse(
    val book: Any,
    val book_id: String,
    val borrowed_at: String,
    val created_at: String,
    val due_at: String,
    val id: String,
    val returning: ReturningResponse,
    val staff: Any,
    val staff_id: String,
    val subscription: Any,
    val subscription_id: String,
    val updated_at: String
)