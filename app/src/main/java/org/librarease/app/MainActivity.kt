package org.librarease.app

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.librarease.app.data.preferences.ThemePreferences
import org.librarease.app.presentation.navigation.AppNavGraph
import org.librarease.app.ui.theme.LibrareaseTheme
import org.librarease.app.ui.theme.ThemeController
import org.librarease.app.ui.theme.ThemeMode
import dagger.hilt.android.AndroidEntryPoint
import org.librarease.app.data.remote.LibrareaseApi

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var themePreferences: ThemePreferences
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        createNotificationChannel()

        themePreferences = ThemePreferences(applicationContext)
        
        lifecycleScope.launch {
            val savedThemeMode = themePreferences.themeMode.first()
            ThemeController.initialize(themePreferences, savedThemeMode)
            
            setContent {
                val themeMode by themePreferences.themeMode.collectAsState(initial = ThemeMode.SYSTEM)
                
                LibrareaseTheme(themeMode = themeMode) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        AppNavGraph()
                    }
                }
            }
        }
    }
    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "channel_id",
                "Channel Name",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = ""
                enableLights(true)
                lightColor = android.graphics.Color.RED
                enableVibration(true)
                vibrationPattern = longArrayOf(100, 200, 300, 400, 500)
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }
}

