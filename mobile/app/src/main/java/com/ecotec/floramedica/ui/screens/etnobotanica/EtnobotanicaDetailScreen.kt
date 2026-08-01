package com.ecotec.floramedica.ui.screens.etnobotanica

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.ecotec.floramedica.data.asDisplayImageUrl
import com.ecotec.floramedica.data.inferBodyRegions
import com.ecotec.floramedica.data.model.Species
import com.ecotec.floramedica.data.repository.ContentRepository
import com.ecotec.floramedica.ui.components.AnimatedSection
import com.ecotec.floramedica.ui.components.BodySilhouette
import com.ecotec.floramedica.ui.components.Pill
import com.ecotec.floramedica.ui.components.SpeciesImage
import com.ecotec.floramedica.ui.theme.Blue600
import com.ecotec.floramedica.ui.theme.HighlightCoral
import com.ecotec.floramedica.ui.theme.Navy900
import com.ecotec.floramedica.ui.theme.SurfaceMint
import com.ecotec.floramedica.ui.theme.SurfaceTint
import com.ecotec.floramedica.ui.theme.TagMint
import com.ecotec.floramedica.ui.theme.TagMintText
import com.ecotec.floramedica.ui.theme.Teal400
import com.ecotec.floramedica.ui.theme.TextPrimary
import com.ecotec.floramedica.ui.theme.TextSecondary

/**
 * Detalle ENFOCADO EN ETNOBOTÁNICA: usos tradicionales, para qué parte del cuerpo sirve
 * (silueta embebida inline), parte utilizada, perfil cultural, historia/dispersión y
 * compuestos. Deliberadamente distinto al detalle de Especies (que muestra taxonomía y
 * comercio con enfoque científico).
 */
@Composable
fun EtnobotanicaDetailScreen(
    slug: String,
    repository: ContentRepository,
    onBack: () -> Unit,
) {
    val especie by produceState<Species?>(initialValue = null, slug, repository) {
        value = repository.getSpeciesBySlug(slug)
    }
    val current = especie ?: return
    val bodyMatches = remember(current.slug) { inferBodyRegions(current) }

    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp, top = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Navy900)
                }
                Text("Volver a etnobotánica", style = MaterialTheme.typography.bodyLarge, color = Navy900)
            }
        }
        item { Hero(current) }

        // Chips de uso (lo primero, enfoque etnobotánico)
        item {
            AnimatedSection {
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
                    UsoChip("Categoría", current.etnobotanica.clasificacion)
                    UsoChip("Parte utilizada", current.etnobotanica.parteUtilizada)
                    UsoChip("Uso tradicional", current.etnobotanica.usoTradicional.ifBlank { "—" })
                }
            }
        }

        // Silueta EMBEBIDA (estrella de esta pantalla)
        item {
            AnimatedSection(delayMillis = 80) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceMint),
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            "¿Para qué parte del cuerpo sirve?",
                            style = MaterialTheme.typography.titleLarge,
                            color = Navy900,
                        )
                        Text(
                            "Zonas del cuerpo asociadas a sus usos tradicionales.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary,
                            modifier = Modifier.padding(top = 2.dp, bottom = 8.dp),
                        )
                        BodySilhouette(
                            regiones = bodyMatches.map { it.region },
                            modifier = Modifier.padding(horizontal = 48.dp, vertical = 8.dp),
                        )
                        Column(modifier = Modifier.padding(top = 8.dp)) {
                            bodyMatches.forEachIndexed { index, match ->
                                LegendRow(index + 1, match.region.etiqueta, match.region.descripcion)
                            }
                        }
                    }
                }
            }
        }

        // Perfil etnobotánico (cultural)
        item {
            AnimatedSection {
                SectionBlock(titulo = "Perfil etnobotánico", texto = current.perfilEtnobotanico)
            }
        }
        // Historia y contexto cultural
        item {
            AnimatedSection {
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
                    SectionTitle("Origen y saber ancestral")
                    Paragraph(current.historiaEvolucion.origen)
                    Paragraph(current.historiaEvolucion.dispersion)
                }
            }
        }
        // Compuestos (chips compactos)
        if (current.compuestosQuimicos.isNotEmpty()) {
            item {
                AnimatedSection {
                    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
                        SectionTitle("Compuestos activos")
                        current.compuestosQuimicos.forEach { c ->
                            Row(modifier = Modifier.padding(top = 8.dp), verticalAlignment = Alignment.Top) {
                                Icon(Icons.Filled.Science, contentDescription = null, tint = Blue600, modifier = Modifier.size(18.dp))
                                Column(modifier = Modifier.padding(start = 10.dp)) {
                                    Text(c.nombre, style = MaterialTheme.typography.titleMedium, color = Navy900)
                                    Text(c.detalle, style = MaterialTheme.typography.bodyMedium, color = TextPrimary)
                                }
                            }
                        }
                    }
                }
            }
        }
        item { Box(modifier = Modifier.padding(bottom = 12.dp)) {} }
    }
}

@Composable
private fun Hero(especie: Species) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .aspectRatio(1.05f)
            .clip(RoundedCornerShape(24.dp)),
    ) {
        SpeciesImage(
            url = especie.multimediaPrincipal.imagenUrl.asDisplayImageUrl(500),
            contentDescription = especie.nombreComun,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(0.4f to Color.Transparent, 1f to Navy900.copy(alpha = 0.92f))),
        )
        Column(modifier = Modifier.align(Alignment.BottomStart).padding(20.dp)) {
            Pill(text = especie.etnobotanica.clasificacion, containerColor = Color.White, contentColor = Navy900)
            Text(
                especie.nombreComun,
                style = MaterialTheme.typography.displayMedium,
                color = Color.White,
                modifier = Modifier.padding(top = 12.dp),
            )
            Text(
                especie.nombreCientifico,
                style = MaterialTheme.typography.titleMedium,
                color = Teal400,
                fontStyle = FontStyle.Italic,
            )
        }
    }
}

@Composable
private fun UsoChip(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceTint)
            .padding(horizontal = 14.dp, vertical = 10.dp),
    ) {
        Text("$label:  ", style = MaterialTheme.typography.labelLarge, color = Blue600)
        Text(value, style = MaterialTheme.typography.bodyLarge, color = TextPrimary)
    }
}

@Composable
private fun LegendRow(numero: Int, etiqueta: String, descripcion: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp), verticalAlignment = Alignment.Top) {
        Box(
            modifier = Modifier.size(26.dp).background(HighlightCoral, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Text(numero.toString(), style = MaterialTheme.typography.labelMedium, color = Color.White)
        }
        Column(modifier = Modifier.padding(start = 12.dp)) {
            Text(etiqueta, style = MaterialTheme.typography.titleMedium, color = Navy900)
            Text(descripcion, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
        }
    }
}

@Composable
private fun SectionBlock(titulo: String, texto: String) {
    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
        SectionTitle(titulo)
        Paragraph(texto)
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.headlineMedium,
        color = Navy900,
        modifier = Modifier.padding(bottom = 8.dp),
    )
}

@Composable
private fun Paragraph(text: String) {
    if (text.isBlank()) return
    Text(
        text = text,
        style = MaterialTheme.typography.bodyLarge,
        color = TextPrimary,
        modifier = Modifier.padding(bottom = 6.dp),
    )
}
