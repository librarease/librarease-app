package org.librarease.app.presentation.subscriptions

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import org.librarease.app.R
import org.librarease.app.domain.model.Subscription

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionsScreen(
    navigateBack: () -> Unit,
    viewModel: SubscriptionViewModel = hiltViewModel(),
    onSubscriptionClick: (String) -> Unit
) {
    val subscriptionsState by viewModel.subscriptionState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.my_subscriptions_title),
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.back_button)
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val state = subscriptionsState) {
                is SubscriptionsScreenState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is SubscriptionsScreenState.Success -> {
                    val subscriptions = state.subscriptions
                    if (subscriptions.isNotEmpty()) {
                        SubscriptionsContent(
                            subscriptions = subscriptions,
                            onClick = { subscription -> onSubscriptionClick(subscription.id) }
                        )
                    } else if (!state.isRefreshing) {
                        // Only show empty state if not refreshing
                        EmptySubscriptionsContent()
                    } else {
                        // Show loading while fetching first time
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }

                is SubscriptionsScreenState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_normal))
                        ) {
                            Text(
                                text = stringResource(R.string.error_title),
                                style = MaterialTheme.typography.headlineMedium
                            )

                            Text(
                                text = state.message,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Button(onClick = { viewModel.fetchSubscriptions() }) {
                                Text(stringResource(R.string.retry))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SubscriptionsContent(
    subscriptions: List<Subscription>,
    onClick: (Subscription) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.spacing_medium)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium))
    ) {
        items(subscriptions) { subscription ->
            SubscriptionCard(
                subscription = subscription,
                modifier = Modifier,
                onClick = {
                    onClick(subscription)
                }
            )
        }
    }
}

@Composable
private fun SubscriptionCard(
    modifier: Modifier,
    subscription: Subscription,
    onClick: (Subscription)-> Unit
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(R.dimen.spacing_xs)),
        modifier = modifier
            .clickable{ onClick(subscription) }
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.spacing_medium)),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.spacing_normal)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(dimensionResource(R.dimen.icon_size_xl))
                        .clip(MaterialTheme.shapes.extraLarge)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.BookmarkBorder,
                        contentDescription = stringResource(R.string.membership_icon),
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(dimensionResource(R.dimen.icon_size_medium))
                    )
                }
                Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_medium)))
                Column {
                    Text(
                        text = subscription.membershipName,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_small)))
                    Text(
                        text = subscription.libraryName,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = stringResource(R.string.go_icon),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
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
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_normal))
        ) {
            Text(
                text = stringResource(R.string.no_subscriptions_title),
                style = MaterialTheme.typography.headlineMedium
            )
            
            Text(
                text = stringResource(R.string.no_subscriptions_message),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            
            Text(
                text = stringResource(R.string.no_subscriptions_hint),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
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
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_normal))
        ) {
            Text(
                text = stringResource(R.string.failed_load_subscriptions),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.error
            )
            
            Text(
                text = stringResource(R.string.check_connection),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            
            Button(onClick = onRetry) {
                Text(stringResource(R.string.retry))
            }
        }
    }
}
