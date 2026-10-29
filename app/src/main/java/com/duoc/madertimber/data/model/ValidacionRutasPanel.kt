package com.duoc.madertimber.data.model

enum class CategoriaRelacionada { PROYECTOS, INVESTIGADORES, PUBLICACIONES }

// Qué listas se muestran según el indicador: proyectos, investigadores o publicaciones
fun categoriasRelacionadas(tipo: TipoIndicador?): List<CategoriaRelacionada> = when (tipo) {
    null, TipoIndicador.IMPACTO_ESTIMADO -> CategoriaRelacionada.entries
    TipoIndicador.PROYECTOS_ACTIVOS, TipoIndicador.AVANCE_PROMEDIO ->
        listOf(CategoriaRelacionada.PROYECTOS)
    TipoIndicador.INVESTIGADORES_ASOCIADOS ->
        listOf(CategoriaRelacionada.INVESTIGADORES)
    TipoIndicador.PUBLICACIONES_GENERADAS ->
        listOf(CategoriaRelacionada.PUBLICACIONES)
}

// Devuelve un mensaje de error si la línea o el año de la ruta no existen; si está bien, null
fun validarContextoRuta(
    lineaId: String?,
    anio: Int?,
    lineas: List<LineaInvestigacion>,
    anios: List<Int>,
    lineaSeleccionada: String?,
    anioSeleccionado: Int
): String? = when {
    lineaId.isNullOrBlank() || lineas.none { it.id == lineaId } ->
        "La línea solicitada no existe."
    anio == null || anio !in anios ->
        "El año solicitado no está disponible."
    lineaId != lineaSeleccionada || anio != anioSeleccionado ->
        "La selección cambió. Vuelve al panel y abre el detalle nuevamente."
    else -> null
}

// Error si el indicador de la ruta no existe en esta línea
fun validarIndicadorRuta(indicadorId: String?, indicadores: List<IndicadorCalculado>): String? =
    if (indicadorId == null || indicadores.none { it.indicador.id == indicadorId }) {
        "El indicador solicitado no existe para esta línea."
    } else {
        null
    }

// Error si la ficha pedida no existe o no corresponde al indicador
fun validarFichaRuta(
    categoria: CategoriaRelacionada?,
    itemId: String?,
    datos: DatosRelacionados?,
    tipo: TipoIndicador?
): String? {
    if (categoria == null || categoria !in categoriasRelacionadas(tipo)) {
        return "El contenido solicitado no corresponde al indicador."
    }
    if (datos == null || itemId.isNullOrBlank()) return "El contenido solicitado no existe."
    val existe = when (categoria) {
        CategoriaRelacionada.PROYECTOS -> datos.proyectos.any { it.id == itemId }
        CategoriaRelacionada.INVESTIGADORES -> datos.investigadores.any { it.id == itemId }
        CategoriaRelacionada.PUBLICACIONES -> datos.publicaciones.any { it.id == itemId }
    }
    return if (existe) null else "El contenido solicitado no existe en esta línea y año."
}
