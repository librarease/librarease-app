package org.librarease.app.domain.model

data class Subscription(
    val id: String,
    val userId: String,
    val userName: String,
    val membershipId: String,
    val membershipName: String,
    val libraryId: String,
    val libraryName: String,
    val createdDate: String,
    val expireDate: String,
    val amount: Int,
    val finePerDay: Int,
    val loanPeriod: Int,
    val activeLoanLimit: Int
)


