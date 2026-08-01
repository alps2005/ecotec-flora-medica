package com.ecotec.floramedica.data

import com.ecotec.floramedica.data.model.Species

/**
 * Regiones del cuerpo humano que una especie puede beneficiar. Cada región tiene una
 * posición normalizada (x, y en 0..1 sobre el lienzo de la silueta) usada para dibujar
 * el punto resaltado en [com.ecotec.floramedica.ui.components.BodySilhouette].
 */
enum class BodyRegion(
    val etiqueta: String,
    val descripcion: String,
    val x: Float,
    val y: Float,
) {
    CABEZA(
        etiqueta = "Cabeza y sistema nervioso",
        descripcion = "Efecto sedante, relajante o sobre el sistema nervioso.",
        x = 0.50f, y = 0.075f,
    ),
    RESPIRATORIO(
        etiqueta = "Vías respiratorias",
        descripcion = "Acción sobre garganta, bronquios y pulmones.",
        x = 0.50f, y = 0.30f,
    ),
    CORAZON(
        etiqueta = "Corazón y circulación",
        descripcion = "Efecto cardioprotector o sobre la circulación.",
        x = 0.415f, y = 0.335f,
    ),
    DIGESTIVO(
        etiqueta = "Sistema digestivo",
        descripcion = "Favorece la digestión, el estómago y el hígado.",
        x = 0.50f, y = 0.44f,
    ),
    URINARIO(
        etiqueta = "Riñones y vías urinarias",
        descripcion = "Acción diurética, depurativa o sobre la próstata.",
        x = 0.50f, y = 0.52f,
    ),
    ARTICULACIONES(
        etiqueta = "Músculos y articulaciones",
        descripcion = "Alivio antiinflamatorio de músculos y articulaciones.",
        x = 0.665f, y = 0.72f,
    ),
    PIEL(
        etiqueta = "Piel y heridas",
        descripcion = "Cicatrizante y protector de la piel.",
        x = 0.335f, y = 0.60f,
    ),
    DEFENSAS(
        etiqueta = "Defensas del organismo",
        descripcion = "Acción antimicrobiana, antioxidante e inmunológica.",
        x = 0.50f, y = 0.235f,
    ),
}

/** Región del cuerpo detectada para una especie, con el término que la justificó. */
data class BodyRegionMatch(
    val region: BodyRegion,
    val motivo: String,
)

// Prefijos de palabra (ya normalizados: minúsculas y sin acentos) que mapean a cada región.
// Se comparan contra el INICIO de cada palabra del texto, no como substring, para evitar
// falsos positivos (ej. "tos" dentro de "efectos").
private val KEYWORDS: List<Pair<BodyRegion, List<String>>> = listOf(
    BodyRegion.CABEZA to listOf(
        "sedante", "sedativ", "sedac", "ansiolit", "ansiedad", "nervios", "relaj", "insomnio",
        "sueno", "estres", "calmante", "calma", "cefalea", "migrana", "tranquiliz", "hipnotic",
    ),
    BodyRegion.RESPIRATORIO to listOf(
        "expectorante", "tos", "toses", "respirat", "bronq", "gripe", "gripal", "antigripal",
        "resfr", "catarro", "asma", "pulmon", "garganta", "mucosidad", "descongest", "antitusiv",
    ),
    BodyRegion.CORAZON to listOf(
        "cardio", "circulat", "circulac", "colesterol", "corazon", "vascular", "hipertens", "presion",
    ),
    BodyRegion.DIGESTIVO to listOf(
        "digest", "estomago", "estomacal", "gastric", "gastro", "higado", "hepat", "diarrea",
        "antidiarr", "espasm", "antiespasm", "colico", "carminativ", "nausea", "intestin",
        "empacho", "acidez", "demulcen", "laxante",
    ),
    BodyRegion.URINARIO to listOf(
        "diuret", "renal", "rinon", "urinari", "prostat", "depurativ", "calculos", "antiparasit",
    ),
    BodyRegion.PIEL to listOf(
        "cicatriz", "dermat", "herida", "quemadura", "astringent", "llagas", "acne", "hidratante",
        "piel", "topico", "emoliente",
    ),
    BodyRegion.ARTICULACIONES to listOf(
        "antiinflam", "artrit", "reumat", "articular", "muscular",
    ),
    BodyRegion.DEFENSAS to listOf(
        "inmun", "antimicrob", "antibiot", "antibacter", "antifung", "antiviral", "antisep",
        "antioxidante", "antioxid", "citotox",
    ),
)

/** Minúsculas + sin acentos, para comparar de forma robusta. */
private fun normalize(text: String): String {
    val sb = StringBuilder(text.length)
    for (c in text.lowercase()) {
        sb.append(
            when (c) {
                'á' -> 'a'; 'é' -> 'e'; 'í' -> 'i'; 'ó' -> 'o'; 'ú', 'ü' -> 'u'; 'ñ' -> 'n'
                else -> c
            }
        )
    }
    return sb.toString()
}

/**
 * Infiere qué regiones del cuerpo beneficia una especie a partir de sus usos tradicionales,
 * clasificación y perfil etnobotánico. Devuelve las regiones en el orden del enum, cada una
 * con el término concreto que la activó (para mostrarlo como justificación).
 */
fun inferBodyRegions(species: Species): List<BodyRegionMatch> {
    val texto = buildString {
        append(species.etnobotanica.usoTradicional).append(' ')
        append(species.etnobotanica.clasificacion).append(' ')
        append(species.perfilEtnobotanico)
    }
    // Palabras del texto, normalizadas.
    val words = normalize(texto).split(Regex("[^a-z]+")).filter { it.isNotBlank() }

    val matches = mutableListOf<BodyRegionMatch>()
    for ((region, prefixes) in KEYWORDS) {
        val hit = words.any { word -> prefixes.any { word.startsWith(it) } }
        if (hit) {
            val motivo = species.etnobotanica.usoTradicional.ifBlank { species.etnobotanica.clasificacion }
            matches.add(BodyRegionMatch(region, motivo))
        }
    }
    return matches.sortedBy { it.region.ordinal }
}
