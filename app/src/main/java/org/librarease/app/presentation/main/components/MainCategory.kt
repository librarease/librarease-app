package org.librarease.app.presentation.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MainCategory(
    modifier: Modifier = Modifier,
    categoryName: String,
    icon: ImageVector,
    onClick: (category: String)-> Unit
) {
    Box(
        modifier = modifier
            .size(250.dp)
            .background(Color.LightGray)
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick(categoryName) },
        contentAlignment = Alignment.Center,
    ) {
        Column {
            Icon(
                imageVector = icon,
                contentDescription = categoryName,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = categoryName,
                fontSize = 16.sp
            )
        }
    }
}