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
    private val _subscriptions = MutableStateFlow<List<Subscription>?>(null)
    val subscriptions: StateFlow<List<Subscription>?> = _subscriptions

    fun fetchSubscriptions(userId: String) {
        viewModelScope.launch {
            try {
                _subscriptions.value = librareaseRepository.getUserSubscriptions(userId)
                Log.d("##subs", "fetchUserSubscriptions: ${_subscriptions.value}")
            } catch (e: Exception) {
                Log.d("##subs", "fetchUserSubscriptions: (empty)")
                _subscriptions.value = emptyList()
                e.printStackTrace()
            }
        }
    }

    fun fetchUserSubscriptions() {
        val currentUser = authRepository.currentUser
        if (currentUser != null) {
            fetchSubscriptions(currentUser.uid)
        } else {
            _subscriptions.value = emptyList()
        }
    }
}