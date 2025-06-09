package org.librarease.app.presentation.profile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.librarease.app.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileAppBar(
    isUserSignIn: Boolean,
    signOut: () -> Unit,
    deleteUser: () -> Unit,
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit
) {
    var openMenu by remember { mutableStateOf(false) }

    TopAppBar(
        title = {

        },
        actions = {
            if (isUserSignIn) {
                IconButton(onClick = { openMenu = !openMenu }) {
                    Icon(
                        imageVector = Icons.Outlined.MoreVert,
                        contentDescription = null
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
                    DropdownMenuItem(
                        onClick = {
                            deleteUser()
                            openMenu = false
                        },
                        text = {
                            Text(text = stringResource(id = R.string.delete_user))
                        }
                    )
                }
            } else {
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .padding(
                            top = 16.dp,
                            end = 4.dp
                        )
                        .fillMaxWidth(0.5f)
                ) {
                    Button(
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp),
                        onClick = onLoginClick,
                        colors = ButtonColors(
                            containerColor = colorResource(R.color.primary),
                            contentColor = colorResource(R.color.white),
                            disabledContainerColor = Color.Gray,
                            disabledContentColor = Color.Gray
                        ),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.sign_in_button),
                            fontSize = 12.sp,
                            maxLines = 1,
                            color = Color.White,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Button(
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp),
                        onClick = onSignUpClick,
                        colors = ButtonColors(
                            Color.LightGray,
                            contentColor = colorResource(R.color.text),
                            disabledContainerColor = Color.Gray,
                            disabledContentColor = Color.Gray
                        ),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.sign_up_button),
                            fontSize = 12.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    )
}
