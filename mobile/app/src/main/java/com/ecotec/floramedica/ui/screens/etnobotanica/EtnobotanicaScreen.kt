package com.ecotec.floramedica.ui.screens.etnobotanica

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.ecotec.floramedica.data.asDisplayImageUrl
import com.ecotec.floramedica.data.model.Species
import com.ecotec.floramedica.data.repository.ContentRepository
import com.ecotec.floramedica.ui.components.AnimatedListItem
import com.ecotec.floramedica.ui.components.Pill
import com.ecotec.floramedica.ui.components.SpeciesImage
import com.ecotec.floramedica.ui.theme.Blue600
import com.ecotec.floramedica.ui.theme.Navy900
import com.ecotec.floramedica.ui.theme.SurfaceTint
import com.ecotec.floramedica.ui.theme.TextPrimary
import com.ecotec.floramedica.ui.theme.TextSecondary

// El sitio eliminó la colección separada "etnobotanicacont": esta pantalla se arma directo
// desde species (mismo criterio que Etnobotanicagrid.astro). Cada tarjeta enlaza al detalle
// completo de la especie.
@Composable
fun EtnobotanicaScreen(
    repository: ContentRepository,
    onFichaClick: (String) -> Unit,
) {
    val species by produceState(initialValue = emptyList<Species>(), repository) {
        value = repository.getSpecies()
    }

    LazyColumn(contentPadding = PaddingValues(16.dp)) {
        item {
            Text(
                text = "Fichas etnobotánicas",
                style = MaterialTheme.typography.headlineMedium,
                color = Navy900,
                modifier = Modifier.padding(bottom = 4.dp),
            )
            Text(
                text = "Documentación de usos tradicionales y conocimiento biocultural de cada especie.",
                style = MaterialTheme.typography.bodyLarge,
                color = TextSecondary,
                modifier = Modifier.padding(bottom = 16.dp),
            )
        }
        itemsIndexed(species, key = { _, it -> it.slug }) { index, especie ->
            AnimatedListItem(index = index) {
                FichaCard(especie, onClick = { onFichaClick(especie.slug) })
            }
        }
    }
}

@Composable
private fun FichaCard(especie: Species, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 14.dp),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column {
            Box {
                SpeciesImage(
                    url = especie.multimediaPrincipal.imagenUrl.asDisplayImageUrl(500),
                    contentDescription = especie.nombreComun,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                )
                Pill(
                    text = especie.etnobotanica.clasificacion,
                    containerColor = Color.White,
                    contentColor = Navy900,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(12.dp),
                )
            }
            Column(modifier = Modifier.padding(16.dp)) {
                Text(especie.nombreComun, style = MaterialTheme.typography.titleLarge, color = Navy900)
                Text(
                    especie.nombreCientifico,
                    style = MaterialTheme.typography.bodyMedium,
                    fontStyle = FontStyle.Italic,
                    color = TextSecondary,
                )
                InfoLine("Parte utilizada", especie.etnobotanica.parteUtilizada)
                InfoLine("Uso tradicional", especie.etnobotanica.usoTradicional.ifBlank { "—" })

                if (especie.compuestosQuimicos.isNotEmpty()) {
                    Row(
                        modifier = Modifier.padding(top = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            Icons.Filled.Science,
                            contentDescription = null,
                            tint = Blue600,
                            modifier = Modifier.size(16.dp),
                        )
                        Text(
                            text = especie.compuestosQuimicos.joinToString(" · ") { it.nombre },
                            style = MaterialTheme.typography.bodySmall,
                            color = TextPrimary,
                            modifier = Modifier.padding(start = 6.dp),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoLine(label: String, value: String) {
    Row(modifier = Modifier.padding(top = 6.dp)) {
        Text("$label: ", style = MaterialTheme.typography.labelLarge, color = Navy900)
        Text(value, style = MaterialTheme.typography.bodyMedium, color = TextPrimary)
    }
}
