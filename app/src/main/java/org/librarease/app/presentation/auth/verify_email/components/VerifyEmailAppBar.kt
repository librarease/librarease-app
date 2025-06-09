package org.librarease.app.presentation.auth.verify_email.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import org.librarease.app.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerifyEmailAppBar() {
    TopAppBar(
        title = {
            Text(text = stringResource(
                id = R.string.verify_email_screen_title
            ))
        }
    )
}