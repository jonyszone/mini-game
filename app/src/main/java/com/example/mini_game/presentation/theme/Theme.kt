package com.example.mini_game.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val BrainSparkColors = lightColorScheme(
    primary = Color(0xFF246BFE),
    secondary = Color(0xFFFFB020),
    tertiary = Color(0xFF17A398),
    background = Color(0xFFF7F8FC),
    surface = Color(0xFFFFFFFF),
    error = Color(0xFFBA1A1A)
)

@Composable
fun BrainSparkTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = BrainSparkColors, typography = MaterialTheme.typography, content = content)
}
