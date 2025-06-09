package org.librarease.app.presentation.splash

import androidx.lifecycle.ViewModel
import org.librarease.app.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class SplashViewModel @Inject constructor(
    private val repo: AuthRepository
): ViewModel(){

    val isUserSignOut get() = repo.currentUser == null

    val isEmailVerified get() = repo.currentUser?.isEmailVerified == true
}