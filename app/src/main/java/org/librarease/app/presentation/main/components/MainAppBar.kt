package org.librarease.app.presentation.main.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.librarease.app.R
import org.librarease.app.ui.theme.ThemeController
import org.librarease.app.ui.theme.ThemeMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppBar(
    isUserSignIn: Boolean,
    signOut: () -> Unit,
    onLoginClick: () -> Unit,
) {
    var openMenu by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.app_name),
                color = colorResource(id = R.color.primary),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier.shadow(elevation = 4.dp),
        actions = {
            IconButton(onClick = { ThemeController.toggleTheme() }) {
                Icon(
                    imageVector = if (ThemeController.themeMode == ThemeMode.DARK) 
                        Icons.Filled.LightMode else Icons.Filled.DarkMode,
                    contentDescription = "Toggle theme",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            
            if (isUserSignIn) {
                IconButton(onClick = { openMenu = !openMenu }) {
                    Icon(
                        imageVector = Icons.Outlined.MoreVert,
                        contentDescription = "Menu",
                        tint = colorResource(id = R.color.primary)
                    )
                }

                DropdownMenu(
                    expanded = openMenu,
                    onDismissRequest = { openMenu = false }
                ) {
                    DropdownMenuItem(
                        onClick = {
                            signOut()
                            openMenu = false
                        },
                        text = {
                            Text(text = stringResource(id = R.string.sign_out))
                        }
                    )
                }
            } else {
                Button(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .height(36.dp),
                    onClick = onLoginClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.primary),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.sign_in_button),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    )
}
