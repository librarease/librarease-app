package org.librarease.app.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun PasswordField(
    password: TextFieldValue,
    onPasswordChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    isConfirmPassword: Boolean = false
) {
    var passwordVisibility by remember { mutableStateOf(false) }
    var isFocused by remember { mutableStateOf(false) }
    var isPasswordTouched by remember { mutableStateOf(false) }
    
    // Password strength calculation
    val passwordStrength by remember(password.text) {
        derivedStateOf {
            calculatePasswordStrength(password.text)
        }
    }
    
    val isPasswordValid by remember(password.text) {
        derivedStateOf {
            password.text.length >= 6
        }
    }
    
    val showError = isPasswordTouched && !isPasswordValid && !isFocused
    
    val strengthColor = when {
        passwordStrength > 0.7 -> Color(0xFF4CAF50) // Strong - Green
        passwordStrength > 0.3 -> Color(0xFFFFC107) // Medium - Yellow
        passwordStrength > 0 -> Color(0xFFF44336) // Weak - Red
        else -> MaterialTheme.colorScheme.surfaceVariant // Empty
    }
    
    val labelText = if (isConfirmPassword) "Confirm password" else "Password"

    Column {
        OutlinedTextField(
            modifier = modifier
                .fillMaxWidth()
                .onFocusChanged { 
                    isFocused = it.isFocused 
                    if (!it.isFocused) {
                        isPasswordTouched = true
                    }
                },
            value = password,
            onValueChange = onPasswordChange,
            label = {
                Text(text = labelText)
            },
            singleLine = true,
            visualTransformation = if(passwordVisibility) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Lock,
                    contentDescription = "Password Icon",
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            trailingIcon = {
                if (showError) {
                    Icon(
                        imageVector = Icons.Filled.Error,
                        contentDescription = "Error",
                        tint = MaterialTheme.colorScheme.error
                    )
                } else {
                    val icon = if (passwordVisibility) {
                        Icons.Filled.Visibility
                    } else {
                        Icons.Filled.VisibilityOff
                    }
                    IconButton(
                        onClick = {
                            passwordVisibility = !passwordVisibility
                        }
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = if (passwordVisibility) "Hide password" else "Show password",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            },
            isError = showError
        )
        
        // Only show password strength for new passwords, not for confirmation
        if (!isConfirmPassword && password.text.isNotEmpty()) {
            LinearProgressIndicator(
                progress = { passwordStrength },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                color = strengthColor,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
            
            Text(
                text = when {
                    passwordStrength > 0.7 -> "Strong password"
                    passwordStrength > 0.3 -> "Medium password"
                    else -> "Weak password"
                },
                color = strengthColor,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
        
        AnimatedVisibility(visible = showError) {
            Text(
                text = "Password must be at least 6 characters",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}

private fun calculatePasswordStrength(password: String): Float {
    if (password.isEmpty()) return 0f
    
    var score = 0f
    
    // Length check
    score += minOf(0.4f, password.length * 0.03f) // Max 40% for length
    
    // Complexity checks
    if (password.any { it.isDigit() }) score += 0.1f
    if (password.any { it.isUpperCase() }) score += 0.2f
    if (password.any { it.isLowerCase() }) score += 0.1f
    if (password.any { !it.isLetterOrDigit() }) score += 0.2f
    
    return minOf(1.0f, score)
}