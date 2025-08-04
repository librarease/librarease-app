package org.librarease.app.presentation.profile.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.librarease.app.R
import org.librarease.app.core.generateQrCode

@Composable
fun ProfileContent(
    innerPadding: PaddingValues,
    userId: String
) {
    val qrBitmap = remember(userId) { generateQrCode(userId) }
    Log.d("##userID", "ProfileContent: $userId and QrBitmap $qrBitmap")
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(top = 48.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            bitmap = qrBitmap.asImageBitmap(),
            contentDescription = "User QR Code",
            modifier = Modifier.size(200.dp)
        )
    }
}