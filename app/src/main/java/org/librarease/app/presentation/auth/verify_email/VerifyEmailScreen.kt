package org.librarease.app.presentation.auth.verify_email

import androidx.activity.compose.BackHandler
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

    BackHandler {
        navigateAndClear(Route.SignIn)
    }

    Scaffold(
        topBar = {
            VerifyEmailAppBar()
        }
    ) { innerPadding ->
        VerifyEmailContent(
            innerPadding = innerPadding,
            onAlreadyVerifiedTextClick = viewModel::reloadUser, // Trigger reloading of user data
        )

        when (val reloadUserResponse = reloadUserResponse) {
            is Resource.Idle -> {
            }
            is Resource.Loading -> {
                LoadingIndicator()
            }
            is Resource.Success -> {
                LaunchedEffect(Unit) {
                    if (isEmailVerified) {
                        navigateAndClear(Route.Main)
                    } else {
                        showToastMessage(context, emailNotVerifiedMessage)
                    }
                }
            }
            is Resource.Failure -> {
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
