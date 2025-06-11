package org.librarease.app.presentation.auth.sign_in

import android.app.Activity
import androidx.activity.compose.BackHandler
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
import org.librarease.app.presentation.auth.sign_in.components.SignInContent
import org.librarease.app.presentation.auth.sign_in.components.SignInTopBar
import org.librarease.app.presentation.navigation.Route

@Composable
fun SignInScreen(
    viewModel: SignInViewModel = hiltViewModel(),
    navigate: (Route) -> Unit,
    navigateAndClear: (Route) -> Unit
) {
    val context = LocalContext.current
    val activity = context as Activity
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val signInResponse by viewModel.signInState.collectAsState()
    val invalidEmailMessage = stringResource(R.string.invalid_email_message)
    val invalidPasswordMessage = stringResource(R.string.invalid_password_message)

    BackHandler {
        activity.finish()
    }

    Scaffold(
        topBar = {
            SignInTopBar()
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
                SignInContent(
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
                    onSignInClick = viewModel::signInWithEmailAndPassword,
                    isLoading = signInResponse is Resource.Loading,
                    onForgotPasswordClick = {
                        navigate(Route.ForgotPassword)
                    },
                    continueAsGuestClick = {
                        navigate(Route.Main)
                    },
                    onSignUpTextClick = {
                        navigate(Route.SignUp)
                    }
                )
            }
        }


        when(val response = signInResponse) {
            is Resource.Idle -> {
            }
            is Resource.Loading -> {

            }
            is Resource.Success -> {
                LaunchedEffect(Unit) {
                    if(viewModel.isEmailVerified) {
                        navigateAndClear(Route.Main)
                    } else {
                        navigateAndClear(Route.VerifyEmail)
                    }
                }
            }
            is Resource.Failure -> {
                response.e?.message?.let { errorMessage ->
                    LaunchedEffect(errorMessage) {
                        logErrorMessage(errorMessage)
                        val userFriendlyMessage = when {
                            errorMessage.contains("password", ignoreCase = true) -> "Incorrect password. Please try again."
                            errorMessage.contains("user", ignoreCase = true) -> "User not found. Please check your email or sign up."
                            errorMessage.contains("network", ignoreCase = true) -> "Network error. Please check your connection."
                            else -> errorMessage
                        }
                        showToastMessage(context, userFriendlyMessage)
                    }
                }
            }
        }
    }
}
