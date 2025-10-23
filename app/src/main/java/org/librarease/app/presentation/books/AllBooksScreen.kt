package org.librarease.app.presentation.books

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.librarease.app.R
import org.librarease.app.presentation.books.components.EmptyBooksContent
import org.librarease.app.presentation.books.components.ErrorContent
import org.librarease.app.presentation.main.MainViewModel
import org.librarease.app.presentation.main.components.BookCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllBooksScreen(
    viewModel: MainViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
    onBookClick: (String) -> Unit = {}
) {
    // Observe single immutable UI state (following data flow best practices)
    val uiState by viewModel.booksUiState.collectAsStateWithLifecycle()
    
    // Load initial books on first composition
    LaunchedEffect(Unit) {
        viewModel.loadInitialBooks()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = stringResource(R.string.all_books_title),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.navigate_back)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = { viewModel.updateSearchQuery(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(R.dimen.spacing_small)),
                placeholder = { 
                    Text(
                        text = stringResource(R.string.search_books_hint),
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(R.string.search_icon),
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                shape = MaterialTheme.shapes.extraLarge,
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyMedium
            )

            Box(modifier = Modifier.fillMaxSize()) {
                when {
                    // Error State
                    uiState.error != null -> {
                        ErrorContent(
                            error = uiState.error,
                            onRetry = { 
                                viewModel.clearBooksError()
                                viewModel.loadInitialBooks()
                            },
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    // Empty State
                    uiState.isEmpty -> {
                        EmptyBooksContent(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    // Success State with Books
                    else -> {
                        val gridState = rememberLazyGridState()
                        
                        // Pagination trigger
                        val shouldLoadMore = remember {
                            derivedStateOf {
                                val lastVisibleItem = gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                                val totalItems = uiState.books.size
                                lastVisibleItem >= totalItems - 5 && uiState.hasMoreBooks && !uiState.isLoadingMore
                            }
                        }
                        
                        LaunchedEffect(shouldLoadMore.value) {
                            if (shouldLoadMore.value) {
                                viewModel.loadMoreBooks()
                            }
                        }
                        
                        LazyVerticalGrid(
                            state = gridState,
                            columns = GridCells.Adaptive(minSize = 140.dp),
                            contentPadding = PaddingValues(dimensionResource(R.dimen.spacing_normal)),
                            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_normal)),
                            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_normal)),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(uiState.books.size) { index ->
                                val book = uiState.books[index]
                                BookCard(
                                    title = book.title,
                                    author = book.author,
                                    cover = book.cover,
                                    onClick = { onBookClick(book.id) }
                                )
                            }

                            if (uiState.isLoadingMore) {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(dimensionResource(R.dimen.spacing_normal)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(dimensionResource(R.dimen.icon_size_large)),
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            }
                        }
                        
                        // Initial loading indicator (centered)
                        if (uiState.isInitialLoad) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .size(dimensionResource(R.dimen.icon_size_xl)),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}
