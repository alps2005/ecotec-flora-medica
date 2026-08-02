package com.ecotec.floramedica.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFlorist
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.ecotec.floramedica.ui.theme.BodyFill
import com.ecotec.floramedica.ui.theme.SurfaceTint
import com.ecotec.floramedica.ui.theme.TextMuted

/**
 * Carga de imágenes resiliente para especies. Muestra un indicador mientras carga y, si la URL
 * falla (servidores con hotlink protection, timeouts o enlaces rotos del contenido), cae en un
 * placeholder con marca (icono de hoja) en vez de dejar un espacio en blanco.
 * crossfade habilitado para una transición suave.
 */
@Composable
fun SpeciesImage(
    url: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
) {
    SubcomposeAsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .crossfade(true)
            .build(),
        contentDescription = contentDescription,
        contentScale = contentScale,
        modifier = modifier,
        loading = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(SurfaceTint),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(strokeWidth = 2.dp, modifier = Modifier.size(24.dp))
            }
        },
        error = { Placeholder() },
    )
}

@Composable
private fun Placeholder() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BodyFill),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.Filled.LocalFlorist,
            contentDescription = null,
            tint = TextMuted,
            modifier = Modifier.size(40.dp),
        )
    }
}
