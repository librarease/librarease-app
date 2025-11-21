package org.librarease.app.presentation.main.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.delay
import org.librarease.app.R

data class CarouselBook(
    val id: String,
    val title: String,
    val author: String,
    val cover: String?
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AutoScrollingCarousel(
    books: List<CarouselBook>,
    modifier: Modifier = Modifier,
    onBookClick: (String) -> Unit = {}
) {
    if (books.isEmpty()) return
    
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { books.size }
    )
    
    LaunchedEffect(Unit) {
        while (true) {
            delay(3000)
            val nextPage = (pagerState.currentPage + 1) % books.size
            pagerState.animateScrollToPage(nextPage)
        }
    }
    
    Column(modifier = modifier) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            pageSpacing = dimensionResource(R.dimen.spacing_normal),
            contentPadding = PaddingValues(horizontal = dimensionResource(R.dimen.spacing_normal))
        ) { page ->
            CarouselBookCard(
                book = books[page],
                onClick = { onBookClick(books[page].id) }
            )
        }
        
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_small)))
        
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(R.dimen.spacing_normal)),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(books.size) { index ->
                Box(
                    modifier = Modifier
                        .size(if (pagerState.currentPage == index) 8.dp else 6.dp)
                        .clip(RoundedCornerShape(50))
                        .background(
                            if (pagerState.currentPage == index) 
                                MaterialTheme.colorScheme.primary 
                            else 
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                        )
                )
                if (index < books.size - 1) {
                    Spacer(modifier = Modifier.width(4.dp))
                }
            }
        }
    }
}

@Composable
fun CarouselBookCard(
    book: CarouselBook,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        elevation = CardDefaults.cardElevation(0.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(R.dimen.spacing_normal)),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_normal))
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(book.cover)
                    .crossfade(true)
                    .build(),
                contentDescription = book.title,
                modifier = Modifier
                    .width(100.dp)
                    .fillMaxHeight()
                    .clip(MaterialTheme.shapes.small),
                contentScale = ContentScale.Crop
            )
            
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = book.author,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_small)))
                
                Text(
                    text = "Featured Book",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
