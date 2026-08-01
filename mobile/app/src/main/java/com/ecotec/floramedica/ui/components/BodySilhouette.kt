package com.ecotec.floramedica.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import com.ecotec.floramedica.data.BodyRegion
import com.ecotec.floramedica.ui.theme.HighlightCoral

/**
 * Silueta humana de frente, dibujada con curvas suaves y relleno en gradiente, que resalta
 * las [regiones] con marcadores numerados que "laten" (animación pulse). La numeración
 * coincide con el orden en que se pasan las regiones, para que la leyenda externa las
 * referencie ("1. Sistema digestivo …").
 */
@Composable
fun BodySilhouette(
    regiones: List<BodyRegion>,
    modifier: Modifier = Modifier,
) {
    val textMeasurer = rememberTextMeasurer()
    val transition = rememberInfiniteTransition(label = "pulse")
    val pulse by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400),
            repeatMode = RepeatMode.Restart,
        ),
        label = "pulseValue",
    )

    Box(modifier = modifier.fillMaxWidth()) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.5f),
        ) {
            drawBody()
            regiones.forEachIndexed { index, region ->
                drawHighlight(
                    center = Offset(region.x * size.width, region.y * size.height),
                    numero = index + 1,
                    pulse = pulse,
                    textMeasurer = textMeasurer,
                )
            }
        }
    }
}

private fun DrawScope.drawBody() {
    val w = size.width
    val h = size.height
    fun px(nx: Float) = nx * w
    fun py(ny: Float) = ny * h

    val bodyGradient = Brush.verticalGradient(
        0f to Color(0xFFD3DCEC),
        1f to Color(0xFFAEBCD6),
    )

    // Cuerpo (torso + cadera) con curvas suaves: hombros -> cintura -> cadera.
    val torso = Path().apply {
        moveTo(px(0.34f), py(0.175f))          // hombro izq
        cubicTo(px(0.30f), py(0.24f), px(0.375f), py(0.36f), px(0.385f), py(0.42f)) // costado izq -> cintura
        cubicTo(px(0.35f), py(0.47f), px(0.35f), py(0.52f), px(0.40f), py(0.55f))   // cadera izq
        lineTo(px(0.60f), py(0.55f))
        cubicTo(px(0.65f), py(0.52f), px(0.65f), py(0.47f), px(0.615f), py(0.42f))  // cadera der
        cubicTo(px(0.625f), py(0.36f), px(0.70f), py(0.24f), px(0.66f), py(0.175f)) // costado der
        close()
    }
    drawPath(torso, bodyGradient)

    // Cabeza
    drawCircle(bodyGradient, radius = w * 0.088f, center = Offset(px(0.5f), py(0.072f)))
    // Cuello
    drawRoundRectN(bodyGradient, 0.46f, 0.12f, 0.54f, 0.175f, w * 0.02f)

    // Hombros redondeados
    drawCircle(bodyGradient, radius = w * 0.055f, center = Offset(px(0.35f), py(0.20f)))
    drawCircle(bodyGradient, radius = w * 0.055f, center = Offset(px(0.65f), py(0.20f)))

    // Brazos (cápsulas ligeramente hacia afuera)
    drawRoundRectN(bodyGradient, 0.205f, 0.195f, 0.30f, 0.53f, w * 0.048f)
    drawRoundRectN(bodyGradient, 0.70f, 0.195f, 0.795f, 0.53f, w * 0.048f)

    // Piernas
    drawRoundRectN(bodyGradient, 0.395f, 0.55f, 0.487f, 0.965f, w * 0.048f)
    drawRoundRectN(bodyGradient, 0.513f, 0.55f, 0.605f, 0.965f, w * 0.048f)

    // Contorno sutil para dar definición
    val outline = Color(0x2214203A)
    drawPath(torso, outline, style = Stroke(width = w * 0.006f))
    drawCircle(outline, radius = w * 0.088f, center = Offset(px(0.5f), py(0.072f)), style = Stroke(width = w * 0.006f))
}

private fun DrawScope.drawRoundRectN(
    brush: Brush,
    nx0: Float, ny0: Float, nx1: Float, ny1: Float,
    corner: Float,
) {
    drawRoundRect(
        brush = brush,
        topLeft = Offset(nx0 * size.width, ny0 * size.height),
        size = Size((nx1 - nx0) * size.width, (ny1 - ny0) * size.height),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(corner, corner),
    )
}

private fun DrawScope.drawHighlight(
    center: Offset,
    numero: Int,
    pulse: Float,
    textMeasurer: TextMeasurer,
) {
    val w = size.width
    val baseR = w * 0.048f

    // Onda expansiva que late y se desvanece
    val ringR = baseR + (w * 0.09f) * pulse
    drawCircle(
        color = HighlightCoral.copy(alpha = (1f - pulse) * 0.45f),
        radius = ringR,
        center = center,
        style = Stroke(width = w * 0.012f),
    )
    // Halo suave fijo
    drawCircle(color = HighlightCoral.copy(alpha = 0.18f), radius = w * 0.088f, center = center)
    // Anillo blanco + punto
    drawCircle(color = Color.White, radius = baseR + w * 0.008f, center = center)
    drawCircle(color = HighlightCoral, radius = baseR, center = center)
    // Número
    val result = textMeasurer.measure(
        text = numero.toString(),
        style = TextStyle(color = Color.White, fontSize = (w * 0.05f).toSp(), fontWeight = FontWeight.Bold),
    )
    drawText(
        textLayoutResult = result,
        topLeft = Offset(center.x - result.size.width / 2f, center.y - result.size.height / 2f),
    )
}
