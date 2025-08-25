package org.librarease.app

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
    
    // Create ThemePreferences instance
    private lateinit var themePreferences: ThemePreferences
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize ThemePreferences
        themePreferences = ThemePreferences(applicationContext)
        
        // Initialize theme from saved preferences
        lifecycleScope.launch {
            val savedThemeMode = themePreferences.themeMode.first()
            ThemeController.initialize(themePreferences, savedThemeMode)
            
            // Now set the content with the initialized theme
            setContent {
                // Get the current theme mode as a state
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
}

