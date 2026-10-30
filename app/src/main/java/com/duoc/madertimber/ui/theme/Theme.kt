package com.duoc.madertimber.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = ForestGreenDark,
    onPrimary = Color(0xFF00391C),
    secondary = ForestGreenDark,
    secondaryContainer = PineContainerDark,
    onSecondaryContainer = Color(0xFFDCEFE1),
    background = Color(0xFF0C1710),
    surface = Color(0xFF142018),
    surfaceVariant = Color(0xFF26372B),
    surfaceContainerLowest = Color(0xFF101B14),
    surfaceContainerLow = Color(0xFF17241B),
    surfaceContainer = Color(0xFF1B2920),
    surfaceContainerHigh = Color(0xFF223128),
    surfaceContainerHighest = Color(0xFF293A2E),
    outline = Color(0xFF879B8A),
    onSurface = Color(0xFFE4F0E5)
)

private val LightColorScheme = lightColorScheme(
    primary = ForestGreen,
    onPrimary = Color.White,
    secondary = ForestGreen,
    secondaryContainer = PineContainer,
    onSecondaryContainer = Color(0xFF153D24),
    background = Color(0xFFF8FBF8),
    surface = Color.White,
    surfaceVariant = Color(0xFFEAF2EA),
    surfaceContainerLowest = Color.White,
    surfaceContainerLow = Color(0xFFF5F9F5),
    surfaceContainer = Color(0xFFF3F7F3),
    surfaceContainerHigh = Color(0xFFEBF2EB),
    surfaceContainerHighest = Color(0xFFE5EEE6),
    outline = Color(0xFF708174),
    onSurface = Color(0xFF17251B)
)

@Composable
fun MaderaTimberTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        content = content
    )
}
