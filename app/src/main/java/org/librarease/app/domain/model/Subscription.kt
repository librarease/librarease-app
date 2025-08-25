package org.librarease.app.domain.model

data class Subscription(
    val id: String,
    val name: String,
    val libraryId: String,
    val libraryName: String,
    val status: SubscriptionStatus,
    val subscriptionDate: String? = null,
    val expiryDate: String? = null,
    val membershipType: String? = null
)

enum class SubscriptionStatus {
    ACTIVE,
    EXPIRED,
    SUSPENDED,
    PENDING
}
