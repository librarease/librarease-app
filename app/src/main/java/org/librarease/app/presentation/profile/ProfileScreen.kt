package org.librarease.app.presentation.profile

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.auth.FirebaseAuth
import org.librarease.app.R
import org.librarease.app.common.LoadingIndicator
import org.librarease.app.core.Resource
import org.librarease.app.core.showToastMessage
import org.librarease.app.presentation.navigation.Route
import org.librarease.app.presentation.profile.components.ProfileAppBar
import org.librarease.app.presentation.profile.components.ProfileContent

@Composable
fun ProfileScreen(
    viewmodel: ProfileViewmodel = hiltViewModel(),
) {
    val context = LocalContext.current
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    val activity = context as Activity
    val snackbarHostState = remember { SnackbarHostState() }
    val isUserSignIn by viewmodel.authState.collectAsState()
    val deleteUserResponse by viewmodel.deleteUserState.collectAsState()
    val userDeletedMessage = stringResource(id = R.string.user_deleted_message)
    val sensitiveKeyword = stringResource(id = R.string.sensitive_keyword)
    val reauthenticationRequiredMessage = stringResource(id = R.string.reauthentication_required_message)
    val signOutActionLabel = stringResource(R.string.sign_out_action_label)

    BackHandler {
        activity.finish()
    }
    Scaffold(
        topBar = {

        }
    ) { innerPadding ->
        ProfileContent(
            innerPadding = innerPadding,
            userId = userId ?: ""
        )
    }

    when (val deleteUserResponse = deleteUserResponse) {
        is Resource.Idle -> {}
        is Resource.Loading -> LoadingIndicator()
        is Resource.Success -> LaunchedEffect(Unit) {
            showToastMessage(context, userDeletedMessage)
        }
        is Resource.Failure -> {
            deleteUserResponse.e?.message?.let { errorMessage ->
                LaunchedEffect(errorMessage) {
                    if (errorMessage.contains(sensitiveKeyword)) {
                        val result = snackbarHostState.showSnackbar(
                            message = reauthenticationRequiredMessage,
                            actionLabel = signOutActionLabel
                        )
                        if (result == SnackbarResult.ActionPerformed) {
                            viewmodel.signOut()
                        }
                    } else {
                        showToastMessage(context, errorMessage)
                    }
                }
            }
        }
    }
}
