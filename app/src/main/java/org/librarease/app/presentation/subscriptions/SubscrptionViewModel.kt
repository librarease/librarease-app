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

@HiltViewModel
class SubscriptionViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val librareaseRepository: LibrareaseRepository
) : ViewModel() {
    private val _subscriptionsState = MutableStateFlow<SubscriptionsScreenState>(
        SubscriptionsScreenState.Loading)
    val subscriptionState: StateFlow<SubscriptionsScreenState> = _subscriptionsState


    init {
        fetchUserSubscriptions()
    }

    fun fetchSubscriptions() {
        viewModelScope.launch {
            _subscriptionsState.value = SubscriptionsScreenState.Loading
            try {
                val subscriptions = librareaseRepository.getUserSubscriptions()
                _subscriptionsState.value = SubscriptionsScreenState.Success(subscriptions)
                Log.d("##subs", "fetchUserSubscriptions: $subscriptions")
            } catch (e: Exception) {
                _subscriptionsState.value = SubscriptionsScreenState.Error(e.message ?: "Unknown error")
                Log.e("##subs", "Error fetching subscriptions", e)
            }
        }
    }

    fun fetchUserSubscriptions() {
        val currentUser = authRepository.currentUser
        if (currentUser != null) {
            fetchSubscriptions()
        } else {
            _subscriptionsState.value = SubscriptionsScreenState.Error("User not authenticated")
        }
    }
}