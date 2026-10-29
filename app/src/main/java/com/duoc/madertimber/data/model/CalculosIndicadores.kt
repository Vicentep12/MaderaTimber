package com.duoc.madertimber.data.model

import kotlin.math.min

sealed interface VariacionIndicador {
    data class Porcentaje(val valor: Double) : VariacionIndicador
    data class PuntosPorcentuales(val valor: Double) : VariacionIndicador
    data object SinComparacion : VariacionIndicador
    data object BaseAnteriorCero : VariacionIndicador
}

data class IndicadorCalculado(
    val indicador: Indicador,
    val anioSeleccionado: Int,
    val valorSeleccionado: Double?,
    val variacion: VariacionIndicador
) {
    val historicoHastaSeleccionado: List<ValorIndicador>
        get() = indicador.valores.filter { it.anio <= anioSeleccionado }
}

/** Deriva indicadores desde entidades validadas; no guarda conteos independientes. */
fun calcularIndicadores(datos: DatosPanel, lineaId: String, anioSeleccionado: Int): List<IndicadorCalculado> {
    val anios = datos.coberturaPorLinea[lineaId].orEmpty().sorted()
    val resumenes = anios.associateWith { anio -> resumirAnio(datos, lineaId, anio) }

    return TipoIndicador.entries.map { tipo ->
        val valores = anios.mapNotNull { anio ->
            resumenes.getValue(anio).valor(tipo)?.let { ValorIndicador(anio, it) }
        }
        val indicador = Indicador(
            id = "$lineaId-${tipo.name}",
            lineaId = lineaId,
            nombre = tipo.nombre(),
            descripcion = tipo.descripcion(),
            unidad = tipo.unidad(),
            tipo = tipo,
            valores = valores
        )
        val actual = valores.find { it.anio == anioSeleccionado }?.valor
        val anterior = valores.find { it.anio.toLong() == anioSeleccionado.toLong() - 1 }?.valor
        IndicadorCalculado(
            indicador = indicador,
            anioSeleccionado = anioSeleccionado,
            valorSeleccionado = actual,
            variacion = calcularVariacion(tipo, actual, anterior)
        )
    }
}

fun calcularVariacion(tipo: TipoIndicador, actual: Double?, anterior: Double?): VariacionIndicador {
    if (actual == null || anterior == null) return VariacionIndicador.SinComparacion
    if (tipo == TipoIndicador.AVANCE_PROMEDIO) {
        return VariacionIndicador.PuntosPorcentuales(actual - anterior)
    }
    if (anterior == 0.0) return VariacionIndicador.BaseAnteriorCero
    return VariacionIndicador.Porcentaje((actual - anterior) / anterior * 100.0)
}

private data class ResumenAnual(
    val proyectosActivos: Int,
    val publicaciones: Int,
    val investigadores: Int,
    val avancePromedio: Double?
) {
    fun valor(tipo: TipoIndicador): Double? = when (tipo) {
        TipoIndicador.PROYECTOS_ACTIVOS -> proyectosActivos.toDouble()
        TipoIndicador.PUBLICACIONES_GENERADAS -> publicaciones.toDouble()
        TipoIndicador.INVESTIGADORES_ASOCIADOS -> investigadores.toDouble()
        TipoIndicador.AVANCE_PROMEDIO -> avancePromedio
        TipoIndicador.IMPACTO_ESTIMADO -> 100.0 * (
            0.4 * min(proyectosActivos / 10.0, 1.0) +
                0.4 * min(publicaciones / 20.0, 1.0) +
                0.2 * min(investigadores / 15.0, 1.0)
            )
    }
}

private fun resumirAnio(datos: DatosPanel, lineaId: String, anio: Int): ResumenAnual {
    val proyectos = proyectosActivos(datos.proyectos, lineaId, anio)
    val publicaciones = datos.publicaciones
        .filter { it.lineaId == lineaId && it.anio == anio }
        .distinctBy { it.id }
    val investigadores = datos.investigadores
        .filter { investigador ->
            investigador.vinculaciones.any { it.lineaId == lineaId && it.anio == anio }
        }
        .distinctBy { it.id }
    val avances = proyectos.map { proyecto ->
        proyecto.seguimientos.single { it.anio == anio }.avancePorcentaje
    }
    return ResumenAnual(
        proyectosActivos = proyectos.size,
        publicaciones = publicaciones.size,
        investigadores = investigadores.size,
        avancePromedio = avances.takeIf { it.isNotEmpty() }?.average()
    )
}

fun proyectosActivos(proyectos: List<Proyecto>, lineaId: String, anio: Int): List<Proyecto> =
    proyectos.filter { proyecto ->
        proyecto.lineaId == lineaId && proyecto.seguimientos.any {
            it.anio == anio && it.estado == EstadoProyecto.ACTIVO
        }
    }.distinctBy { it.id }

fun TipoIndicador.nombre(): String = when (this) {
    TipoIndicador.PROYECTOS_ACTIVOS -> "Proyectos activos"
    TipoIndicador.PUBLICACIONES_GENERADAS -> "Publicaciones generadas"
    TipoIndicador.INVESTIGADORES_ASOCIADOS -> "Investigadores asociados"
    TipoIndicador.AVANCE_PROMEDIO -> "Avance promedio"
    TipoIndicador.IMPACTO_ESTIMADO -> "Impacto estimado"
}

private fun TipoIndicador.descripcion(): String = when (this) {
    TipoIndicador.PROYECTOS_ACTIVOS -> "Proyectos activos al cierre del año"
    TipoIndicador.PUBLICACIONES_GENERADAS -> "Publicaciones generadas durante el año"
    TipoIndicador.INVESTIGADORES_ASOCIADOS -> "Investigadores vinculados a la línea durante el año"
    TipoIndicador.AVANCE_PROMEDIO -> "Media del avance de los proyectos activos"
    TipoIndicador.IMPACTO_ESTIMADO -> "Índice demostrativo de actividad frente a metas ficticias"
}

fun TipoIndicador.unidad(): String = when (this) {
    TipoIndicador.PROYECTOS_ACTIVOS -> "proyectos"
    TipoIndicador.PUBLICACIONES_GENERADAS -> "publicaciones"
    TipoIndicador.INVESTIGADORES_ASOCIADOS -> "investigadores"
    TipoIndicador.AVANCE_PROMEDIO -> "%"
    TipoIndicador.IMPACTO_ESTIMADO -> "puntos"
}
