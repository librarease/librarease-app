package org.librarease.app.presentation.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import org.librarease.app.R
import org.librarease.app.presentation.navigation.Route
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    viewModel: SplashViewModel = hiltViewModel(),
    navigateAndClear: (Route) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize().background(Color(0xFF4CAF50)),
        contentAlignment = Alignment.Center
    ) {
//        Image(
//            painter = painterResource(R.drawable.splash_screen),
//            contentDescription = null
//        )
    }
    LaunchedEffect(Unit) {
        delay(1500)
        when {
            viewModel.isUserSignOut -> navigateAndClear(Route.SignIn)
            viewModel.isEmailVerified -> navigateAndClear(Route.Main)
            else -> navigateAndClear(Route.VerifyEmail)
        }
    }
}
