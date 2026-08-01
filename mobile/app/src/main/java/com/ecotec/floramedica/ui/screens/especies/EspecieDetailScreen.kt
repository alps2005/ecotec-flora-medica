package com.ecotec.floramedica.ui.screens.especies

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.ecotec.floramedica.ui.components.BodyRegionsDialog
import com.ecotec.floramedica.ui.components.Pill
import com.ecotec.floramedica.ui.components.SpeciesImage
import com.ecotec.floramedica.ui.theme.Blue600
import com.ecotec.floramedica.ui.theme.Navy900
import com.ecotec.floramedica.ui.theme.SurfaceMint
import com.ecotec.floramedica.ui.theme.SurfaceTint
import com.ecotec.floramedica.ui.theme.TagMint
import com.ecotec.floramedica.ui.theme.TagMintText
import com.ecotec.floramedica.ui.theme.Teal400
import com.ecotec.floramedica.ui.theme.TextPrimary
import com.ecotec.floramedica.ui.theme.TextSecondary

@Composable
fun EspecieDetailScreen(
    slug: String,
    repository: ContentRepository,
    onBack: () -> Unit,
) {
    val especie by produceState<Species?>(initialValue = null, slug, repository) {
        value = repository.getSpeciesBySlug(slug)
    }
    val current = especie ?: return

    var showBodyDialog by remember { mutableStateOf(false) }
    val bodyMatches = remember(current.slug) { inferBodyRegions(current) }

    if (showBodyDialog) {
        BodyRegionsDialog(
            nombreComun = current.nombreComun,
            matches = bodyMatches,
            onDismiss = { showBodyDialog = false },
        )
    }

    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp, top = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver al catálogo", tint = Navy900)
                }
                Text("Volver al catálogo", style = MaterialTheme.typography.bodyLarge, color = Navy900)
            }
        }
        item { HeroImage(current) }
        item { TaxonomyStrip(current) }
        item { AnimatedSection { UsoCorporalCard(onClick = { showBodyDialog = true }) } }
        item {
            AnimatedSection {
                NumberedSection(numero = "01", titulo = "Perfil etnobotánico") {
                    InfoChipsRow(current)
                    BodyText(current.perfilEtnobotanico, modifier = Modifier.padding(top = 12.dp))
                }
            }
        }
        item {
            AnimatedSection {
                NumberedSection(numero = "02", titulo = "Historia y evolución") {
                    InfoBlock("Origen", current.historiaEvolucion.origen)
                    InfoBlock("Dispersión", current.historiaEvolucion.dispersion)
                    InfoBlock("Evolución", current.historiaEvolucion.evolucion)
                }
            }
        }
        if (current.compuestosQuimicos.isNotEmpty()) {
            item {
                AnimatedSection {
                    NumberedSection(numero = "03", titulo = "Compuestos químicos") {
                        current.compuestosQuimicos.forEach { compuesto ->
                            CompoundCard(nombre = compuesto.nombre, detalle = compuesto.detalle)
                        }
                    }
                }
            }
        }
        if (current.comercio.exportacion.isNotEmpty() || current.comercio.importacion.isNotEmpty()) {
            item {
                AnimatedSection {
                    NumberedSection(numero = "04", titulo = "Comercio") {
                        if (current.comercio.exportacion.isNotEmpty()) {
                            SubHeader("Exportación")
                            current.comercio.exportacion.forEach { InfoBlock(it.pais, it.detalle) }
                        }
                        if (current.comercio.importacion.isNotEmpty()) {
                            SubHeader("Importación")
                            current.comercio.importacion.forEach { InfoBlock(it.pais, it.detalle) }
                        }
                    }
                }
            }
        }
        if (current.descripcion.isNotBlank()) {
            item {
                AnimatedSection {
                    Column(modifier = Modifier.padding(20.dp)) {
                        BodyText(current.descripcion)
                    }
                }
            }
        }
    }
}

@Composable
private fun HeroImage(especie: Species) {
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
                .background(
                    Brush.verticalGradient(
                        0.45f to Color.Transparent,
                        1f to Navy900.copy(alpha = 0.92f),
                    ),
                ),
        )
        Column(modifier = Modifier.align(Alignment.BottomStart).padding(20.dp)) {
            Pill(text = "Familia: ${especie.taxonomia.familia}", containerColor = Color.White, contentColor = Navy900)
            Text(
                text = especie.nombreComun,
                style = MaterialTheme.typography.displayMedium,
                color = Color.White,
                modifier = Modifier.padding(top = 12.dp),
            )
            Text(
                text = especie.nombreCientifico,
                style = MaterialTheme.typography.titleMedium,
                color = Teal400,
                fontStyle = FontStyle.Italic,
            )
        }
    }
}

@Composable
private fun TaxonomyStrip(especie: Species) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Navy900),
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text("CLASIFICACIÓN TAXONÓMICA", style = MaterialTheme.typography.labelMedium, color = Teal400)
            // Filas full-width (rango a la izquierda, valor a la derecha): no se corta ni se
            // amontona aunque el valor sea largo como "Magnoliophyta".
            TaxonomyRow("Reino", especie.taxonomia.reino)
            TaxonomyRow("División", especie.taxonomia.division)
            TaxonomyRow("Clase", especie.taxonomia.clase)
            TaxonomyRow("Familia", especie.taxonomia.familia)
            TaxonomyRow("Género", especie.taxonomia.genero)
        }
    }
}

@Composable
private fun TaxonomyRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            label.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = Teal400,
            modifier = Modifier.weight(0.4f),
        )
        Text(
            value,
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            modifier = Modifier.weight(0.6f),
        )
    }
}

@Composable
private fun UsoCorporalCard(onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceMint),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(Icons.Filled.Accessibility, contentDescription = null, tint = Blue600)
            Column(modifier = Modifier.weight(1f).padding(start = 12.dp)) {
                Text("¿Para qué parte del cuerpo sirve?", style = MaterialTheme.typography.titleMedium, color = Navy900)
                Text(
                    "Míralo en una silueta del cuerpo humano.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                )
            }
            Button(
                onClick = onClick,
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = Blue600),
            ) {
                Text("Ver")
            }
        }
    }
}

@Composable
private fun NumberedSection(numero: String, titulo: String, content: @Composable () -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
        Text(
            text = "$numero / ${titulo.uppercase()}",
            style = MaterialTheme.typography.labelMedium,
            color = Blue600,
        )
        Text(
            text = titulo,
            style = MaterialTheme.typography.headlineMedium,
            color = Navy900,
            modifier = Modifier.padding(top = 4.dp, bottom = 12.dp),
        )
        content()
    }
}

@Composable
private fun InfoChipsRow(especie: Species) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        LabeledValue("Clasificación", especie.etnobotanica.clasificacion)
        LabeledValue("Parte utilizada", especie.etnobotanica.parteUtilizada)
        LabeledValue("Uso tradicional", especie.etnobotanica.usoTradicional.ifBlank { "—" })
    }
}

@Composable
private fun LabeledValue(label: String, value: String) {
    Row {
        Text(
            text = "$label: ",
            style = MaterialTheme.typography.labelLarge,
            color = Navy900,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = TextPrimary,
        )
    }
}

@Composable
private fun CompoundCard(nombre: String, detalle: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceTint),
    ) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.Top) {
            Icon(Icons.Filled.Science, contentDescription = null, tint = Blue600)
            Column(modifier = Modifier.padding(start = 12.dp)) {
                Text(nombre, style = MaterialTheme.typography.titleMedium, color = Navy900)
                Text(
                    detalle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextPrimary,
                    modifier = Modifier.padding(top = 2.dp),
                )
            }
        }
    }
}

@Composable
private fun SubHeader(text: String) {
    Pill(
        text = text,
        containerColor = TagMint,
        contentColor = TagMintText,
        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp),
    )
}

@Composable
private fun InfoBlock(titulo: String, texto: String) {
    Column(modifier = Modifier.padding(top = 10.dp)) {
        Text(titulo, style = MaterialTheme.typography.labelLarge, color = Navy900)
        BodyText(texto, modifier = Modifier.padding(top = 2.dp))
    }
}

@Composable
private fun BodyText(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyLarge,
        color = TextPrimary,
        modifier = modifier,
    )
}
