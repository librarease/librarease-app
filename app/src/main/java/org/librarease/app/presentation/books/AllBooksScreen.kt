package org.librarease.app.presentation.books

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.collectAsState
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
import org.librarease.app.presentation.main.MainViewModel
import org.librarease.app.presentation.main.components.BookCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllBooksScreen(
    viewModel: MainViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
    onBookClick: (String) -> Unit = {}
) {
    val books by viewModel.filteredAllBooksList.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val hasMoreBooks by viewModel.hasMoreBooks.collectAsState()
    
    LaunchedEffect(key1 = Unit) {
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
                value = searchQuery,
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
                val gridState = rememberLazyGridState()
                
                val shouldLoadMore = remember {
                    derivedStateOf {
                        val lastVisibleItem = gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                        val totalItems = books.size
                        lastVisibleItem >= totalItems - 5 && hasMoreBooks && !isLoading
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
                    items(books.size) { index ->
                        val book = books[index]
                        BookCard(
                            title = book.title,
                            author = book.author,
                            cover = book.cover,
                            onClick = { onBookClick(book.id) }
                        )
                    }
                    
                    if (isLoading) {
                        item {
                            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_small)))
                        }
                    }
                }
                
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(dimensionResource(R.dimen.spacing_normal))
                            .size(dimensionResource(R.dimen.icon_size_xl)),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}
