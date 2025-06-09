package org.librarease.app.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp

@Composable
fun ActionText(
    onActionTextClick: () -> Unit,
    resourceId: Int,
    modifier: Modifier = Modifier,
    textDecoration: TextDecoration = TextDecoration.None
) {
    val interactionSource = remember { MutableInteractionSource() }
    
    Text(
        modifier = modifier.clickable(
            interactionSource = interactionSource,
            indication = null
        ) {
            onActionTextClick()
        },
        text = stringResource(id = resourceId),
        color = MaterialTheme.colorScheme.primary,
        fontSize = 15.sp,
        fontWeight = FontWeight.Medium,
        textDecoration = textDecoration
    )
}