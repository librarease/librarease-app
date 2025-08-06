package org.librarease.app.presentation.main

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.LocalLibrary
import androidx.compose.material.icons.filled.Subscriptions
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.librarease.app.domain.model.CategoryItem
import org.librarease.app.presentation.main.components.MainAppBar
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
           Icons.Default.Book,
           "Books"
       ),
       CategoryItem(
           Icons.Filled.LocalLibrary,
           "Library"
       ),
       CategoryItem(
           Icons.Default.Subscriptions,
           "Subscriptions"
       ),
       CategoryItem(
           Icons.Filled.LibraryBooks,
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
                onCategoryClick = { navigateAndClear()},
            )
//            "home" -> MainContent(
//                innerPadding = innerPadding,
//                isUserSignIn = isUserSignIn,
//                onLoginClick = { navigateAndClear(Route.SignIn) },
//                onSignUpClick = { navigateAndClear(Route.SignUp) },
//                bookList = books,
//                libraryList = libraries,
//                searchQuery = searchQuery,
//                onSearchQueryChange = viewModel::updateSearchQuery,
//                onSeeAllBooksClick = {
//                    viewModel.resetPagination()
//                    navigate(Route.AllBooks)
//                },
//                onLibraryClick = { library ->
//                    try {
//                        println("Library clicked: ${library.name} with ID: ${library.id}")
//                        val route = Route.LibraryDetailWithId(library.id)
//                        navigate(route)
//                    } catch (e: Exception) {
//                        println("Error navigating to library detail: ${e.message}")
//                        e.printStackTrace()
//                    }
//                }
//            )
            "profile" -> {
                ProfileScreen()
            }
            "settings" -> {}
        }
    }
}
