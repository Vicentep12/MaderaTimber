package com.duoc.madertimber.data.repository

import android.content.Context
import com.duoc.madertimber.data.model.DatosPanel
import com.duoc.madertimber.data.model.DatosRelacionados
import java.io.Reader

sealed interface EstadoCargaPanel {
    data object Cargando : EstadoCargaPanel
    data class Listo(val datos: DatosPanel) : EstadoCargaPanel
    data object Vacio : EstadoCargaPanel
    data class Error(val mensaje: String) : EstadoCargaPanel
}

sealed interface ResultadoConsultaPanel {
    data object Cargando : ResultadoConsultaPanel
    data class Disponible(val datos: DatosRelacionados) : ResultadoConsultaPanel
    data class Vacio(val datos: DatosRelacionados) : ResultadoConsultaPanel
    data class SinCobertura(val lineaId: String, val anio: Int) : ResultadoConsultaPanel
    data class Error(val mensaje: String) : ResultadoConsultaPanel
}

class LocalPanelRepository(private val abrirDatos: () -> Reader) {
    constructor(context: Context) : this({
        context.assets.open(ARCHIVO_DATOS).bufferedReader(Charsets.UTF_8)
    })

    private var estado: EstadoCargaPanel = EstadoCargaPanel.Cargando

    fun cargar(): EstadoCargaPanel {
        estado = EstadoCargaPanel.Cargando
        estado = try {
            val datos = abrirDatos().use { LectorDatosPanel.leer(it) }
            if (datos.lineas.isEmpty()) EstadoCargaPanel.Vacio else EstadoCargaPanel.Listo(datos)
        } catch (error: Exception) {
            EstadoCargaPanel.Error(error.message ?: "No se pudieron cargar los datos locales")
        }
        return estado
    }

    fun consultar(lineaId: String, anio: Int): ResultadoConsultaPanel {
        return when (val actual = estado) {
            EstadoCargaPanel.Cargando -> ResultadoConsultaPanel.Cargando
            EstadoCargaPanel.Vacio -> ResultadoConsultaPanel.Error("No hay líneas disponibles")
            is EstadoCargaPanel.Error -> ResultadoConsultaPanel.Error(actual.mensaje)
            is EstadoCargaPanel.Listo -> {
                val linea = actual.datos.lineas.find { it.id == lineaId }
                    ?: return ResultadoConsultaPanel.Error("La línea $lineaId no existe")
                if (anio !in actual.datos.coberturaPorLinea[lineaId].orEmpty()) {
                    ResultadoConsultaPanel.SinCobertura(lineaId, anio)
                } else {
                    val relacionados = DatosRelacionados(
                        linea = linea,
                        anio = anio,
                        proyectos = actual.datos.proyectos.filter { proyecto ->
                            proyecto.lineaId == lineaId && proyecto.seguimientos.any { it.anio == anio }
                        },
                        investigadores = actual.datos.investigadores.filter { investigador ->
                            investigador.vinculaciones.any {
                                it.lineaId == lineaId && it.anio == anio
                            }
                        },
                        publicaciones = actual.datos.publicaciones.filter {
                            it.lineaId == lineaId && it.anio == anio
                        }
                    )
                    if (relacionados.estaVacio) {
                        ResultadoConsultaPanel.Vacio(relacionados)
                    } else {
                        ResultadoConsultaPanel.Disponible(relacionados)
                    }
                }
            }
        }
    }

    companion object {
        const val ARCHIVO_DATOS = "cenamad_datos.json"
    }
}
