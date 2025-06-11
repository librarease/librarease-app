package org.librarease.app.presentation.main.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.librarease.app.domain.model.Library

@Composable
fun LazyLibraryRow(
    lazyRowState: LazyListState,
    modifier: Modifier = Modifier,
    itemList: List<Library>
) {
    LazyRow(
        state = lazyRowState,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .animateContentSize(spring(stiffness = Spring.StiffnessMedium)),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        flingBehavior = rememberSnapFlingBehavior(lazyListState = lazyRowState)
    ) {
        items(itemList) { libraryItem ->
            LibraryCard(
                libraryItem.name,
                modifier = modifier
                    .padding(6.dp)
                    .wrapContentWidth(Alignment.CenterHorizontally)
            )
        }
    }
}