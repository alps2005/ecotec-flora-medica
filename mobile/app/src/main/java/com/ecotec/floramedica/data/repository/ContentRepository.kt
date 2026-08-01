package com.ecotec.floramedica.data.repository

import com.ecotec.floramedica.data.model.Species

data class CatalogStats(
    val totalEspecies: Int,
    val totalFamilias: Int,
    val totalFichasEtnobotanicas: Int,
    val totalCompuestos: Int,
)

/**
 * Fuente de verdad para el contenido de la app. En Fase 1 la implementación es
 * [LocalContentRepository] (JSON empaquetado en assets). En Fase 2, cuando el
 * backend tenga una URL estable, se agrega una RemoteContentRepository que
 * implemente esta misma interfaz y se cambia el binding en un solo lugar
 * (ver MainActivity) sin tocar las pantallas.
 */
interface ContentRepository {
    suspend fun getSpecies(): List<Species>
    suspend fun getSpeciesBySlug(slug: String): Species?
    suspend fun getStats(): CatalogStats
}
