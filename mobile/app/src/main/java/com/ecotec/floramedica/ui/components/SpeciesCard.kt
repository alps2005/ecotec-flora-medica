package com.ecotec.floramedica.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ecotec.floramedica.data.asDisplayImageUrl
import com.ecotec.floramedica.data.model.Species
import com.ecotec.floramedica.ui.theme.TextSecondary

@Composable
fun SpeciesCard(
    species: Species,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column {
            SpeciesImage(
                url = species.multimediaPrincipal.imagenUrl.asDisplayImageUrl(500),
                contentDescription = species.nombreComun,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
            )
            Column(modifier = Modifier.padding(12.dp)) {
                Pill(text = species.taxonomia.familia)
                Text(
                    text = species.nombreComun,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 8.dp),
                )
                Text(
                    text = species.nombreCientifico,
                    style = MaterialTheme.typography.bodyMedium,
                    fontStyle = FontStyle.Italic,
                    color = TextSecondary,
                )
                Text(
                    text = species.etnobotanica.usoTradicional,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    modifier = Modifier.padding(top = 4.dp),
                    fontSize = 12.sp,
                )
            }
        }
    }
}
