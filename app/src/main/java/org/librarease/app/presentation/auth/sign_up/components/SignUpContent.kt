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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    onSignUpClick: (String, String) -> Unit,
    isLoading: Boolean,
    onSignInTextClick: () -> Unit
) {
    val keyboard = LocalSoftwareKeyboardController.current
    var fullName by remember { mutableStateOf(TextFieldValue("")) }
    var agreeToTerms by remember { mutableStateOf(false) }
    
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
            text = "Join the Reading Community",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        
        Text(
            text = "Create Account",
            color = MaterialTheme.colorScheme.primary,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(bottom = 16.dp)
                .align(Alignment.CenterHorizontally)
        )
        
        // Full Name Field
        OutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text("Full Name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        EmailField(
            email = email,
            onEmailChange = onEmailChange
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        PasswordField(
            password = password,
            onPasswordChange = onPasswordChange
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
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
                text = "I agree to the Terms of Service and Privacy Policy",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        ActionButton(
            onActionButtonClick = {
                val isEmailValid = email.text.isNotBlank()
                val isPasswordValid = password.text.isNotBlank()
                val isNameValid = fullName.text.isNotBlank()
                
                if (!isNameValid) {
                    // Handle name validation
                } else if (!isEmailValid) {
                    onEmailInvalid()
                } else if (!isPasswordValid) {
                    onPasswordInvalid()
                } else if (!agreeToTerms) {
                    // Handle terms agreement validation
                } else {
                    // For now, we're still using the existing sign-up function
                    // In a real implementation, you'd want to pass the full name too
                    onSignUpClick(email.text, password.text)
                    keyboard?.hide()
                }
            },
            enabled = !isLoading,
            resourceId = R.string.sign_up_button
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Sign in text
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Already have an account? ",
                color = MaterialTheme.colorScheme.onSurface
            )
            ActionText(
                onActionTextClick = onSignInTextClick,
                resourceId = R.string.sign_in_button
            )
        }
    }
}