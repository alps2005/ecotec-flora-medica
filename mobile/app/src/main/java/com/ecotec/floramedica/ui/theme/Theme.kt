package com.ecotec.floramedica.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Blue600,
    onPrimary = Surface,
    secondary = Teal400,
    onSecondary = Navy900,
    background = Background,
    onBackground = TextPrimary,
    surface = Surface,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceTint,
    onSurfaceVariant = TextSecondary,
    outline = BorderLight,
)

private val DarkColors = darkColorScheme(
    primary = Teal400,
    onPrimary = Navy900,
    secondary = Blue500,
    onSecondary = Surface,
    background = Navy900,
    onBackground = Background,
    surface = Navy700,
    onSurface = Background,
    surfaceVariant = Navy700,
    onSurfaceVariant = Teal200,
    outline = Navy700,
)

/** Gradiente navy -> teal usado en el banner de estadísticas del home. */
val StatsBannerGradient = Brush.horizontalGradient(listOf(Navy900, Blue600, Teal400))

/** Gradiente sutil de fondo usado en el hero. */
val HeroBackgroundGradient = Brush.verticalGradient(listOf(Color(0xFFEFF3FC), Color(0xFFE7EDFA)))

@Composable
fun EcotecFloraMedicaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = if (darkTheme) DarkColors else LightColors
    MaterialTheme(
        colorScheme = colors,
        typography = EcotecTypography,
        content = content,
    )
}
