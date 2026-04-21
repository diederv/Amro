package com.amro.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// LUCID is a branded, neon-on-deep-space experience. Dynamic color is off by design
// (the palette is the brand). A light scheme is provided for completeness but the
// app is intended to run dark.

private val DarkColors = darkColorScheme(
    primary = NeonPurple,
    onPrimary = OnNeonPurple,
    primaryContainer = NeonPurpleContainer,
    onPrimaryContainer = OnNeonPurpleContainer,
    secondary = ElectricCyan,
    onSecondary = OnElectricCyan,
    secondaryContainer = ElectricCyanContainer,
    onSecondaryContainer = OnElectricCyanContainer,
    tertiary = VividMagenta,
    onTertiary = OnVividMagenta,
    tertiaryContainer = VividMagentaContainer,
    onTertiaryContainer = OnVividMagentaContainer,
    background = VoidBackground,
    onBackground = OnSurface,
    surface = VoidBackground,
    onSurface = OnSurface,
    surfaceVariant = SurfaceHighest,
    onSurfaceVariant = OnSurfaceVariant,
    surfaceTint = NeonPurple,
    surfaceBright = SurfaceBright,
    surfaceDim = VoidBackground,
    surfaceContainerLowest = Color.Black,
    surfaceContainerLow = SurfaceLow,
    surfaceContainer = SurfaceMid,
    surfaceContainerHigh = SurfaceHigh,
    surfaceContainerHighest = SurfaceHighest,
    outline = Outline,
    outlineVariant = OutlineVariant,
    error = ErrorNeon,
    onError = OnErrorNeon,
    errorContainer = ErrorContainerNeon,
    onErrorContainer = OnErrorContainerNeon,
)

private val LightColors = lightColorScheme(
    primary = NeonPurpleDim,
    secondary = ElectricCyanContainer,
    tertiary = VividMagentaContainer,
)

@Composable
fun AmroTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    @Suppress("UNUSED_PARAMETER") dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors
    MaterialTheme(
        colorScheme = colorScheme,
        typography = LucidTypography,
        shapes = LucidShapes,
        content = content,
    )
}
