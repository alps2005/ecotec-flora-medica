package com.ecotec.floramedica.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ecotec.floramedica.data.repository.CatalogStats
import com.ecotec.floramedica.ui.theme.StatsBannerGradient

@Composable
fun StatsBanner(stats: CatalogStats, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(StatsBannerGradient)
            .padding(vertical = 24.dp, horizontal = 12.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        StatItem(stats.totalEspecies.toString(), "Especies registradas")
        StatItem(stats.totalFamilias.toString(), "Familias botánicas")
        StatItem(stats.totalFichasEtnobotanicas.toString(), "Fichas etnobotánicas")
        StatItem(stats.totalCompuestos.toString(), "Compuestos documentados")
    }
}

@Composable
private fun StatItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.displayMedium,
            color = Color.White,
            fontWeight = FontWeight.Normal,
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = Color.White.copy(alpha = 0.85f),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
        )
    }
}
