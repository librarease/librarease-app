package org.librarease.app.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.librarease.app.presentation.auth.forgot_psw.ForgotPasswordScreen
import org.librarease.app.presentation.auth.forgot_psw.ForgotPasswordViewModel
import org.librarease.app.presentation.auth.sign_in.SignInScreen
import org.librarease.app.presentation.auth.sign_in.SignInViewModel
import org.librarease.app.presentation.auth.sign_up.SignUpScreen
import org.librarease.app.presentation.auth.sign_up.SignUpViewModel
import org.librarease.app.presentation.auth.verify_email.VerifyEmailScreen
import org.librarease.app.presentation.auth.verify_email.VerifyEmailViewModel
import org.librarease.app.presentation.profile.ProfileScreen
import org.librarease.app.presentation.profile.ProfileViewmodel


@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: Route = Route.Profile
) {
    NavHost(
        navController = navController,
        startDestination = startDestination.route
    ) {
        composable(Route.Profile.route) {
            val viewModel: ProfileViewmodel = hiltViewModel()
            ProfileScreen(
                viewmodel = viewModel,
                navigateAndClear = navController::navigateAndClear
            )
        }
        composable(Route.SignIn.route) {
            val viewModel: SignInViewModel = hiltViewModel()
            SignInScreen(
                viewModel = viewModel,
                navigate = { route -> navController.navigate(route.route) },
                navigateAndClear = navController::navigateAndClear
            )
        }
        composable(Route.ForgotPassword.route) {
            val viewModel: ForgotPasswordViewModel = hiltViewModel()
            ForgotPasswordScreen(
                viewModel = viewModel,
                navigateBack = navController::navigateUp
            )
        }
        composable(Route.SignUp.route) {
            val viewModel: SignUpViewModel = hiltViewModel()
            SignUpScreen(
                viewModel = viewModel,
                navigateBack = navController::navigateUp,
                navigateAndClear = navController::navigateAndClear
            )
        }
        composable(Route.VerifyEmail.route) {
            val viewModel: VerifyEmailViewModel = hiltViewModel()
            VerifyEmailScreen(
                viewModel = viewModel,
                navigateAndClear = navController::navigateAndClear
            )
        }

    }
}

fun NavHostController.navigateAndClear(route: Route) {
    val startRoute = graph.findStartDestination().route
    if (startRoute != null) {
        navigate(route.route) {
            popUpTo(startRoute) { inclusive = false }
            launchSingleTop = true
        }
    } else {
        navigate(route.route) {
            popUpTo(0) { inclusive = false }
            launchSingleTop = true
        }
    }
}

