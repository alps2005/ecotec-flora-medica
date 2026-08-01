package com.ecotec.floramedica.data

private val wikimediaOriginalRegex =
    Regex("""^https://upload\.wikimedia\.org/wikipedia/commons/([0-9a-f])/([0-9a-f]{2})/([^/]+)$""")

// Wikimedia solo genera thumbnails on-the-fly para un allow-list fijo de anchos (no acepta
// cualquier valor, responde 400 fuera de esta lista). Verificado empíricamente contra su CDN.
private val allowedThumbWidths = listOf(120, 250, 500)

/**
 * Wikimedia Commons sirve el archivo "original" en resolución completa (a veces varios MB,
 * ej. 7.3MB para una sola foto de especie), lo cual es lento e innecesario para una tarjeta o
 * thumbnail móvil. Se reescribe a la convención de thumbnail de Wikimedia:
 * /commons/thumb/<a>/<ab>/<file>/<widthPx>px-<file>, ajustando al ancho permitido más cercano.
 * Si la URL no es de Wikimedia (Cloudinary, iNaturalist, etc.) se devuelve tal cual, esos CDNs
 * ya sirven tamaños razonables por su cuenta.
 */
fun String.asDisplayImageUrl(desiredWidthPx: Int = 250): String {
    val match = wikimediaOriginalRegex.find(this) ?: return this
    val (a, ab, file) = match.destructured
    val width = allowedThumbWidths.minByOrNull { kotlin.math.abs(it - desiredWidthPx) } ?: 250
    return "https://upload.wikimedia.org/wikipedia/commons/thumb/$a/$ab/$file/${width}px-$file"
}
