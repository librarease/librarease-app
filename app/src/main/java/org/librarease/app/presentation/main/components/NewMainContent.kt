package org.librarease.app.presentation.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.librarease.app.R
import org.librarease.app.domain.model.CategoryItem
import java.util.Locale

@Composable
fun NewMainContent(
    innerPadding: PaddingValues,
    isUserSignIn: Boolean,
    userName: String?,
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit,
    categoryList: List<CategoryItem>,
    onCategoryClick: (CategoryItem) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(dimensionResource(R.dimen.spacing_normal)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Personalized Welcome Header
        WelcomeHeader(
            isUserSignIn = isUserSignIn,
            userName = userName
        )
        
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_xl)))
        
        // Category Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_normal)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_normal))
        ) {
            items(categoryList) { category ->
                CategoryCard(
                    category = category,
                    onClick = { onCategoryClick(category) }
                )
            }
        }
        
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_xl)))
        
        // Sign in prompt if not signed in
        if (!isUserSignIn) {
            SignInCardView(
                onLoginClick = onLoginClick,
                onSignUpClick = onSignUpClick
            )
        }
    }
}

@Composable
fun CategoryCard(
    category: CategoryItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = CardDefaults.outlinedCardBorder(),
        shape = MaterialTheme.shapes.medium,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(R.dimen.spacing_normal)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(MaterialTheme.shapes.extraLarge)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ){
                Icon(
                    imageVector = category.icon,
                    contentDescription = category.title,
                    modifier = Modifier.size(dimensionResource(R.dimen.icon_size_large)),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_small)))
            
            Text(
                text = category.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun WelcomeHeader(
    isUserSignIn: Boolean,
    userName: String?
) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primaryContainer,
                            MaterialTheme.colorScheme.secondaryContainer
                        )
                    ),
                    shape = MaterialTheme.shapes.medium
                )
                .clip(MaterialTheme.shapes.medium)
                .padding(dimensionResource(R.dimen.spacing_large))
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        
                        Text(
                            text = if (isUserSignIn && !userName.isNullOrBlank()) {
                                stringResource(
                                    R.string.welcome_back_user,
                                    userName.replaceFirstChar { 
                                        if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() 
                                    }
                                )
                            } else if (isUserSignIn) {
                                stringResource(R.string.hello)
                            } else {
                                stringResource(R.string.welcome_to_librarease)
                            },
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }

                    Text(
                        text = if (isUserSignIn) {
                            stringResource(R.string.explore_today)
                        } else {
                            stringResource(R.string.digital_library_companion_subtitle)
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                    )
                }
            }
        }
}