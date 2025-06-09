package org.librarease.app.presentation.auth.sign_in.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.librarease.app.R
import org.librarease.app.common.ActionButton
import org.librarease.app.common.ActionIconButton
import org.librarease.app.common.ActionText
import org.librarease.app.common.EmailField
import org.librarease.app.common.PasswordField

private const val VERTICAL_DIVIDER = "|"

@Composable
fun SignInContent(
    email: TextFieldValue,
    onEmailChange: (TextFieldValue) -> Unit,
    onEmailInvalid: () -> Unit,
    password: TextFieldValue,
    onPasswordChange: (TextFieldValue) -> Unit,
    onPasswordInvalid: () -> Unit,
    onSignInClick: (String, String) -> Unit,
    isLoading: Boolean,
    onForgotPasswordClick: () -> Unit,
    onSignUpTextClick: () -> Unit
) {
    val keyboard = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .wrapContentHeight()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // App Logo
        Icon(
            imageVector = Icons.Filled.MenuBook,
            contentDescription = "Librarease Logo",
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        
        Text(
            text = "Librarease",
            color = MaterialTheme.colorScheme.primary,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        Text(
            text = "Your Digital Library Companion",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        
        Text(
            text = "Sign In",
            color = MaterialTheme.colorScheme.primary,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(bottom = 16.dp)
                .align(Alignment.CenterHorizontally)
        )
        
        EmailField(
            email = email,
            onEmailChange = onEmailChange
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        PasswordField(
            password = password,
            onPasswordChange = onPasswordChange
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Forgot Password link aligned to the right
        ActionText(
            onActionTextClick = onForgotPasswordClick,
            resourceId = R.string.forgot_password,
            modifier = Modifier
                .align(Alignment.End)
                .padding(end = 4.dp, bottom = 16.dp)
        )
        
        ActionButton(
            onActionButtonClick = {
                val isEmailValid = email.text.isNotBlank()
                val isPasswordValid = password.text.isNotBlank()
                if (!isEmailValid) {
                    onEmailInvalid()
                } else if (!isPasswordValid) {
                    onPasswordInvalid()
                } else {
                    onSignInClick(email.text, password.text)
                    keyboard?.hide()
                }
            },
            enabled = !isLoading,
            resourceId = R.string.sign_in_button
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // OR divider
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Divider(
                modifier = Modifier.weight(1f),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )
            Text(
                text = "OR",
                modifier = Modifier.padding(horizontal = 16.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Divider(
                modifier = Modifier.weight(1f),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Social sign-in options would go here
        // For now, just showing a placeholder text
        Text(
            text = "Continue as Guest",
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .padding(bottom = 24.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        
        // Sign up text
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Don't have an account? ",
                color = MaterialTheme.colorScheme.onSurface
            )
            ActionText(
                onActionTextClick = onSignUpTextClick,
                resourceId = R.string.sign_up_button,
                modifier = Modifier
            )
        }
    }
}