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
import org.librarease.app.presentation.main.MainScreen
import org.librarease.app.presentation.main.MainViewModel
import org.librarease.app.presentation.profile.ProfileScreen
import org.librarease.app.presentation.profile.ProfileViewmodel


@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: Route = Route.Main
) {
    NavHost(
        navController = navController,
        startDestination = startDestination.route
    ) {
        composable(Route.Main.route) {
            val viewModel: MainViewModel = hiltViewModel()
            MainScreen (
                viewModel = viewModel,
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
    navigate(route.route) {
        // Pop up to the start destination to avoid building up a large stack of destinations
        startRoute?.let { popUpTo(it) { inclusive = false } } ?: popUpTo(0) { inclusive = false }
        // Single top ensures we don't create multiple copies of the same destination
        launchSingleTop = true
        // Restore state ensures the state of the destination is restored when navigating back to it
        restoreState = true
    }
}

