package org.librarease.app.presentation.auth.verify_email

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import org.librarease.app.R
import org.librarease.app.common.LoadingIndicator
import org.librarease.app.core.Resource
import org.librarease.app.core.logErrorMessage
import org.librarease.app.core.showToastMessage
import org.librarease.app.presentation.auth.verify_email.components.VerifyEmailAppBar
import org.librarease.app.presentation.auth.verify_email.components.VerifyEmailContent
import org.librarease.app.presentation.navigation.Route

@Composable
fun VerifyEmailScreen(
    viewModel: VerifyEmailViewModel = hiltViewModel(),
    navigateAndClear: (Route) -> Unit
) {
    val context = LocalContext.current
    val reloadUserResponse by viewModel.reloadUserState.collectAsState()
    val isEmailVerified by viewModel.isEmailVerifiedState.collectAsState()
    val emailNotVerifiedMessage = stringResource(R.string.email_not_verified_message)

    // Scaffold to hold the top bar and content
    Scaffold(
        topBar = {
            VerifyEmailAppBar()
        }
    ) { innerPadding ->
        // Content of the screen with a callback to reload the user on click
        VerifyEmailContent(
            innerPadding = innerPadding,
            onAlreadyVerifiedTextClick = viewModel::reloadUser, // Trigger reloading of user data
        )

        // Handle different states of reload user response
        when (val reloadUserResponse = reloadUserResponse) {
            is Resource.Idle -> {
                // No action needed when idle
            }
            is Resource.Loading -> {
                // Show loading indicator while reloading user data
                LoadingIndicator()
            }
            is Resource.Success -> {
                LaunchedEffect(Unit) {
                    // Navigate to Profile screen if email is verified, otherwise show error message
                    if (isEmailVerified) {
                        navigateAndClear(Route.Profile)
                    } else {
                        showToastMessage(context, emailNotVerifiedMessage)
                    }
                }
            }
            is Resource.Failure -> {
                // Show error message if reload user fails
                reloadUserResponse.e?.message?.let { errorMessage ->
                    LaunchedEffect(errorMessage) {
                        logErrorMessage(errorMessage)
                        showToastMessage(context, errorMessage)
                    }
                }
            }
        }
    }
}
