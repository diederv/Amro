package com.amro.core.designsystem.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

/** 135° gradient from primary → primary-dim. The signature "neon tube" luminosity. */
@Composable
@ReadOnlyComposable
fun primaryGradient(): Brush = Brush.linearGradient(
    colors = listOf(NeonPurple, NeonPurpleDim),
    start = Offset.Zero,
    end = Offset.Infinite,
)

/** Purple → magenta → cyan ramp. Used for hero title tints and FAB. */
@Composable
@ReadOnlyComposable
fun heroTitleGradient(): Brush = Brush.linearGradient(
    colors = listOf(NeonPurple, VividMagenta, ElectricCyan),
)

/** Soft vertical scrim over a hero image so text remains readable. */
@Composable
@ReadOnlyComposable
fun heroScrim(): Brush = Brush.verticalGradient(
    colorStops = arrayOf(
        0f to Color.Transparent,
        0.55f to MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
        1f to MaterialTheme.colorScheme.surface,
    ),
)

/**
 * Glassmorphism surface — translucent fill + faint "ghost" stroke. Avoids the 1px
 * solid border rule by keeping the stroke at low opacity so edges read as reflection,
 * not structure.
 */
fun Modifier.glassSurface(
    shape: Shape = RoundedCornerShape(16.dp),
    tint: Color? = null,
    strokeOpacity: Float = 0.08f,
): Modifier = composed {
    val fill = (tint ?: MaterialTheme.colorScheme.surfaceContainerHigh).copy(alpha = 0.55f)
    this
        .clip(shape)
        .background(fill, shape)
        .border(1.dp, Color.White.copy(alpha = strokeOpacity), shape)
}
