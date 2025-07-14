package org.librarease.app.domain.model

data class Membership(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val durationDays: Int,
    val loanPeriod: Int,
    val libraryId: String,
    val maxBooks: Int
) 