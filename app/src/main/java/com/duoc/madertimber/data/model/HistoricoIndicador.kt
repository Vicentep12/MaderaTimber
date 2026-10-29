package com.duoc.madertimber.data.model

data class PuntoHistorico(
    val anio: Int,
    val valor: Double?,
    val seleccionado: Boolean
)

data class HistoricoIndicador(
    val titulo: String,
    val unidad: String,
    val puntos: List<PuntoHistorico>
)

/** Prepara el eje anual sin convertir años ausentes en valores cero. */
fun prepararHistorico(
    indicador: IndicadorCalculado,
    aniosDisponibles: List<Int>
): HistoricoIndicador {
    val seleccionado = indicador.anioSeleccionado
    val primerAnio = (aniosDisponibles + indicador.historicoHastaSeleccionado.map { it.anio })
        .filter { it <= seleccionado }
        .minOrNull() ?: seleccionado
    val valores = indicador.historicoHastaSeleccionado.associateBy { it.anio }
    return HistoricoIndicador(
        titulo = indicador.indicador.nombre,
        unidad = indicador.indicador.unidad,
        puntos = (primerAnio..seleccionado).map { anio ->
            PuntoHistorico(
                anio = anio,
                valor = valores[anio]?.valor,
                seleccionado = anio == seleccionado
            )
        }
    )
}
