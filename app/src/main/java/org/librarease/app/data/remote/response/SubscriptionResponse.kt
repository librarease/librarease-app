package org.librarease.app.data.remote.response

import com.google.gson.annotations.SerializedName


data class SubscriptionListResponse(
    @SerializedName("data")
    val subscriptions: List<SubscriptionResponse>?,
    val meta: MetaResponse?
)

data class SubscriptionResponse(
    val id: String?,
    @SerializedName("user_id")
    val userId: String?,
    @SerializedName("membership_id")
    val membershipId: String?,
    @SerializedName("created_at")
    val createdDate: String?,
    val user: UserResponse?,
    val membership: MembershipResponse?,
    @SerializedName("expires_at")
    val expireDate: String?,
    val amount: Int?,
    @SerializedName("fine_per_day")
    val finePerDay: Int?,
    @SerializedName("loan_period")
    val loanPeriod: Int?,
    @SerializedName("active_loan_limit")
    val activeLoanLimit: Int?
)

data class MembershipResponse(
    val id: String?,
    val name: String?,
    @SerializedName("library_id")
    val libraryId: String?,
    val library: LibraryResponse?
)

data class LibraryResponse(
    val id: String?,
    val name: String?
)
data class UserResponse(
    val id: String?,
    val name: String?
)
data class MetaResponse(
    val total: Int?,
    val skip: Int?,
    val limit: Int?
)
