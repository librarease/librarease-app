package org.librarease.app.presentation.auth.forgot_psw.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import org.librarease.app.R
import org.librarease.app.common.ActionIconButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordAppBar(
    onArrowBackIconClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(
                    id = R.string.forgot_password_screen_title
                )
            )
        },
        navigationIcon = {
            ActionIconButton(
                onActionIconButtonClick = onArrowBackIconClick,
                imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                resourceId = R.string.navigate_back
            )
        }
    )
}