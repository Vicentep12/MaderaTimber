package com.duoc.madertimber.data.model

data class DatosPanel(
    val lineas: List<LineaInvestigacion>,
    val coberturaPorLinea: Map<String, Set<Int>>,
    val proyectos: List<Proyecto>,
    val investigadores: List<Investigador>,
    val publicaciones: List<Publicacion>
)

data class DatosRelacionados(
    val linea: LineaInvestigacion,
    val anio: Int,
    val proyectos: List<Proyecto>,
    val investigadores: List<Investigador>,
    val publicaciones: List<Publicacion>
) {
    val estaVacio: Boolean
        get() = proyectos.isEmpty() && investigadores.isEmpty() && publicaciones.isEmpty()
}
