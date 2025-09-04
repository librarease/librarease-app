package org.librarease.app.data.remote.response


data class SubscriptionListResponse(
    val subscriptions: List<SubscriptionResponse>?,
    val success: Boolean? = null,
    val message: String? = null
)

data class SubscriptionResponse(
    val id: String,
    val name: String,
    val libraryId: String,
    val libraryName: String,
    val status: String = "active",
    val subscriptionDate: String? = null,
    val expiryDate: String? = null,
    val membershipType: String? = null
)
