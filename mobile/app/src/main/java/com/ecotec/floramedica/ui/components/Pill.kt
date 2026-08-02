package com.ecotec.floramedica.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ecotec.floramedica.ui.theme.PillBackground
import com.ecotec.floramedica.ui.theme.PillTextOnLight

@Composable
fun Pill(
    text: String,
    modifier: Modifier = Modifier,
    containerColor: Color = PillBackground,
    contentColor: Color = PillTextOnLight,
) {
    Text(
        text = text.uppercase(),
        style = MaterialTheme.typography.labelSmall,
        color = contentColor,
        modifier = modifier
            .background(containerColor, RoundedCornerShape(50))
            .padding(horizontal = 12.dp, vertical = 6.dp),
    )
}
