package org.librarease.app.presentation.library_detail

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.librarease.app.domain.model.Library
import org.librarease.app.presentation.library_detail.components.LibraryDetailAppBar
import org.librarease.app.presentation.library_detail.components.LibraryInfoCard
import org.librarease.app.presentation.library_detail.components.MembershipCard

@Composable
fun LibraryDetailScreen(
    libraryId: String,
    viewModel: LibraryDetailViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val memberships by viewModel.memberships.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val library by viewModel.library.collectAsStateWithLifecycle()
    
    LaunchedEffect(libraryId) {
        viewModel.loadLibraryData(libraryId)
    }
    
    Scaffold(
        topBar = {
            LibraryDetailAppBar(
                libraryName = library?.name ?: "Library",
                navigateBack = navigateBack
            )
        }
    ) { innerPadding ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                library?.let { lib ->
                    item {
                        LibraryInfoCard(library = lib)
                    }
                } ?: run {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Loading library information...",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                    }
                }
                
                item {
                    Text(
                        text = "Available Memberships",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                if (memberships.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No membership plans available for this library",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                    }
                } else {
                    items(memberships) { membership ->
                        MembershipCard(membership = membership)
                    }
                }
            }
        }
    }
} 