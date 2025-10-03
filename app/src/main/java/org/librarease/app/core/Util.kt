package org.librarease.app.core

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.vector.ImageVector
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import androidx.core.graphics.createBitmap
import androidx.core.graphics.set

const val TAG = "AppTag"
const val EMPTY_STRING = ""

fun logErrorMessage(
    errorMessage: String
) = Log.e(TAG, errorMessage)

fun showToastMessage(
    context: Context,
    message: String
) = Toast.makeText(context, message, Toast.LENGTH_LONG).show()

fun generateQrCode(content: String, size: Int = 512): Bitmap {
    val writer = QRCodeWriter()
    val bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, size, size)
    val bitmap = createBitmap(size, size, Bitmap.Config.RGB_565)
    for(x in 0 until size) {
        for(y in 0 until size) {
            bitmap[x, y] =
                if (bitMatrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE
        }
    }
    return bitmap
}

sealed class MainNavItem(val label: String, val icon: ImageVector, val route: String) {
    object Home: MainNavItem("Home", Icons.Filled.Home, "home")
    object Settings: MainNavItem("Settings", Icons.Filled.Settings, "settings")

    companion object {
        val items = listOf(Home, Settings)
    }
}

