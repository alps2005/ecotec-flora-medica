package com.ecotec.floramedica.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.Yard
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.ecotec.floramedica.data.model.Species
import com.ecotec.floramedica.data.repository.CatalogStats
import com.ecotec.floramedica.data.repository.ContentRepository
import com.ecotec.floramedica.ui.components.SpeciesCard
import com.ecotec.floramedica.ui.components.StatsBanner
import com.ecotec.floramedica.ui.theme.Blue600
import com.ecotec.floramedica.ui.theme.Navy900
import com.ecotec.floramedica.ui.theme.SurfaceMint
import com.ecotec.floramedica.ui.theme.SurfaceTint
import com.ecotec.floramedica.ui.theme.TextSecondary

private data class Pilar(val titulo: String, val descripcion: String, val icono: ImageVector, val fondo: androidx.compose.ui.graphics.Color)

@Composable
fun HomeScreen(
    repository: ContentRepository,
    onVerEspecies: () -> Unit,
    onVerEtnobotanica: () -> Unit,
    onEspecieClick: (String) -> Unit,
) {
    val species by produceState(initialValue = emptyList<Species>(), repository) {
        value = repository.getSpecies()
    }
    val stats by produceState(initialValue = CatalogStats(0, 0, 0, 0), repository) {
        value = repository.getStats()
    }

    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        item { HeroSection(onVerEspecies = onVerEspecies, onVerEtnobotanica = onVerEtnobotanica) }
        item {
            if (species.isNotEmpty()) {
                Text(
                    text = "Especies destacadas",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(start = 20.dp, top = 24.dp, bottom = 12.dp),
                )
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    items(species.take(6)) { especie ->
                        SpeciesCard(
                            species = especie,
                            onClick = { onEspecieClick(especie.slug) },
                            modifier = Modifier.width(200.dp),
                        )
                    }
                }
            }
        }
        item { StatsBanner(stats = stats, modifier = Modifier.padding(top = 24.dp)) }
        item { MarcoDeEstudioSection() }
    }
}

@Composable
private fun HeroSection(onVerEspecies: () -> Unit, onVerEtnobotanica: () -> Unit) {
    Column(modifier = Modifier.padding(20.dp)) {
        Text(
            text = "HERBARIUM DIGITAL",
            style = MaterialTheme.typography.labelMedium,
            color = Navy900,
        )
        Text(
            text = "El saber ancestral",
            style = MaterialTheme.typography.displayLarge,
            color = Navy900,
            modifier = Modifier.padding(top = 8.dp),
        )
        Text(
            text = "en la lente científica.",
            style = MaterialTheme.typography.displayLarge,
            color = Blue600,
            fontStyle = FontStyle.Italic,
        )
        Text(
            text = "Un compendio riguroso dedicado a la preservación del conocimiento botánico y la investigación fitoquímica. Explore la taxonomía de especies medicinales bajo un marco de validación empírica y respeto cultural.",
            style = MaterialTheme.typography.bodyLarge,
            color = TextSecondary,
            modifier = Modifier.padding(top = 16.dp),
        )
        Text(
            text = "Encuentra especies, contexto etnobotánico y lecturas claras para entender la relación entre plantas, salud y territorio.",
            style = MaterialTheme.typography.bodyLarge,
            color = TextSecondary,
            modifier = Modifier.padding(top = 12.dp),
        )
        Row(modifier = Modifier.padding(top = 20.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = onVerEspecies, shape = RoundedCornerShape(50)) {
                Text("Explorar especies")
            }
            OutlinedButton(onClick = onVerEtnobotanica, shape = RoundedCornerShape(50)) {
                Text("Ver etnobotánica")
            }
        }
    }
}

private val pilares = listOf(
    Pilar(
        "Taxonomía",
        "Sistematización morfológica y genética para una identificación inequívoca de cada especie.",
        Icons.Filled.Science,
        SurfaceTint,
    ),
    Pilar(
        "Etnobotánica",
        "Documentación de usos tradicionales y conocimiento biocultural de comunidades originarias.",
        Icons.Filled.Yard,
        SurfaceMint,
    ),
    Pilar(
        "Fitoquímica",
        "Análisis de compuestos bioactivos, metabolitos secundarios y sus mecanismos de acción.",
        Icons.Filled.WaterDrop,
        SurfaceTint,
    ),
    Pilar(
        "Sostenibilidad",
        "Modelos de aprovechamiento responsable que garantizan la regeneración de los ecosistemas.",
        Icons.Filled.Spa,
        SurfaceMint,
    ),
)

@Composable
private fun MarcoDeEstudioSection() {
    Column(modifier = Modifier.padding(20.dp)) {
        Text(
            text = "Nuestro Marco de Estudio",
            style = MaterialTheme.typography.headlineLarge,
            color = Navy900,
            fontStyle = FontStyle.Italic,
            modifier = Modifier.padding(bottom = 16.dp),
        )
        pilares.forEach { pilar ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                colors = CardDefaults.cardColors(containerColor = pilar.fondo),
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Icon(pilar.icono, contentDescription = null, tint = Blue600)
                    Text(
                        text = pilar.titulo,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(top = 12.dp, bottom = 6.dp),
                    )
                    Text(
                        text = pilar.descripcion,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary,
                    )
                }
            }
        }
    }
}
