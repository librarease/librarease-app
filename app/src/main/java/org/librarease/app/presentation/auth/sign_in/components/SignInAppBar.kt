package org.librarease.app.presentation.auth.sign_in.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import org.librarease.app.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInTopBar() {
    TopAppBar (
        title = {
            Text(
                text = stringResource(
                    id = R.string.sign_in_screen_title
                ),
                color = Color.White
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(colorResource(R.color.primary))
    )
}