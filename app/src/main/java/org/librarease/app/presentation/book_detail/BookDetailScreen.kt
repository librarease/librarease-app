package org.librarease.app.presentation.book_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import org.librarease.app.R
import org.librarease.app.domain.model.BookDetail
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailScreen(
    bookId: String,
    navigateBack: () -> Unit,
    viewModel: BookDetailViewModel = hiltViewModel()
) {
    val bookDetail by viewModel.bookDetail.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(bookId) {
        viewModel.loadBookDetail(bookId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.book_details_title),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_button)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* More options */ }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = stringResource(R.string.more_options)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                error != null -> {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(dimensionResource(R.dimen.spacing_normal)),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = error ?: stringResource(R.string.unknown_error),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_normal)))
                        Button(onClick = { viewModel.loadBookDetail(bookId) }) {
                            Text(stringResource(R.string.retry))
                        }
                    }
                }
                bookDetail != null -> {
                    BookDetailContent(bookDetail = bookDetail!!)
                }
            }
        }
    }
}

@Composable
fun BookDetailContent(bookDetail: BookDetail) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.background)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.spacing_large)),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .width(dimensionResource(R.dimen.book_cover_width))
                    .height(dimensionResource(R.dimen.book_cover_height))
            ) {
                if (bookDetail.cover != null && bookDetail.cover.isNotEmpty()) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(bookDetail.cover)
                            .crossfade(true)
                            .build(),
                        contentDescription = stringResource(R.string.book_cover),
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(MaterialTheme.shapes.medium),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(MaterialTheme.shapes.medium)
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${bookDetail.title} ${stringResource(R.string.book_cover)}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(dimensionResource(R.dimen.spacing_normal))
                        )
                    }
                }
                
                val isAvailable = bookDetail.currentBorrowing?.isReturned ?: true
                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(dimensionResource(R.dimen.spacing_small)),
                    shape = MaterialTheme.shapes.medium,
                    color = if (isAvailable) 
                        MaterialTheme.colorScheme.primary
                    else 
                        MaterialTheme.colorScheme.error
                ) {
                    Row(
                        modifier = Modifier.padding(
                            horizontal = dimensionResource(R.dimen.spacing_small),
                            vertical = dimensionResource(R.dimen.spacing_xs)
                        ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (isAvailable) Icons.Default.Check else Icons.Default.Close,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onTertiary,
                            modifier = Modifier.size(dimensionResource(R.dimen.icon_size_small))
                        )
                        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_xs)))
                        Text(
                            text = stringResource(if (isAvailable) R.string.available else R.string.borrowed),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onTertiary
                        )
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(R.dimen.spacing_large)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = bookDetail.title,
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_small)))
            Text(
                text = stringResource(R.string.by_author, bookDetail.author),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_normal)))
            
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null,
                    modifier = Modifier.size(dimensionResource(R.dimen.icon_size_small)),
                    tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
                Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_xs)))
                Text(
                    text = bookDetail.year.toString(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
                
                Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_normal)))
                
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    modifier = Modifier.size(dimensionResource(R.dimen.icon_size_small)),
                    tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
                Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_xs)))
                Text(
                    text = bookDetail.code,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
            }
        }

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_large)))
        
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(R.dimen.spacing_normal)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(dimensionResource(R.dimen.icon_size_xl))
                        .clip(MaterialTheme.shapes.small)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    bookDetail.library.name?.take(3)?.uppercase()?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                
                Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_medium)))
                
                Column(modifier = Modifier.weight(1f)) {
                    bookDetail.library.name?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_xs)))
                    Text(
                        text = stringResource(R.string.member_since, formatDate(bookDetail.library.created_at ?: "")),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.9f)
                    )
                }
                OutlinedButton(
                    onClick = {  },
                    modifier = Modifier
                ) {
                    Text(stringResource(R.string.view_library))
                }
            }

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_large)))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(R.dimen.spacing_large)),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_normal))
        ) {
            StatisticCard(
                icon = Icons.Default.Star,
                value = (bookDetail.borrowCount ?: 0).toString(),
                label = stringResource(R.string.times_borrowed),
                modifier = Modifier.weight(1f)
            )
            
            val daysLeft = bookDetail.currentBorrowing?.let { borrowing ->
                if (!borrowing.isReturned) {
                    calculateDaysLeft(borrowing.dueAt)
                } else null
            } ?: 0
            
            StatisticCard(
                icon = Icons.Default.DateRange,
                value = daysLeft.toString(),
                label = stringResource(R.string.days_left),
                modifier = Modifier.weight(1f)
            )
        }

        bookDetail.currentBorrowing?.let { borrowing ->
            if (!borrowing.isReturned) {
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_large)))
                
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = dimensionResource(R.dimen.spacing_large)),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(dimensionResource(R.dimen.spacing_normal))
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_small)))
                            Text(
                                text = stringResource(R.string.currently_borrowed),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_medium)))

                        BorrowingInfoRow(
                            label = stringResource(R.string.borrowed_on),
                            value = formatDate(borrowing.borrowedAt)
                        )

                        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_small)))

                        BorrowingInfoRow(
                            label = stringResource(R.string.due_date),
                            value = formatDate(borrowing.dueAt)
                        )

                        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_small)))

                        BorrowingInfoRow(
                            label = stringResource(R.string.status),
                            value = stringResource(if (borrowing.isReturned) R.string.returned else R.string.active)
                        )
                    }
                }
            } else {
                borrowing.returnedAt?.let { returnedAt ->
                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_large)))
                    
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = dimensionResource(R.dimen.spacing_large)),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        ),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(dimensionResource(R.dimen.spacing_normal))
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.tertiary
                                )
                                Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_small)))
                                Text(
                                    text = stringResource(R.string.previously_borrowed),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_medium)))
                            
                            BorrowingInfoRow(
                                label = stringResource(R.string.borrowed_on),
                                value = formatDate(borrowing.borrowedAt)
                            )
                            
                            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_small)))
                            
                            BorrowingInfoRow(
                                label = stringResource(R.string.due_date),
                                value = formatDate(borrowing.dueAt)
                            )
                            
                            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_small)))
                            
                            BorrowingInfoRow(
                                label = stringResource(R.string.status),
                                value = stringResource(R.string.returned)
                            )
                            
                            borrowing.fine?.let { fine ->
                                if (fine > 0) {
                                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_small)))
                                    BorrowingInfoRow(
                                        label = stringResource(R.string.fine),
                                        value = "$$fine"
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_large)))
    }
}

@Composable
fun StatisticCard(
    icon: ImageVector,
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.spacing_normal)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(dimensionResource(R.dimen.icon_size_large))
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_small)))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_xs)))
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun BorrowingInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

fun formatDate(dateString: String): String {
    return try {
        val cleanedDate = dateString.replace(" + 0000UTC", "").trim()
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")
        val date = inputFormat.parse(cleanedDate)
        val outputFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        date?.let { outputFormat.format(it) } ?: dateString
    } catch (e: Exception) {
        try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            inputFormat.timeZone = TimeZone.getTimeZone("UTC")
            val date = inputFormat.parse(dateString)
            val outputFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            date?.let { outputFormat.format(it) } ?: dateString
        } catch (e2: Exception) {
            dateString
        }
    }
}

fun calculateDaysLeft(dueDate: String): Int {
    return try {
        val cleanedDate = dueDate.replace(" + 0000UTC", "").trim()
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")
        val date = inputFormat.parse(cleanedDate)
        val today = Calendar.getInstance().time
        val diff = date?.time?.minus(today.time) ?: 0
        (diff / (1000 * 60 * 60 * 24)).toInt()
    } catch (e: Exception) {
        try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            inputFormat.timeZone = TimeZone.getTimeZone("UTC")
            val date = inputFormat.parse(dueDate)
            val today = Calendar.getInstance().time
            val diff = date?.time?.minus(today.time) ?: 0
            (diff / (1000 * 60 * 60 * 24)).toInt()
        } catch (e2: Exception) {
            0
        }
    }
}
