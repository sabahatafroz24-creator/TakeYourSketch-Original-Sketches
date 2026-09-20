package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = StudioInkLight,
    onPrimary = StudioCharcoalBg,
    primaryContainer = StudioSurfaceElevated,
    onPrimaryContainer = StudioInkLight,
    secondary = StudioGraphiteMuted,
    onSecondary = StudioCharcoalBg,
    tertiary = StudioWaxRed,
    background = StudioCharcoalBg,
    onBackground = StudioInkLight,
    surface = StudioSurface,
    onSurface = StudioInkLight,
    surfaceVariant = StudioSurfaceElevated,
    onSurfaceVariant = StudioGraphiteMuted,
    outline = StudioBorder,
  )

private val LightColorScheme =
  lightColorScheme(
    primary = InkPrimary,
    onPrimary = PaperSheet,
    primaryContainer = PaperDark,
    onPrimaryContainer = InkPrimary,
    secondary = Graphite,
    onSecondary = PaperSheet,
    tertiary = WaxRed,
    background = PaperBackground,
    onBackground = InkPrimary,
    surface = PaperSheet,
    onSurface = InkPrimary,
    surfaceVariant = PaperDark,
    onSurfaceVariant = Graphite,
    outline = LineBorder,
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }
      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}

@Composable
fun TysTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit,
) {
  MyApplicationTheme(darkTheme = darkTheme, dynamicColor = false, content = content)
}

