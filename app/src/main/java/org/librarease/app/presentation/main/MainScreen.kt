package org.librarease.app.presentation.main

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Cached
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.LocalLibrary
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Subscriptions
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.librarease.app.domain.model.CategoryItem
import org.librarease.app.presentation.main.components.MainAppBar
import org.librarease.app.presentation.main.components.MainBottomNavBar
import org.librarease.app.presentation.main.components.MainContent
import org.librarease.app.presentation.main.components.NewMainContent
import org.librarease.app.presentation.navigation.Route
import org.librarease.app.presentation.profile.ProfileScreen

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
    navigateAndClear: (Route) -> Unit,
    navigate: (Route) -> Unit = {}
) {
    val context = LocalContext.current
    val activity = context as Activity
    val isUserSignIn by viewModel.authState.collectAsState()

    val categoryList = listOf(
        CategoryItem(
            Icons.Default.MenuBook,
            "Books"
        ),
        CategoryItem(
            Icons.Filled.LocationOn,
            "Libraries"
        ),
        CategoryItem(
            Icons.Default.Subscriptions,
            "Subscriptions"
        ),
        CategoryItem(
            Icons.Filled.Cached,
            "Borrowings"
        )
    )

    val (selectedRoute, setSelectedRoute) = rememberSaveable { mutableStateOf("home") }

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
            )
        },
        bottomBar = {
            MainBottomNavBar(selectedRoute = selectedRoute) { newRoute ->
                setSelectedRoute(newRoute)
            }
        }
    ) { innerPadding ->
        when(selectedRoute) {
            "home" -> NewMainContent(
                innerPadding = innerPadding,
                isUserSignIn = isUserSignIn,
                onLoginClick = { navigateAndClear(Route.SignIn) },
                onSignUpClick = { navigateAndClear(Route.SignUp) },
                categoryList = categoryList,
                onCategoryClick = { category ->
                    when (category.title) {
                        "Books" -> navigate(Route.Books)
                        "Libraries" -> navigate(Route.Libraries)
                        "Subscriptions" -> navigate(Route.Subscriptions)
                        "Borrowings" -> navigate(Route.Borrowings)
                    }
                }
            )
            "books" -> {
                LaunchedEffect(Unit) {
                    navigate(Route.Books)
                }
            }
            "libraries" -> {
                // Navigate to Libraries screen
                LaunchedEffect(Unit) {
                    navigate(Route.Libraries)
                }
            }
            "subscriptions" -> {
                // Navigate to Subscriptions screen
                LaunchedEffect(Unit) {
                    navigate(Route.Subscriptions)
                }
            }
            "borrowings" -> {
                // Navigate to Borrowings screen
                LaunchedEffect(Unit) {
                    navigate(Route.Borrowings)
                }
            }
            "profile" -> {
                ProfileScreen()
            }
            "settings" -> {}
        }
    }
}
