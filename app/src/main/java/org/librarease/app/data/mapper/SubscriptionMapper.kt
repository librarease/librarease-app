package org.librarease.app.data.mapper

import org.librarease.app.data.remote.response.SubscriptionResponse
import org.librarease.app.domain.model.Subscription
import org.librarease.app.domain.model.SubscriptionStatus

fun SubscriptionResponse.toDomain(): Subscription {
    return Subscription(
        id = id,
        name = name,
        libraryId = libraryId,
        libraryName = libraryName,
        status = when (status.lowercase()) {
            "active" -> SubscriptionStatus.ACTIVE
            "expired" -> SubscriptionStatus.EXPIRED
            "suspended" -> SubscriptionStatus.SUSPENDED
            "pending" -> SubscriptionStatus.PENDING
            else -> SubscriptionStatus.ACTIVE
        },
        subscriptionDate = subscriptionDate,
        expiryDate = expiryDate,
        membershipType = membershipType
    )
}

fun List<SubscriptionResponse>.toDomain(): List<Subscription> {
    return map { it.toDomain() }
}
