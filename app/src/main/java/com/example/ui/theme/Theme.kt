package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = AccentIndigo,
    secondary = AccentPurple,
    tertiary = AccentCyan,
    background = NavyDarkBackground,
    surface = NavySurfaceDark,
    surfaceVariant = NavySurfaceVariantDark,
    onPrimary = TextPrimaryDark,
    onBackground = TextPrimaryDark,
    onSurface = TextPrimaryDark,
    outline = BorderStrokeDark
)

private val LightColorScheme = lightColorScheme(
    primary = AccentIndigo,
    secondary = AccentPurple,
    tertiary = AccentCyan,
    background = LightBackground,
    surface = LightSurface,
    surfaceVariant = LightSurfaceVariant,
    onPrimary = LightSurface,
    onBackground = TextPrimaryLight,
    onSurface = TextPrimaryLight,
    outline = LightBorder
)

@Composable
fun OfflineAiReceptionistTheme(
    themePreference: String = "DARK",
    content: @Composable () -> Unit
) {
    val isDark = when (themePreference) {
        "LIGHT" -> false
        "DARK" -> true
        else -> isSystemInDarkTheme()
    }

    val colors = if (isDark) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}
