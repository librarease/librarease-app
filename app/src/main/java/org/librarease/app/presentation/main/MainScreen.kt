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
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import org.librarease.app.R
import org.librarease.app.domain.model.CategoryItem
import org.librarease.app.presentation.main.components.MainAppBar
import org.librarease.app.presentation.main.components.NewMainContent
import org.librarease.app.presentation.navigation.Route

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
    navigateAndClear: (Route) -> Unit,
    navigate: (Route) -> Unit = {}
) {
    val context = LocalContext.current
    val activity = context as Activity
    val isUserSignIn by viewModel.authState.collectAsState()
    val userName by viewModel.userName.collectAsState()

    val categoryList = listOf(
        CategoryItem(
            Icons.Default.MenuBook,
            stringResource(R.string.books_category)
        ),
        CategoryItem(
            Icons.Filled.LocationOn,
            stringResource(R.string.libraries_category)
        ),
        CategoryItem(
            Icons.Default.Subscriptions,
            stringResource(R.string.subscriptions_category)
        ),
        CategoryItem(
            Icons.Filled.Cached,
            stringResource(R.string.borrowings_category)
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
                userName = userName,
                onLoginClick = { navigateAndClear(Route.SignIn) },
                onSignUpClick = { navigateAndClear(Route.SignUp) },
                categoryList = categoryList,
                onCategoryClick = { category ->
                    when (category.title) {
                        context.getString(R.string.books_category) -> navigate(Route.Books)
                        context.getString(R.string.libraries_category) -> navigate(Route.Libraries)
                        context.getString(R.string.subscriptions_category) -> navigate(Route.Subscriptions)
                        context.getString(R.string.borrowings_category) -> navigate(Route.Borrowings)
                    }
                }
            )
            "books" -> {
                LaunchedEffect(Unit) {
                    navigate(Route.Books)
                }
            }
            "libraries" -> {
                LaunchedEffect(Unit) {
                    navigate(Route.Libraries)
                }
            }
            "subscriptions" -> {
                LaunchedEffect(Unit) {
                    navigate(Route.Subscriptions)
                }
            }
            "borrowings" -> {
                LaunchedEffect(Unit) {
                    navigate(Route.Borrowings)
                }
            }
            "settings" -> {}
        }
    }
}
