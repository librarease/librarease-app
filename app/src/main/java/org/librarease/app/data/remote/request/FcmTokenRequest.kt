package org.librarease.app.data.remote.request

data class FcmTokenRequest(
    val token: String,
    val provider: String = "fcm"
)
