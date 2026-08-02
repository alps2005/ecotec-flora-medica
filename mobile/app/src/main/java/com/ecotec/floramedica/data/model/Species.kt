package com.ecotec.floramedica.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Taxonomia(
    val reino: String,
    val division: String,
    val clase: String,
    val familia: String,
    val genero: String,
)

@Serializable
data class EtnobotanicaResumen(
    val clasificacion: String,
    val parteUtilizada: String,
    val usoTradicional: String,
)

@Serializable
data class HistoriaEvolucion(
    val origen: String,
    val dispersion: String,
    val evolucion: String,
)

@Serializable
data class ComercioPais(
    val pais: String,
    val detalle: String,
)

@Serializable
data class Comercio(
    val exportacion: List<ComercioPais> = emptyList(),
    val importacion: List<ComercioPais> = emptyList(),
)

@Serializable
data class CompuestoQuimico(
    val nombre: String,
    val detalle: String,
)

@Serializable
data class MultimediaPrincipal(
    val imagenUrl: String,
    val imagenPublicId: String = "",
    val videoUrl: String = "",
    val videoPublicId: String = "",
    val proveedor: String = "",
)

@Serializable
data class Species(
    val slug: String,
    val nombreComun: String,
    val nombreCientifico: String,
    val nombresAlternativos: List<String> = emptyList(),
    val taxonomia: Taxonomia,
    val etnobotanica: EtnobotanicaResumen,
    val perfilEtnobotanico: String,
    val historiaEvolucion: HistoriaEvolucion,
    val comercio: Comercio,
    val compuestosQuimicos: List<CompuestoQuimico> = emptyList(),
    val multimediaPrincipal: MultimediaPrincipal,
    val estado: String,
    val descripcion: String = "",
)

