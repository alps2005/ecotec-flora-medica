package com.ecotec.floramedica.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

/**
 * Envuelve un item de lista para que aparezca con un fade + slide-up sutil al entrar.
 * El [index] genera un pequeño escalonado (stagger) para un efecto en cascada.
 */
@Composable
fun AnimatedListItem(
    index: Int,
    content: @Composable () -> Unit,
) {
    val progress = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        val delay = (index.coerceAtMost(8) * 55).toLong()
        kotlinx.coroutines.delay(delay)
        progress.animateTo(1f, animationSpec = tween(360))
    }
    androidx.compose.foundation.layout.Box(
        modifier = Modifier
            .alpha(progress.value)
            .offset { IntOffset(x = 0, y = ((1f - progress.value) * 28.dp.toPx()).toInt()) },
    ) {
        content()
    }
}

/** Aparición simple con fade + slide para secciones (sin stagger). */
@Composable
fun AnimatedSection(
    delayMillis: Long = 0,
    content: @Composable () -> Unit,
) {
    val progress = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(delayMillis)
        progress.animateTo(1f, animationSpec = tween(420))
    }
    androidx.compose.foundation.layout.Box(
        modifier = Modifier
            .alpha(progress.value)
            .offset { IntOffset(x = 0, y = ((1f - progress.value) * 24.dp.toPx()).toInt()) },
    ) {
        content()
    }
}
