package com.ecotec.floramedica.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ecotec.floramedica.data.BodyRegion
import com.ecotec.floramedica.ui.theme.HighlightCoral
import com.ecotec.floramedica.ui.theme.HighlightCoralSoft

/**
 * Silueta humana de frente que resalta las [regiones] indicadas con marcadores numerados.
 * La numeración coincide con el orden en que se pasan las regiones, para que la leyenda
 * externa pueda referenciarlas ("1. Sistema digestivo …").
 */
@Composable
fun BodySilhouette(
    regiones: List<BodyRegion>,
    modifier: Modifier = Modifier,
    bodyColor: Color = Color(0xFFCBD5E8),
) {
    val textMeasurer = rememberTextMeasurer()
    Box(modifier = modifier.fillMaxWidth()) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.52f),
        ) {
            drawBody(bodyColor)
            regiones.forEachIndexed { index, region ->
                drawHighlight(
                    center = Offset(region.x * size.width, region.y * size.height),
                    numero = index + 1,
                    textMeasurer = textMeasurer,
                )
            }
        }
    }
}

private fun DrawScope.drawBody(bodyColor: Color) {
    val w = size.width
    val h = size.height
    fun p(nx: Float, ny: Float) = Offset(nx * w, ny * h)

    // Cabeza
    drawCircle(color = bodyColor, radius = w * 0.085f, center = p(0.5f, 0.075f))
    // Cuello
    drawRoundRectN(bodyColor, 0.455f, 0.125f, 0.545f, 0.175f, w * 0.02f)

    // Torso (hombros -> cintura -> cadera)
    val torso = Path().apply {
        moveTo(w * 0.30f, h * 0.185f)
        lineTo(w * 0.70f, h * 0.185f)
        lineTo(w * 0.615f, h * 0.44f)
        lineTo(w * 0.64f, h * 0.55f)
        lineTo(w * 0.36f, h * 0.55f)
        lineTo(w * 0.385f, h * 0.44f)
        close()
    }
    drawPath(torso, bodyColor)

    // Brazos
    drawRoundRectN(bodyColor, 0.205f, 0.195f, 0.305f, 0.545f, w * 0.05f)
    drawRoundRectN(bodyColor, 0.695f, 0.195f, 0.795f, 0.545f, w * 0.05f)

    // Piernas
    drawRoundRectN(bodyColor, 0.385f, 0.545f, 0.485f, 0.965f, w * 0.05f)
    drawRoundRectN(bodyColor, 0.515f, 0.545f, 0.615f, 0.965f, w * 0.05f)
}

private fun DrawScope.drawRoundRectN(
    color: Color,
    nx0: Float, ny0: Float, nx1: Float, ny1: Float,
    corner: Float,
) {
    drawRoundRect(
        color = color,
        topLeft = Offset(nx0 * size.width, ny0 * size.height),
        size = Size((nx1 - nx0) * size.width, (ny1 - ny0) * size.height),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(corner, corner),
    )
}

private fun DrawScope.drawHighlight(
    center: Offset,
    numero: Int,
    textMeasurer: TextMeasurer,
) {
    val w = size.width
    // Halo suave
    drawCircle(color = HighlightCoralSoft, radius = w * 0.10f, center = center)
    // Anillo blanco
    drawCircle(color = Color.White, radius = w * 0.052f, center = center)
    // Punto principal
    drawCircle(color = HighlightCoral, radius = w * 0.045f, center = center)
    // Número (px -> sp usando la densidad del DrawScope, que implementa Density)
    val result = textMeasurer.measure(
        text = numero.toString(),
        style = TextStyle(color = Color.White, fontSize = (w * 0.05f).toSp(), fontWeight = FontWeight.Bold),
    )
    drawText(
        textLayoutResult = result,
        topLeft = Offset(center.x - result.size.width / 2f, center.y - result.size.height / 2f),
    )
}
