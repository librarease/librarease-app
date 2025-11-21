package org.librarease.app.presentation.subscriptions

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.librarease.app.domain.model.Subscription
import org.librarease.app.domain.repository.AuthRepository
import org.librarease.app.domain.repository.LibrareaseRepository
import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update

@HiltViewModel
class SubscriptionViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val librareaseRepository: LibrareaseRepository
) : ViewModel() {
    private val _subscriptionsState = MutableStateFlow<SubscriptionsScreenState>(
        SubscriptionsScreenState.Success(emptyList()))
    val subscriptionState: StateFlow<SubscriptionsScreenState> = _subscriptionsState
    
    private var cachedSubscriptions: List<Subscription> = emptyList()

    init {
        fetchUserSubscriptions(showLoading = true)
    }

    fun fetchSubscriptions(showLoading: Boolean = false) {
        viewModelScope.launch {
            // Show cached data immediately if available, or loading state if not
            if (showLoading && cachedSubscriptions.isEmpty()) {
                _subscriptionsState.value = SubscriptionsScreenState.Loading
            } else if (cachedSubscriptions.isNotEmpty()) {
                _subscriptionsState.value = SubscriptionsScreenState.Success(cachedSubscriptions, isRefreshing = true)
            }
            
            try {
                val subscriptions = librareaseRepository.getUserSubscriptions()
                cachedSubscriptions = subscriptions
                _subscriptionsState.value = SubscriptionsScreenState.Success(subscriptions, isRefreshing = false)
                Log.d("##subs", "fetchUserSubscriptions: $subscriptions")
            } catch (e: Exception) {
                // If we have cached data, keep showing it with error toast instead of error screen
                if (cachedSubscriptions.isNotEmpty()) {
                    _subscriptionsState.value = SubscriptionsScreenState.Success(cachedSubscriptions, isRefreshing = false)
                } else {
                    _subscriptionsState.value = SubscriptionsScreenState.Error(e.message ?: "Unknown error")
                }
                Log.e("##subs", "Error fetching subscriptions", e)
            }
        }
    }

    fun fetchUserSubscriptions(showLoading: Boolean = false) {
        val currentUser = authRepository.currentUser
        if (currentUser != null) {
            fetchSubscriptions(showLoading)
        } else {
            _subscriptionsState.value = SubscriptionsScreenState.Error("User not authenticated")
        }
    }
}