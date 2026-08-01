package com.ecotec.floramedica.ui.screens.especies

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ecotec.floramedica.data.model.Species
import com.ecotec.floramedica.data.repository.ContentRepository
import com.ecotec.floramedica.ui.components.AnimatedListItem
import com.ecotec.floramedica.ui.components.SpeciesCard
import com.ecotec.floramedica.ui.theme.Navy900

@Composable
fun EspeciesListScreen(
    repository: ContentRepository,
    onEspecieClick: (String) -> Unit,
) {
    val species by produceState(initialValue = emptyList<Species>(), repository) {
        value = repository.getSpecies()
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item(span = { GridItemSpan(2) }) {
            Text(
                text = "Catálogo de especies",
                style = MaterialTheme.typography.headlineMedium,
                color = Navy900,
                modifier = Modifier.padding(bottom = 8.dp),
            )
        }
        itemsIndexed(species, key = { _, it -> it.slug }) { index, especie ->
            AnimatedListItem(index = index) {
                SpeciesCard(species = especie, onClick = { onEspecieClick(especie.slug) })
            }
        }
    }
}
