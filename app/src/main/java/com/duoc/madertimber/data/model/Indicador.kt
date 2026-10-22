package com.duoc.madertimber.data.model

data class Indicador(
    val id: String,
    val lineaId: String,
    val nombre: String,
    val descripcion: String,
    val unidad: String,
    val tipo: TipoIndicador,
    val valores: List<ValorIndicador>
) {
}