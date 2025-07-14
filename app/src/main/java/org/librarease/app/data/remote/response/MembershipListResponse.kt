package org.librarease.app.data.remote.response

import com.google.gson.annotations.SerializedName

data class MembershipListResponse(
    @SerializedName("data")
    val membershipList: List<Membership?>?
) {
    data class Membership(
        @SerializedName("id")
        val id: String?,

        @SerializedName("name")
        val name: String?,

        @SerializedName("description")
        val description: String?,

        @SerializedName("price")
        val price: Double?,

        @SerializedName("duration")
        val durationDays: Int?,

        @SerializedName("loan_period")
        val loadPeriod: Int?,

        @SerializedName("library_id")
        val libraryId: String?,

        @SerializedName("usage_limit")
        val maxBooks: Int?,
    )
} 