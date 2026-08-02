package com.ecotec.floramedica.navigation

object Routes {
    const val HOME = "home"
    const val ESPECIES = "especies"
    const val ESPECIE_DETAIL = "especies/{slug}"
    const val ETNOBOTANICA = "etnobotanica"
    const val ETNOBOTANICA_DETAIL = "etnobotanica/{slug}"
    const val AUTORES = "autores"

    fun especieDetail(slug: String) = "especies/$slug"
    fun etnobotanicaDetail(slug: String) = "etnobotanica/$slug"
}
