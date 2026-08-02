package com.ecotec.floramedica.data.model

import kotlinx.serialization.Serializable

/**
 * Contenedor de todo el contenido empaquetado en assets/content.json.
 * La sección de Blog se eliminó (reemplazada por Autores); la clave "blog" que aún
 * pueda venir en el JSON se ignora gracias a ignoreUnknownKeys en el parser.
 */
@Serializable
data class ContentBundle(
    val species: List<Species>,
)
