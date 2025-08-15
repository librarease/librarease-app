package org.librarease.app.presentation.main.components


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.material3.MaterialTheme
import org.librarease.app.domain.model.BookItem
import org.librarease.app.domain.model.Library
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.ui.res.colorResource
import org.librarease.app.R
import org.librarease.app.domain.model.CategoryItem



@Composable
fun MainContent(
    innerPadding: PaddingValues,
    isUserSignIn: Boolean,
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit,
    bookList: List<BookItem>,
    libraryList: List<Library>,
    searchQuery: String = "",
    onSearchQueryChange: (String) -> Unit = {},
    onSeeAllBooksClick: () -> Unit = {},
    onLibraryClick: (Library) -> Unit = {}
) {
    val scrollState = rememberScrollState()
    val bookRowState = rememberLazyListState()
    val libraryRowState = rememberLazyListState()

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
        ) {
            Box(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(bottomEnd = 24.dp, bottomStart = 24.dp))
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
                    .padding(16.dp),
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Top
                ) {
                    Text(
                        text = "Hello "
                    )
                }
            }

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { onSearchQueryChange(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                placeholder = { Text("Search books...") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon",
                        tint = colorResource(id = R.color.primary)
                    )
                },
                shape = RoundedCornerShape(24.dp),
                singleLine = true
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Results",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                
                Row(
                    modifier = Modifier
                        .clickable { onSeeAllBooksClick() },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "See All",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "See all books",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }
            
            LazyBookRow(
                lazyRowState = bookRowState,
                modifier = Modifier,
                itemList = bookList
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Libraries",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp, top = 8.dp)
            )
            LazyLibraryRow(
                lazyRowState = libraryRowState,
                modifier = Modifier,
                itemList = libraryList,
                onLibraryClick = onLibraryClick
            )
        }

        if (!isUserSignIn) {
            SignInCardView(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(16.dp),
                onSignUpClick = onSignUpClick,
                onLoginClick = onLoginClick
            )
        }
    }
}


