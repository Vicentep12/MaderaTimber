package com.duoc.madertimber.data.model

fun tieneIdsDuplicados(lineas: List<LineaInvestigacion>): Boolean {

    val ids = lineas.map { it.id }
    val idsUnicos = ids.distinct()

    return ids.size != idsUnicos.size
}