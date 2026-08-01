package com.ecotec.floramedica.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.ecotec.floramedica.data.BodyRegionMatch
import com.ecotec.floramedica.ui.theme.HighlightCoral
import com.ecotec.floramedica.ui.theme.Navy900
import com.ecotec.floramedica.ui.theme.TextPrimary
import com.ecotec.floramedica.ui.theme.TextSecondary

@Composable
fun BodyRegionsDialog(
    nombreComun: String,
    matches: List<BodyRegionMatch>,
    onDismiss: () -> Unit,
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
            ) {
                Text(
                    text = "¿Para qué parte del cuerpo sirve?",
                    style = MaterialTheme.typography.titleLarge,
                    color = Navy900,
                )
                Text(
                    text = nombreComun,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    modifier = Modifier.padding(top = 2.dp, bottom = 12.dp),
                )

                BodySilhouette(
                    regiones = matches.map { it.region },
                    modifier = Modifier.padding(horizontal = 40.dp),
                )

                Column(modifier = Modifier.padding(top = 16.dp)) {
                    matches.forEachIndexed { index, match ->
                        LegendRow(numero = index + 1, etiqueta = match.region.etiqueta, descripcion = match.region.descripcion)
                    }
                    if (matches.isEmpty()) {
                        Text(
                            text = "Esta especie tiene un uso general; no se identificó una zona específica del cuerpo.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }

                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(top = 8.dp),
                ) {
                    Text("Cerrar")
                }
            }
        }
    }
}

@Composable
private fun LegendRow(numero: Int, etiqueta: String, descripcion: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.Top,
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .background(HighlightCoral, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = numero.toString(),
                style = MaterialTheme.typography.labelMedium,
                color = androidx.compose.ui.graphics.Color.White,
            )
        }
        Column(modifier = Modifier.padding(start = 12.dp)) {
            Text(etiqueta, style = MaterialTheme.typography.titleMedium, color = TextPrimary)
            Text(descripcion, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
        }
    }
}
