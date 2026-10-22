package com.duoc.madertimber.data.model

data class Proyecto(
    val id: String,
    val lineaId: String,
    val titulo: String,
    val seguimientos: List<SeguimientoProyecto>
) {
}