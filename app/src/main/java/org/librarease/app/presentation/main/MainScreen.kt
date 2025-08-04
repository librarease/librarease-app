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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.librarease.app.R
import org.librarease.app.common.LoadingIndicator
import org.librarease.app.core.Resource
import org.librarease.app.core.showToastMessage
import org.librarease.app.presentation.main.components.MainAppBar
import org.librarease.app.presentation.main.components.MainContent
import org.librarease.app.presentation.navigation.Route
import org.librarease.app.domain.model.Library
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

    val books = viewModel.filteredBooksList.collectAsStateWithLifecycle().value
    val libraries = viewModel.libraryList.collectAsStateWithLifecycle().value
    val searchQuery by viewModel.searchQuery.collectAsState()

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
                onSignUpClick = {
                    navigateAndClear(Route.SignUp)
                },
            )
        },
        bottomBar = {
            MainBottomNavBar(selectedRoute = selectedRoute) { newRoute ->
                setSelectedRoute(newRoute)
                // Optionally, call navigate(Route.YourRoute) if you want to trigger navigation
            }
        }
    ) { innerPadding ->
        when(selectedRoute) {
            "home" -> MainContent(
                innerPadding = innerPadding,
                isUserSignIn = isUserSignIn,
                onLoginClick = { navigateAndClear(Route.SignIn) },
                onSignUpClick = { navigateAndClear(Route.SignUp) },
                bookList = books,
                libraryList = libraries,
                searchQuery = searchQuery,
                onSearchQueryChange = viewModel::updateSearchQuery,
                onSeeAllBooksClick = {
                    viewModel.resetPagination()
                    navigate(Route.AllBooks)
                },
                onLibraryClick = { library ->
                    try {
                        println("Library clicked: ${library.name} with ID: ${library.id}")
                        val route = Route.LibraryDetailWithId(library.id)
                        navigate(route)
                    } catch (e: Exception) {
                        println("Error navigating to library detail: ${e.message}")
                        e.printStackTrace()
                    }
                }
            )
            "profile" -> {
                ProfileScreen()
            }
            "settings" -> {}
        }
    }
}
