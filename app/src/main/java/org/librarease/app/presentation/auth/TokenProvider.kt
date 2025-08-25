package org.librarease.app.presentation.auth

interface TokenProvider {
    suspend fun getToken(): String?
}