package org.librarease.app.presentation.subscriptions

import org.librarease.app.domain.model.Subscription

sealed class SubscriptionsScreenState {
    object Loading: SubscriptionsScreenState()
    data class Success(val subscriptions: List<Subscription>, val isRefreshing: Boolean = false) : SubscriptionsScreenState()
    data class Error(val message: String): SubscriptionsScreenState()
}