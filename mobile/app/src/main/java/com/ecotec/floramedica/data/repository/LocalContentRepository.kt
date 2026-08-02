package com.ecotec.floramedica.data.repository

import android.content.Context
import com.ecotec.floramedica.data.model.ContentBundle
import com.ecotec.floramedica.data.model.Species
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

/**
 * Fase 1: lee el contenido real del sitio (exportado desde las content collections
 * de Astro) empaquetado como `assets/content.json`. Funciona 100% offline.
 */
class LocalContentRepository(private val context: Context) : ContentRepository {

    private val json = Json { ignoreUnknownKeys = true }
    private val mutex = Mutex()
    private var cache: ContentBundle? = null

    private suspend fun bundle(): ContentBundle {
        cache?.let { return it }
        return mutex.withLock {
            cache ?: withContext(Dispatchers.IO) {
                val text = context.assets.open("content.json").bufferedReader().use { it.readText() }
                json.decodeFromString<ContentBundle>(text).also { cache = it }
            }
        }
    }

    // El sitio real solo muestra especies con estado ACTIVO (getCollection('species',
    // ({data}) => data.estado === 'ACTIVO')) en catálogo, home y etnobotánica. Se filtra
    // acá una sola vez para que todas las pantallas queden consistentes con el sitio.
    override suspend fun getSpecies(): List<Species> =
        bundle().species.filter { it.estado == "ACTIVO" }

    override suspend fun getSpeciesBySlug(slug: String): Species? =
        bundle().species.firstOrNull { it.slug == slug }

    override suspend fun getStats(): CatalogStats {
        val activeSpecies = getSpecies()
        return CatalogStats(
            totalEspecies = activeSpecies.size,
            totalFamilias = activeSpecies.map { it.taxonomia.familia }.distinct().size,
            // El sitio ya no tiene una colección de "fichas etnobotánicas" separada:
            // la sección de Etnobotánica se deriva de species, así que el conteo es el mismo.
            totalFichasEtnobotanicas = activeSpecies.size,
            totalCompuestos = activeSpecies.sumOf { it.compuestosQuimicos.size },
        )
    }
}
