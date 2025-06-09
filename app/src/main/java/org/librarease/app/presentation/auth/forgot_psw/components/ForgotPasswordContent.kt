package org.librarease.app.presentation.auth.forgot_psw.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import org.librarease.app.R
import org.librarease.app.common.ActionButton
import org.librarease.app.common.EmailField

@Composable
fun ForgotPasswordContent(
    innerPadding: PaddingValues,
    email: TextFieldValue,
    onEmailChange: (TextFieldValue) -> Unit,
    onEmailInvalid: () -> Unit,
    onSendPasswordResetEmail: (String) -> Unit,
    isLoading: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EmailField(
            email = email,
            onEmailChange = onEmailChange
        )
        Spacer(modifier = Modifier.height(8.dp))
        ActionButton(
            onActionButtonClick = {
                val isEmailValid = email.text.isNotBlank()
                if(!isEmailValid) {
                    onEmailInvalid()
                } else {
                    onSendPasswordResetEmail(email.text)
                }
            },
            enabled = !isLoading,
            resourceId = R.string.reset_password_button
        )
    }
}