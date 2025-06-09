package org.librarease.app.presentation.auth.forgot_psw

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import org.librarease.app.R
import org.librarease.app.common.LoadingIndicator
import androidx.hilt.navigation.compose.hiltViewModel
import org.librarease.app.core.Resource
import org.librarease.app.core.logErrorMessage
import org.librarease.app.core.showToastMessage
import org.librarease.app.presentation.auth.forgot_psw.components.ForgotPasswordAppBar
import org.librarease.app.presentation.auth.forgot_psw.components.ForgotPasswordContent

@Composable
fun ForgotPasswordScreen(
    viewModel: ForgotPasswordViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val context = LocalContext.current
    val email by viewModel.email.collectAsState()
    val passwordResetEmailResponse by viewModel.passwordResetEmailState.collectAsState()
    val invalidEmailMessage = stringResource(id = R.string.invalid_email_message)
    val resetPasswordMessage = stringResource(id = R.string.reset_password_message)

    Scaffold(
        topBar = {
            ForgotPasswordAppBar(
                onArrowBackIconClick = navigateBack
            )
        }
    ) { innerPadding ->
        ForgotPasswordContent(
            innerPadding = innerPadding,
            email = email,
            onEmailChange = viewModel::onEmailChange,
            onEmailInvalid = {
                showToastMessage(context, invalidEmailMessage)
            },
            onSendPasswordResetEmail = viewModel::onSendPasswordResetEmail,
            isLoading = passwordResetEmailResponse is Resource.Loading,
        )
    }

    when(val sendResetEmailResponse = passwordResetEmailResponse) {
        is Resource.Idle -> {
            // Do nothing for idle state
        }
        is Resource.Loading -> {
            // Show loading indicator
            LoadingIndicator()
        }
        is Resource.Success -> {
            // Show toast and navigate back on success
            LaunchedEffect(Unit) {
                showToastMessage(context, resetPasswordMessage)
                navigateBack()
            }
        }
        is Resource.Failure -> {
            sendResetEmailResponse.e?.message?.let { errorMessage ->
                // Handle error with a toast message
                LaunchedEffect(errorMessage) {
                    logErrorMessage(errorMessage)
                    showToastMessage(context, errorMessage)
                }
            }
        }
    }
}
