package org.librarease.app.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.librarease.app.domain.model.Library
import org.librarease.app.presentation.auth.forgot_psw.ForgotPasswordScreen
import org.librarease.app.presentation.auth.forgot_psw.ForgotPasswordViewModel
import org.librarease.app.presentation.auth.sign_in.SignInScreen
import org.librarease.app.presentation.auth.sign_in.SignInViewModel
import org.librarease.app.presentation.auth.sign_up.SignUpScreen
import org.librarease.app.presentation.auth.sign_up.SignUpViewModel
import org.librarease.app.presentation.auth.verify_email.VerifyEmailScreen
import org.librarease.app.presentation.auth.verify_email.VerifyEmailViewModel
import org.librarease.app.presentation.books.AllBooksScreen
import org.librarease.app.presentation.borrowings.BorrowingsPlaceholderScreen
import org.librarease.app.presentation.libraries.LibrariesPlaceholderScreen
import org.librarease.app.presentation.library_detail.LibraryDetailScreen
import org.librarease.app.presentation.library_detail.components.LibraryListScreen
import org.librarease.app.presentation.main.MainScreen
import org.librarease.app.presentation.main.MainViewModel
import org.librarease.app.presentation.profile.ProfileScreen
import org.librarease.app.presentation.profile.ProfileViewmodel
import org.librarease.app.presentation.subscriptions.SubscriptionQRScreen
import org.librarease.app.presentation.subscriptions.SubscriptionsScreen

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
                navigateAndClear = navController::navigateAndClear,
                navigate = { route -> navController.navigate(route.route) }
            )
        }
        composable(Route.SignIn.route) {
            SignInScreen(
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
            SignUpScreen(
                navigateBack = navController::navigateUp,
                navigateAndClear = navController::navigateAndClear
            )
        }

        composable(Route.Books.route) {
            val viewModel: MainViewModel = hiltViewModel()
            AllBooksScreen(
                viewModel = viewModel,
                navigateBack = navController::navigateUp
            )
        }

        composable(Route.Libraries.route) {
            val viewModel: MainViewModel = hiltViewModel()
            val libraryList by viewModel.libraryList.collectAsState()
            LibraryListScreen(
                libraryList = libraryList,
                onLibraryClick = { library ->
                    navController.navigate("library_detail/${library.id}")
                },
                navigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Route.Subscriptions.route) {
            SubscriptionsScreen(
                navigateBack = navController::navigateUp,
                onSubscriptionClick = { subscriptionId ->
                    navController.navigate(Route.SubscriptionQR.createRoute(subscriptionId))
                }
            )
        }

        composable(
            route = Route.SubscriptionQR.route,
            arguments = listOf(navArgument("subscriptionId") { type = NavType.StringType })
        ) { backStackEntry ->
            val subscriptionId = backStackEntry.arguments?.getString("subscriptionId") ?: ""
            SubscriptionQRScreen(
                innerPadding = PaddingValues(0.dp),
                subscriptionId = subscriptionId
            )
        }

        composable(
            route = "library_detail/{libraryId}",
            arguments = listOf(navArgument("libraryId") { type = NavType.StringType })
        ) { backStackEntry ->
            val libraryId = backStackEntry.arguments?.getString("libraryId") ?: ""
            LibraryDetailScreen(
                libraryId = libraryId,
                navigateBack = navController::navigateUp
            )
        }

        composable(Route.Borrowings.route) {
            BorrowingsPlaceholderScreen(
                navigateBack = navController::navigateUp
            )
        }
    }
}

fun NavHostController.navigateAndClear(route: Route) {
    val startRoute = graph.findStartDestination().route
    navigate(route.route) {
        startRoute?.let { popUpTo(it) { inclusive = false } } ?: popUpTo(0) { inclusive = false }
        launchSingleTop = true
        restoreState = true
    }
}

