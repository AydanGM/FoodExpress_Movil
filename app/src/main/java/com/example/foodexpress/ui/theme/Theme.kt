package com.example.foodexpress.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = PrimaryRed,
    onPrimary = Color.White,
    background = Color(0xFFF5F5F5),
    surface = Color.White
)

private val DarkColors = darkColorScheme(
    primary = DarkRed,
    onPrimary = Color.White,
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E)
)

@Composable
fun FoodExpressTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (!darkTheme) LightColors else DarkColors

    MaterialTheme(
        colorScheme = colors,
        typography = FoodExpressTypography,
        content = content
    )
}