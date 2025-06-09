package org.librarease.app.core

sealed class Resource<out T> {
    data object Idle : Resource<Nothing>()

    data object Loading : Resource<Nothing>()

    data class Success<out T>(
        val data: T?
    ) : Resource<T>()

    data class Failure(
        val e: Exception?
    ) : Resource<Nothing>()
}