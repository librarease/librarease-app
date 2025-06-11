package org.librarease.app.presentation.main

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import org.librarease.app.R
import org.librarease.app.common.LoadingIndicator
import org.librarease.app.core.Resource
import org.librarease.app.core.showToastMessage
import org.librarease.app.presentation.main.components.MainAppBar
import org.librarease.app.presentation.main.components.MainContent
import org.librarease.app.presentation.navigation.Route

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
    navigateAndClear: (Route) -> Unit
) {
    val context = LocalContext.current
    val activity = context as Activity
    val isUserSignIn by viewModel.authState.collectAsState()

    BackHandler {
        activity.finish()
    }
    
    Scaffold(
        topBar = {
            MainAppBar(
                signOut = viewModel::signOut,
                isUserSignIn = isUserSignIn,
                onLoginClick = {
                    navigateAndClear(Route.SignIn)
                },
                onSignUpClick = {
                    navigateAndClear(Route.SignUp)
                },
            )
        }
    ) { innerPadding ->
        MainContent(
            innerPadding = innerPadding,
            isUserSignIn = isUserSignIn,
            onLoginClick = {
                navigateAndClear(Route.SignIn)
            },
            onSignUpClick = {
                navigateAndClear(Route.SignUp)
            }
        )
    }
}
