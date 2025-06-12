package org.librarease.app.presentation.main.components


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import org.librarease.app.R
import androidx.compose.ui.draw.clip
import org.librarease.app.domain.model.BookItem
import org.librarease.app.domain.model.Library


@Composable
fun MainContent(
    innerPadding: PaddingValues,
    isUserSignIn: Boolean,
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit,
    bookList: List<BookItem>,
    libraryList: List<Library>
) {
    var searchQuery by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()
    val bookRowState = rememberLazyListState()
    val libraryRowState = rememberLazyListState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.background))
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
                    .background(colorResource(id = R.color.primary).copy(alpha = 0.1f))
                    .padding(16.dp),
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Filled.MenuBook,
                        contentDescription = "Librarease Logo",
                        modifier = Modifier.size(64.dp),
                        tint = colorResource(id = R.color.primary)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Your Digital Library Management Solution",
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
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

            Text(
                text = "Featured Books",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp, top = 8.dp)
            )
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
                itemList = libraryList
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


