package org.librarease.app.presentation.subscriptions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.librarease.app.data.remote.response.SubscriptionResponse
import org.librarease.app.domain.repository.AuthRepository
import retrofit2.HttpException
import javax.inject.Inject

class SubscriptionViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {
    private val _subscriptions = MutableStateFlow<Result<List<SubscriptionResponse>>?>(null)
    val subscriptions: StateFlow<Result<List<SubscriptionResponse>>?> = _subscriptions

    fun fetchSubscriptions(userId: String) {
        try {
            viewModelScope.launch {
                _subscriptions.value = repository.getUserSubscriptions(userId)
            }
        } catch (e: HttpException) {
            e.printStackTrace()
        }
    }
}