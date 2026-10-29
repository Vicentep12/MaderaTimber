package com.duoc.madertimber.data.model

enum class TipoErrorIntegridad {
    ID_DUPLICADO,
    LINEA_INEXISTENTE,
    SEGUIMIENTO_DUPLICADO,
    VINCULACION_DUPLICADA,
    AVANCE_FUERA_DE_RANGO,
    INDICADOR_DUPLICADO,
    VALOR_ANUAL_DUPLICADO
}

data class ErrorIntegridadDatos(
    val tipo: TipoErrorIntegridad,
    val detalle: String
)

/** Comprueba las relaciones y restricciones de los modelos antes de usar un conjunto de datos. */
fun validarIntegridadDatos(
    lineas: List<LineaInvestigacion>,
    proyectos: List<Proyecto>,
    investigadores: List<Investigador>,
    publicaciones: List<Publicacion>,
    indicadores: List<Indicador>
): List<ErrorIntegridadDatos> {
    val errores = mutableListOf<ErrorIntegridadDatos>()
    val idsLineas = lineas.mapTo(mutableSetOf()) { it.id }

    registrarIdsDuplicados(lineas, "Línea", { it.id }, errores)
    registrarIdsDuplicados(proyectos, "Proyecto", { it.id }, errores)
    registrarIdsDuplicados(investigadores, "Investigador", { it.id }, errores)
    registrarIdsDuplicados(publicaciones, "Publicación", { it.id }, errores)
    registrarIdsDuplicados(indicadores, "Indicador", { it.id }, errores)

    proyectos.forEach { proyecto ->
        if (proyecto.lineaId !in idsLineas) {
            errores += ErrorIntegridadDatos(
                TipoErrorIntegridad.LINEA_INEXISTENTE,
                "Proyecto ${proyecto.id}: línea ${proyecto.lineaId}"
            )
        }

        val anios = mutableSetOf<Int>()
        proyecto.seguimientos.forEach { seguimiento ->
            if (!anios.add(seguimiento.anio)) {
                errores += ErrorIntegridadDatos(
                    TipoErrorIntegridad.SEGUIMIENTO_DUPLICADO,
                    "Proyecto ${proyecto.id}: año ${seguimiento.anio}"
                )
            }
            if (!seguimiento.avancePorcentaje.isFinite() || seguimiento.avancePorcentaje !in 0.0..100.0) {
                errores += ErrorIntegridadDatos(
                    TipoErrorIntegridad.AVANCE_FUERA_DE_RANGO,
                    "Proyecto ${proyecto.id}: año ${seguimiento.anio}"
                )
            }
        }
    }

    investigadores.forEach { investigador ->
        val vinculaciones = mutableSetOf<Pair<String, Int>>()
        investigador.vinculaciones.forEach { vinculacion ->
            if (vinculacion.lineaId !in idsLineas) {
                errores += ErrorIntegridadDatos(
                    TipoErrorIntegridad.LINEA_INEXISTENTE,
                    "Investigador ${investigador.id}: línea ${vinculacion.lineaId}"
                )
            }
            if (!vinculaciones.add(vinculacion.lineaId to vinculacion.anio)) {
                errores += ErrorIntegridadDatos(
                    TipoErrorIntegridad.VINCULACION_DUPLICADA,
                    "Investigador ${investigador.id}: línea ${vinculacion.lineaId}, año ${vinculacion.anio}"
                )
            }
        }
    }

    publicaciones.forEach { publicacion ->
        if (publicacion.lineaId !in idsLineas) {
            errores += ErrorIntegridadDatos(
                TipoErrorIntegridad.LINEA_INEXISTENTE,
                "Publicación ${publicacion.id}: línea ${publicacion.lineaId}"
            )
        }
    }

    val indicadoresPorLineaYTipo = mutableSetOf<Pair<String, TipoIndicador>>()
    indicadores.forEach { indicador ->
        if (indicador.lineaId !in idsLineas) {
            errores += ErrorIntegridadDatos(
                TipoErrorIntegridad.LINEA_INEXISTENTE,
                "Indicador ${indicador.id}: línea ${indicador.lineaId}"
            )
        }
        if (!indicadoresPorLineaYTipo.add(indicador.lineaId to indicador.tipo)) {
            errores += ErrorIntegridadDatos(
                TipoErrorIntegridad.INDICADOR_DUPLICADO,
                "Línea ${indicador.lineaId}: tipo ${indicador.tipo}"
            )
        }

        val anios = mutableSetOf<Int>()
        indicador.valores.forEach { valor ->
            if (!anios.add(valor.anio)) {
                errores += ErrorIntegridadDatos(
                    TipoErrorIntegridad.VALOR_ANUAL_DUPLICADO,
                    "Indicador ${indicador.id}: año ${valor.anio}"
                )
            }
        }
    }

    return errores
}

private fun <T> registrarIdsDuplicados(
    elementos: List<T>,
    entidad: String,
    idDe: (T) -> String,
    errores: MutableList<ErrorIntegridadDatos>
) {
    val ids = mutableSetOf<String>()
    elementos.forEach { elemento ->
        val id = idDe(elemento)
        if (!ids.add(id)) {
            errores += ErrorIntegridadDatos(TipoErrorIntegridad.ID_DUPLICADO, "$entidad $id")
        }
    }
}

fun tieneIdsDuplicados(lineas: List<LineaInvestigacion>): Boolean {

    val ids = lineas.map { it.id }
    val idsUnicos = ids.distinct()

    return ids.size != idsUnicos.size
}

fun tieneProyectosConLineaInexistente(
    proyectos: List<Proyecto>,
    lineas: List<LineaInvestigacion>
): Boolean {
    val idsLineas = lineas.map { it.id }

    return proyectos.any { proyecto ->
        proyecto.lineaId !in idsLineas
    }
}
