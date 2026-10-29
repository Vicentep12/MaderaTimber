package com.duoc.madertimber.data.repository

import android.content.Context
import com.duoc.madertimber.data.model.DatosPanel
import com.duoc.madertimber.data.model.DatosRelacionados
import com.duoc.madertimber.data.model.IndicadorCalculado
import com.duoc.madertimber.data.model.LineaInvestigacion
import com.duoc.madertimber.data.model.TipoIndicador
import com.duoc.madertimber.data.model.calcularIndicadores
import com.duoc.madertimber.data.model.proyectosActivos
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

sealed interface ResultadoIndicadoresPanel {
    data object Cargando : ResultadoIndicadoresPanel
    data class Disponible(
        val linea: LineaInvestigacion,
        val anio: Int,
        val aniosCubiertos: List<Int>,
        val indicadores: List<IndicadorCalculado>
    ) : ResultadoIndicadoresPanel
    data class SinCobertura(val lineaId: String, val anio: Int) : ResultadoIndicadoresPanel
    data class Error(val mensaje: String) : ResultadoIndicadoresPanel
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

    fun consultarIndicadores(lineaId: String, anio: Int): ResultadoIndicadoresPanel {
        return when (val actual = estado) {
            EstadoCargaPanel.Cargando -> ResultadoIndicadoresPanel.Cargando
            EstadoCargaPanel.Vacio -> ResultadoIndicadoresPanel.Error("No hay líneas disponibles")
            is EstadoCargaPanel.Error -> ResultadoIndicadoresPanel.Error(actual.mensaje)
            is EstadoCargaPanel.Listo -> {
                val linea = actual.datos.lineas.find { it.id == lineaId }
                    ?: return ResultadoIndicadoresPanel.Error("La línea $lineaId no existe")
                if (anio !in actual.datos.coberturaPorLinea[lineaId].orEmpty()) {
                    ResultadoIndicadoresPanel.SinCobertura(lineaId, anio)
                } else {
                    ResultadoIndicadoresPanel.Disponible(
                        linea = linea,
                        anio = anio,
                        aniosCubiertos = actual.datos.coberturaPorLinea[lineaId].orEmpty().sorted(),
                        indicadores = calcularIndicadores(actual.datos, lineaId, anio)
                    )
                }
            }
        }
    }

    fun consultarRelacionados(
        lineaId: String,
        anio: Int,
        tipo: TipoIndicador? = null
    ): ResultadoConsultaPanel {
        val consulta = consultar(lineaId, anio)
        if (consulta !is ResultadoConsultaPanel.Disponible) return consulta

        val datos = consulta.datos
        val activos = proyectosActivos(datos.proyectos, lineaId, anio)
        val incluirProyectos = tipo == null || tipo == TipoIndicador.PROYECTOS_ACTIVOS ||
            tipo == TipoIndicador.AVANCE_PROMEDIO || tipo == TipoIndicador.IMPACTO_ESTIMADO
        val incluirPublicaciones = tipo == null || tipo == TipoIndicador.PUBLICACIONES_GENERADAS ||
            tipo == TipoIndicador.IMPACTO_ESTIMADO
        val incluirInvestigadores = tipo == null || tipo == TipoIndicador.INVESTIGADORES_ASOCIADOS ||
            tipo == TipoIndicador.IMPACTO_ESTIMADO
        val filtrados = datos.copy(
            proyectos = if (incluirProyectos) activos else emptyList(),
            publicaciones = if (incluirPublicaciones) datos.publicaciones.distinctBy { it.id }
                else emptyList(),
            investigadores = if (incluirInvestigadores) datos.investigadores.distinctBy { it.id }
                else emptyList()
        )
        return if (filtrados.estaVacio) {
            ResultadoConsultaPanel.Vacio(filtrados)
        } else {
            ResultadoConsultaPanel.Disponible(filtrados)
        }
    }

    companion object {
        const val ARCHIVO_DATOS = "cenamad_datos.json"
    }
}
