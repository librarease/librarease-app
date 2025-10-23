package org.librarease.app.presentation.auth.sign_in.components

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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import org.librarease.app.R
import org.librarease.app.common.ActionButton
import org.librarease.app.common.ActionText
import org.librarease.app.common.EmailField
import org.librarease.app.common.PasswordField

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
    continueAsGuestClick: () -> Unit,
    onSignUpTextClick: () -> Unit
) {
    val keyboard = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .wrapContentHeight()
            .padding(dimensionResource(R.dimen.spacing_large)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // App Logo
        Icon(
            imageVector = Icons.Filled.MenuBook,
            contentDescription = stringResource(R.string.librarease_logo),
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        
        Text(
            text = stringResource(R.string.librarease_title),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = dimensionResource(R.dimen.spacing_small))
        )
        
        Text(
            text = stringResource(R.string.digital_library_companion),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = dimensionResource(R.dimen.spacing_large))
        )
        
        Text(
            text = stringResource(R.string.sign_in_title),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .padding(bottom = dimensionResource(R.dimen.spacing_normal))
                .align(Alignment.CenterHorizontally)
        )
        
        EmailField(
            email = email,
            onEmailChange = onEmailChange,
            autoFocus = true
        )
        
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_medium)))
        
        PasswordField(
            password = password,
            onPasswordChange = onPasswordChange
        )
        
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_small)))
        
        ActionText(
            onActionTextClick = onForgotPasswordClick,
            resourceId = R.string.forgot_password,
            modifier = Modifier
                .align(Alignment.End)
                .padding(end = dimensionResource(R.dimen.spacing_xs), bottom = dimensionResource(R.dimen.spacing_normal))
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
            isLoading = isLoading,
            resourceId = R.string.sign_in_button
        )
        
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_large)))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.outlineVariant
            )
            Text(
                text = stringResource(R.string.or_divider),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.spacing_normal)),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.outlineVariant
            )
        }
        
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_large)))

        ActionText(
            onActionTextClick = continueAsGuestClick,
            resourceId = R.string.continue_as_guest,
            modifier = Modifier
                .padding(bottom = dimensionResource(R.dimen.spacing_large))
                .align(Alignment.CenterHorizontally),
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.no_account_text),
                style = MaterialTheme.typography.bodyMedium,
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