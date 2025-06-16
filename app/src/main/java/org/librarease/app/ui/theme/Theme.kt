package org.librarease.app.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// Custom color schemes using our app-specific colors
private val DarkColorScheme = darkColorScheme(
    primary = PrimaryDark,
    background = BackgroundDark,
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    error = ErrorDark
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryLight,
    background = BackgroundLight,
    surface = SurfaceLight,
    onSurface = OnSurfaceLight,
    error = ErrorLight
)

// Theme state holder
enum class ThemeMode {
    LIGHT, DARK, SYSTEM
}

// Theme controller to manage theme state
object ThemeController {
    // Default to system theme
    private var _themeMode = mutableStateOf(ThemeMode.SYSTEM)
    val themeMode: ThemeMode get() = _themeMode.value
    
    // Reference to ThemePreferences for persistence (set in MainActivity)
    private var themePreferences: org.librarease.app.data.preferences.ThemePreferences? = null
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    
    fun initialize(preferences: org.librarease.app.data.preferences.ThemePreferences, initialMode: ThemeMode) {
        themePreferences = preferences
        _themeMode.value = initialMode
    }
    
    fun setThemeMode(mode: ThemeMode) {
        _themeMode.value = mode
        // Save the preference
        themePreferences?.let { prefs ->
            coroutineScope.launch {
                prefs.saveThemeMode(mode)
            }
        }
    }
    
    fun toggleTheme() {
        val newMode = when (_themeMode.value) {
            ThemeMode.LIGHT -> ThemeMode.DARK
            ThemeMode.DARK -> ThemeMode.LIGHT
            ThemeMode.SYSTEM -> ThemeMode.LIGHT // Default to light if coming from system
        }
        setThemeMode(newMode)
    }
}

// Local composition provider for theme mode
val LocalThemeMode = staticCompositionLocalOf { ThemeMode.SYSTEM }

@Composable
fun LibrareaseTheme(
    // Use the theme mode from ThemeController, falling back to system default
    themeMode: ThemeMode = ThemeController.themeMode,
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // Disabled by default to use our custom colors
    content: @Composable () -> Unit
) {
    // Determine if dark theme should be used based on theme mode
    val darkTheme = when (themeMode) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
    }
    
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    CompositionLocalProvider(LocalThemeMode provides themeMode) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}