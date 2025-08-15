package org.librarease.app.presentation.auth.sign_up

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import org.librarease.app.R
import org.librarease.app.common.LoadingIndicator
import org.librarease.app.core.Resource
import org.librarease.app.core.logErrorMessage
import org.librarease.app.core.showToastMessage
import org.librarease.app.presentation.auth.sign_up.components.SignUpAppBar
import org.librarease.app.presentation.auth.sign_up.components.SignUpContent
import org.librarease.app.presentation.navigation.Route

@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
    navigateAndClear: (Route) -> Unit
) {
    val context = LocalContext.current
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val fullName by viewModel.fullName.collectAsState()
    val signUpResponse by viewModel.signUpState.collectAsState()
    val emailVerificationResponse by viewModel.emailVerificationState.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val invalidEmailMessage = stringResource(R.string.invalid_email_message)
    val invalidPasswordMessage = stringResource(R.string.invalid_password_message)
    val accountCreatedMessage = stringResource(R.string.account_created_message)
    val emailVerificationSentMessage = stringResource(R.string.email_verification_sent_message)

    Scaffold(
        topBar = {
            SignUpAppBar(
                navigateBack
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .wrapContentHeight()
                    .wrapContentWidth()
                    .offset(y = (-40).dp)
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp)
            ) {
                SignUpContent(
                    innerPadding = innerPadding,
                    email = email,
                    onEmailChange = viewModel::onEmailChange,
                    onEmailInvalid = {
                        showToastMessage(context, invalidEmailMessage)
                    },
                    password = password,
                    onPasswordChange = viewModel::onPasswordChange,
                    onPasswordInvalid = {
                        showToastMessage(context, invalidPasswordMessage)
                    },
                    fullName = fullName,
                    onFullNameChange = viewModel::onFullNameChange,
                    onFullNameInvalid = {
                        showToastMessage(context, "Please enter your full name")
                    },
                    onTermsNotAccepted = {
                        showToastMessage(context, "Please accept the terms and conditions")
                    },
                    onSignUpClick = { email, password, fullName ->
                        viewModel.onSignUpWithEmailAndPassword(email, password, fullName)
                    },
                    isLoading = isLoading,
                    onSignInTextClick = navigateBack
                )
            }
        }
    }

    // Handle the Sign-Up state response
    when (val signUpResponse = signUpResponse) {
        is Resource.Idle -> {
        }
        is Resource.Loading -> {

        }
        is Resource.Success -> {
            LaunchedEffect(Unit) {
                showToastMessage(context, accountCreatedMessage)
                viewModel.sendEmailVerification()
            }
        }
        is Resource.Failure -> {
            signUpResponse.e?.message?.let { errorMessage ->
                LaunchedEffect(errorMessage) {
                    logErrorMessage(errorMessage)
                    val userFriendlyMessage = when {
                        errorMessage.contains("email", ignoreCase = true) && errorMessage.contains("use", ignoreCase = true) -> 
                            "This email is already in use. Please try signing in instead."
                        errorMessage.contains("password", ignoreCase = true) -> 
                            "Password is too weak. Please use a stronger password."
                        errorMessage.contains("network", ignoreCase = true) -> 
                            "Network error. Please check your connection."
                        else -> errorMessage
                    }
                    showToastMessage(context, userFriendlyMessage)
                }
            }
        }
    }

    when (val emailVerificationResponse = emailVerificationResponse) {
        is Resource.Idle -> {
        }
        is Resource.Loading -> {

        }
        is Resource.Success -> {
            LaunchedEffect(Unit) {
                showToastMessage(context, emailVerificationSentMessage)
                navigateAndClear(Route.Main)
            }
        }
        is Resource.Failure -> {
            emailVerificationResponse.e?.message?.let { errorMessage ->
                LaunchedEffect(errorMessage) {
                    logErrorMessage(errorMessage)
                    val userFriendlyMessage = when {
                        errorMessage.contains("network", ignoreCase = true) -> 
                            "Network error. Please check your connection."
                        else -> "Failed to send verification email. Please try again later."
                    }
                    showToastMessage(context, userFriendlyMessage)
                }
            }
        }
    }
}
