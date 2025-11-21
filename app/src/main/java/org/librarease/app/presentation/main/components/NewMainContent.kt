package org.librarease.app.presentation.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
    val scrollState = rememberScrollState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .verticalScroll(scrollState)
            .padding(dimensionResource(R.dimen.spacing_normal)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        WelcomeHeader(
            isUserSignIn = isUserSignIn,
            userName = userName
        )
        
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_large)))
        
        AutoScrollingCarousel(
            books = listOf(
                CarouselBook(
                    id = "1",
                    title = "The Great Gatsby",
                    author = "F. Scott Fitzgerald",
                    cover = "https://covers.openlibrary.org/b/id/7222246-L.jpg"
                ),
                CarouselBook(
                    id = "2",
                    title = "To Kill a Mockingbird",
                    author = "Harper Lee",
                    cover = "https://covers.openlibrary.org/b/id/8228691-L.jpg"
                ),
                CarouselBook(
                    id = "3",
                    title = "1984",
                    author = "George Orwell",
                    cover = "https://covers.openlibrary.org/b/id/7222246-L.jpg"
                ),
                CarouselBook(
                    id = "4",
                    title = "Pride and Prejudice",
                    author = "Jane Austen",
                    cover = "https://covers.openlibrary.org/b/id/8235657-L.jpg"
                ),
                CarouselBook(
                    id = "5",
                    title = "The Catcher in the Rye",
                    author = "J.D. Salinger",
                    cover = "https://covers.openlibrary.org/b/id/8228691-L.jpg"
                )
            ),
            onBookClick = { bookId ->
                
            }
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_xl)))

        Divider(modifier = Modifier.height(1.dp))

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_xl)))

        Text(
            text = "Explore",
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(R.dimen.spacing_normal))
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_normal)))

        // Category grid - using regular Column/Row instead of LazyVerticalGrid
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_normal))
        ) {
            categoryList.chunked(2).forEach { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_normal))
                ) {
                    rowItems.forEach { category ->
                        Box(modifier = Modifier.weight(1f)) {
                            CategoryCard(
                                category = category,
                                onClick = { onCategoryClick(category) }
                            )
                        }
                    }
                    // Add empty box if odd number of items in last row
                    if (rowItems.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_normal)))
        
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
            containerColor = MaterialTheme.colorScheme.background
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
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(R.dimen.spacing_small))
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
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = if (isUserSignIn) {
                stringResource(R.string.explore_today)
            } else {
                stringResource(R.string.digital_library_companion_subtitle)
            },
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}