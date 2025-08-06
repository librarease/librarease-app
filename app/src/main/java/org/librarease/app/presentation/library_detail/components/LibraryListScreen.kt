package org.librarease.app.presentation.library_detail.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.librarease.app.domain.model.Library
import org.librarease.app.presentation.main.components.LazyLibraryRow

@Composable
fun LibraryListScreen(
    modifier: Modifier = Modifier,
    libraryList: List<Library>,
    onLibraryClick: (Library)-> Unit
) {
    val libraryRowState = rememberLazyListState()
    Text(
        text = "Libraries",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        modifier = modifier.padding(start = 16.dp, top = 8.dp)
    )
    LazyLibraryRow(
        lazyRowState = libraryRowState,
        modifier = modifier,
        itemList = libraryList,
        onLibraryClick = onLibraryClick
    )
}