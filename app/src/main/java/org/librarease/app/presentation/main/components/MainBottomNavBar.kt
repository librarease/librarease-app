package org.librarease.app.presentation.main.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.LocalLibrary
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Subscriptions
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MainBottomNavBar(
    selectedRoute: String,
    onRouteSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(modifier = modifier) {
        NavigationBarItem(
            selected = selectedRoute == "home",
            onClick = { onRouteSelected("home") },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") }
        )
        
        NavigationBarItem(
            selected = selectedRoute == "books",
            onClick = { onRouteSelected("books") },
            icon = { Icon(Icons.Default.Book, contentDescription = "Books") },
            label = { Text("Books") }
        )
        
        NavigationBarItem(
            selected = selectedRoute == "libraries",
            onClick = { onRouteSelected("libraries") },
            icon = { Icon(Icons.Default.LocalLibrary, contentDescription = "Libraries") },
            label = { Text("Libraries") }
        )
        
        NavigationBarItem(
            selected = selectedRoute == "subscriptions",
            onClick = { onRouteSelected("subscriptions") },
            icon = { Icon(Icons.Default.Subscriptions, contentDescription = "Subscriptions") },
            label = { Text("Subscriptions") }
        )
        
        NavigationBarItem(
            selected = selectedRoute == "borrowings",
            onClick = { onRouteSelected("borrowings") },
            icon = { Icon(Icons.Default.LibraryBooks, contentDescription = "Borrowings") },
            label = { Text("Borrowings") }
        )
    }
} 