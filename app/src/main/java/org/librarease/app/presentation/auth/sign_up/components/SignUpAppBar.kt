package org.librarease.app.presentation.auth.sign_up.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import org.librarease.app.R
import org.librarease.app.common.ActionIconButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpAppBar(
    onArrowBackClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource( id = R.string.sign_up_screen_title),
                color = Color.White
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(colorResource(R.color.primary)),
        navigationIcon = {
            ActionIconButton(
                onActionIconButtonClick = onArrowBackClick,
                imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                resourceId = R.string.navigate_back
            )
        }
    )
}