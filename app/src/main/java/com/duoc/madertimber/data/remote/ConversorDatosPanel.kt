package com.duoc.madertimber.data.remote

import com.duoc.madertimber.data.model.DatosPanel
import com.duoc.madertimber.data.model.EstadoProyecto
import com.duoc.madertimber.data.model.Investigador
import com.duoc.madertimber.data.model.LineaInvestigacion
import com.duoc.madertimber.data.model.Proyecto
import com.duoc.madertimber.data.model.Publicacion
import com.duoc.madertimber.data.model.SeguimientoProyecto
import com.duoc.madertimber.data.model.VinculacionInvestigador
import com.duoc.madertimber.data.model.validarIntegridadDatos

// Convierte las respuestas de la API en los modelos que ya usaba el panel
object ConversorDatosPanel {

    fun convertir(
        lineasApi: List<LineaDto>,
        proyectosApi: List<ProyectoDto>,
        investigadoresApi: List<InvestigadorDto>,
        publicacionesApi: List<PublicacionDto>
    ): DatosPanel {
        val lineas = lineasApi.map { LineaInvestigacion(it.codigo, it.nombre, it.descripcion, it.color) }
        val cobertura = lineasApi.associate { it.codigo to it.anios.toSet() }
        val proyectos = proyectosApi.map { proyecto ->
            Proyecto(
                id = proyecto.codigo,
                lineaId = proyecto.lineaCodigo,
                titulo = proyecto.titulo,
                seguimientos = proyecto.seguimientos.map {
                    SeguimientoProyecto(it.anio, EstadoProyecto.valueOf(it.estado), it.avancePorcentaje)
                }
            )
        }
        val investigadores = investigadoresApi.map { investigador ->
            Investigador(
                id = investigador.codigo,
                nombre = investigador.nombre,
                especialidad = investigador.especialidad,
                vinculaciones = investigador.vinculaciones.map { VinculacionInvestigador(it.lineaId, it.anio) }
            )
        }
        val publicaciones = publicacionesApi.map {
            Publicacion(it.codigo, it.lineaCodigo, it.titulo, it.tipo, it.anio)
        }

        // Misma revisión de integridad que se hacía con el JSON local
        val errores = validarIntegridadDatos(lineas, proyectos, investigadores, publicaciones, indicadores = emptyList())
        require(errores.isEmpty()) { "Los datos recibidos no son válidos." }

        return DatosPanel(lineas, cobertura, proyectos, investigadores, publicaciones)
    }
}
