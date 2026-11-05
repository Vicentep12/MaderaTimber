package com.duoc.madertimber.viewmodel

import com.duoc.madertimber.data.model.DatosRelacionados
import com.duoc.madertimber.data.model.HistoricoIndicador
import com.duoc.madertimber.data.model.IndicadorCalculado
import com.duoc.madertimber.data.model.LineaInvestigacion
import com.duoc.madertimber.data.model.TipoIndicador

data class DetalleLineaUiState(
    val linea: LineaInvestigacion,
    val anio: Int,
    val indicadores: List<IndicadorCalculado>,
    val tipoHistorico: TipoIndicador,
    val historico: HistoricoIndicador?,
    val relacionados: DatosRelacionados?,
    val mensaje: String? = null
)
