package com.ecotec.floramedica.ui.screens.autores

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ecotec.floramedica.ui.components.Pill
import com.ecotec.floramedica.ui.theme.Blue600
import com.ecotec.floramedica.ui.theme.Navy900
import com.ecotec.floramedica.ui.theme.TagMint
import com.ecotec.floramedica.ui.theme.TagMintText
import com.ecotec.floramedica.ui.theme.Teal400
import com.ecotec.floramedica.ui.theme.TextSecondary

@Composable
fun AutoresScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .background(Navy900),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Outlined.Groups,
                    contentDescription = null,
                    tint = Teal400,
                    modifier = Modifier.size(52.dp),
                )
            }

            Text(
                text = "Autores",
                style = MaterialTheme.typography.displayMedium,
                color = Navy900,
                modifier = Modifier.padding(top = 24.dp),
            )

            Pill(
                text = "En desarrollo",
                containerColor = TagMint,
                contentColor = TagMintText,
                modifier = Modifier.padding(top = 12.dp),
            )

            Text(
                text = "Esta sección estará disponible próximamente. Aquí conocerás al equipo detrás de Ecotec Flora Médica y su trabajo de investigación.",
                style = MaterialTheme.typography.bodyLarge,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, start = 8.dp, end = 8.dp),
            )

            Text(
                text = "PRÓXIMAMENTE",
                style = MaterialTheme.typography.labelMedium,
                color = Blue600,
                modifier = Modifier.padding(top = 24.dp),
            )
        }
    }
}
