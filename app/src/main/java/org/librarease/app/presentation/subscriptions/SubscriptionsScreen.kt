package org.librarease.app.presentation.subscriptions

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import org.librarease.app.domain.model.Subscription
import org.librarease.app.domain.model.SubscriptionStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionsScreen(
    navigateBack: () -> Unit,
    viewModel: SubscriptionViewModel = hiltViewModel()
) {
    val subscriptionsState by viewModel.subscriptions.collectAsState()
    
    // Fetch subscriptions when screen loads
    LaunchedEffect(Unit) {
        viewModel.fetchUserSubscriptions()
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Subscriptions") },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors()
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                subscriptionsState == null -> {
                    // Loading state
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                
                !subscriptionsState.isNullOrEmpty() -> {
                    // Success state with data
                    SubscriptionsContent(subscriptions = subscriptionsState!!)
                }
                
                else -> {
                    // Empty state
                    EmptySubscriptionsContent()
                }
            }
        }
    }
}

@Composable
private fun SubscriptionsContent(
    subscriptions: List<Subscription>
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Active Subscriptions (${subscriptions.size})",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        
        items(subscriptions) { subscription ->
            SubscriptionCard(subscription = subscription)
        }
    }
}

@Composable
private fun SubscriptionCard(
    subscription: Subscription
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = subscription.libraryName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    if (subscription.membershipType != null) {
                        Text(
                            text = subscription.membershipType,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                
                AssistChip(
                    onClick = { },
                    label = { Text(subscription.status.name.lowercase().replaceFirstChar { it.uppercase() }) },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = when (subscription.status) {
                            SubscriptionStatus.ACTIVE -> MaterialTheme.colorScheme.primaryContainer
                            SubscriptionStatus.EXPIRED -> MaterialTheme.colorScheme.errorContainer
                            SubscriptionStatus.SUSPENDED -> MaterialTheme.colorScheme.secondaryContainer
                            SubscriptionStatus.PENDING -> MaterialTheme.colorScheme.tertiaryContainer
                        }
                    )
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            if (subscription.subscriptionDate != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Subscribed: ",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = subscription.subscriptionDate,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            
            if (subscription.expiryDate != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Expires: ",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = subscription.expiryDate,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptySubscriptionsContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "No Subscriptions",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            
            Text(
                text = "You don't have any active library subscriptions yet.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Text(
                text = "Visit libraries to subscribe and access their collections!",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ErrorContent(
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Failed to load subscriptions",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.error
            )
            
            Text(
                text = "Please check your internet connection and try again.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Button(onClick = onRetry) {
                Text("Retry")
            }
        }
    }
}
