package org.librarease.app.presentation.auth.sign_up.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import org.librarease.app.R
import org.librarease.app.common.ActionButton
import org.librarease.app.common.ActionText
import org.librarease.app.common.EmailField
import org.librarease.app.common.PasswordField

@Composable
fun SignUpContent(
    innerPadding: PaddingValues,
    email: TextFieldValue,
    onEmailChange: (TextFieldValue) -> Unit,
    onEmailInvalid: () -> Unit,
    password: TextFieldValue,
    onPasswordChange: (TextFieldValue) -> Unit,
    onPasswordInvalid: () -> Unit,
    fullName: TextFieldValue,
    onFullNameChange: (TextFieldValue) -> Unit,
    onFullNameInvalid: () -> Unit,
    onTermsNotAccepted: () -> Unit,
    onSignUpClick: (String, String, String) -> Unit,
    isLoading: Boolean,
    onSignInTextClick: () -> Unit
) {
    val keyboard = LocalSoftwareKeyboardController.current
    var agreeToTerms by remember { mutableStateOf(false) }
    
    Column(
        modifier = Modifier
            .wrapContentHeight()
            .padding(dimensionResource(R.dimen.spacing_large)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
            text = stringResource(R.string.join_reading_community),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = dimensionResource(R.dimen.spacing_large))
        )
        
        Text(
            text = stringResource(R.string.create_account_title),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .padding(bottom = dimensionResource(R.dimen.spacing_normal))
                .align(Alignment.CenterHorizontally)
        )
        
        // Full Name Field
        OutlinedTextField(
            value = fullName,
            onValueChange = onFullNameChange,
            label = { Text(stringResource(R.string.full_name_label)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = stringResource(R.string.person_icon),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        )
        
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_medium)))
        
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
        
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_normal)))
        
        // Terms and Conditions Checkbox
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = agreeToTerms,
                onCheckedChange = { agreeToTerms = it }
            )
            Text(
                text = stringResource(R.string.terms_agreement),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_normal)))
        
        ActionButton(
            onActionButtonClick = {
                val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$"
                val isEmailValid = email.text.isNotBlank() && email.text.matches(Regex(emailRegex))
                val isPasswordValid = password.text.isNotBlank() && password.text.length >= 6
                val isFullNameValid = fullName.text.isNotBlank() && fullName.text.trim().length >= 2
                val isTermsAccepted = agreeToTerms
                
                if (!isEmailValid) {
                    onEmailInvalid()
                } else if (!isPasswordValid) {
                    onPasswordInvalid()
                } else if (!isFullNameValid) {
                    onFullNameInvalid()
                } else if (!isTermsAccepted) {
                    onTermsNotAccepted()
                } else {
                    onSignUpClick(email.text.trim(), password.text, fullName.text.trim())
                    keyboard?.hide()
                }
            },
            enabled = !isLoading,
            isLoading = isLoading,
            resourceId = R.string.sign_up_button
        )
        
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_large)))
        
        // Sign in text
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.already_have_account),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            ActionText(
                onActionTextClick = onSignInTextClick,
                resourceId = R.string.sign_in_button,
                modifier = Modifier
            )
        }
    }
}