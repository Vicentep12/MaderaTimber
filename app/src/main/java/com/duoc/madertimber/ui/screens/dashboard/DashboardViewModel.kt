package com.duoc.madertimber.ui.screens.dashboard

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.duoc.madertimber.data.model.DatosRelacionados
import com.duoc.madertimber.data.model.HistoricoIndicador
import com.duoc.madertimber.data.model.IndicadorCalculado
import com.duoc.madertimber.data.model.LineaInvestigacion
import com.duoc.madertimber.data.model.TipoIndicador
import com.duoc.madertimber.data.model.prepararHistorico
import com.duoc.madertimber.data.repository.EstadoCargaPanel
import com.duoc.madertimber.data.repository.LocalPanelRepository
import com.duoc.madertimber.data.repository.ResultadoConsultaPanel
import com.duoc.madertimber.data.repository.ResultadoIndicadoresPanel
import com.duoc.madertimber.data.repository.SeleccionGuardada
import com.duoc.madertimber.data.repository.SeleccionRepository
import com.duoc.madertimber.ui.screens.lineas.DetalleLineaUiState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

enum class EstadoDashboard { CARGANDO, LISTO, VACIO, SIN_COBERTURA, ERROR }

data class BarraIndicador(
    val linea: LineaInvestigacion,
    val valor: Double?,
    val fraccion: Float,
    val seleccionada: Boolean
)

data class DashboardUiState(
    val estado: EstadoDashboard = EstadoDashboard.CARGANDO,
    val lineaId: String? = null,
    val anio: Int = 2024,
    val tipoComparacion: TipoIndicador = TipoIndicador.PROYECTOS_ACTIVOS,
    val tipoIndicador: TipoIndicador = TipoIndicador.PROYECTOS_ACTIVOS,
    val tipoHistoricoLinea: TipoIndicador = TipoIndicador.PROYECTOS_ACTIVOS,
    val lineas: List<LineaInvestigacion> = emptyList(),
    val aniosDisponibles: List<Int> = emptyList(),
    val indicadores: List<IndicadorCalculado> = emptyList(),
    val historicos: Map<TipoIndicador, HistoricoIndicador> = emptyMap(),
    val barras: List<BarraIndicador> = emptyList(),
    val relacionados: DatosRelacionados? = null,
    val relacionadosSinFiltro: DatosRelacionados? = null,
    val mensaje: String? = null
) {
    val lineaSeleccionada: LineaInvestigacion?
        get() = lineas.find { it.id == lineaId }

    val indicadorSeleccionado: IndicadorCalculado?
        get() = indicadores.find { it.indicador.tipo == tipoIndicador }

    val detalleLinea: DetalleLineaUiState?
        get() = lineaSeleccionada?.let { linea ->
            DetalleLineaUiState(
                linea = linea,
                anio = anio,
                indicadores = indicadores,
                tipoHistorico = tipoHistoricoLinea,
                historico = historicos[tipoHistoricoLinea],
                relacionados = relacionadosSinFiltro,
                mensaje = if (estado == EstadoDashboard.SIN_COBERTURA) mensaje else null
            )
        }
}

sealed interface DashboardEvent {
    data class LineaSeleccionada(val id: String) : DashboardEvent
    data class AnioSeleccionado(val anio: Int) : DashboardEvent
    data class FiltrosAplicados(val lineaId: String, val anio: Int) : DashboardEvent
    data class MetricaComparada(val tipo: TipoIndicador) : DashboardEvent
    data class IndicadorSeleccionado(val tipo: TipoIndicador) : DashboardEvent
    data class HistoricoLineaSeleccionado(val tipo: TipoIndicador) : DashboardEvent
    data object ReintentarCarga : DashboardEvent
}

// Guarda el estado del dashboard (línea, año y filtros) y decide qué se muestra
class DashboardViewModel(
    private val guardado: SavedStateHandle,
    private val repositorio: LocalPanelRepository,
    private val dispatcherCarga: CoroutineDispatcher = Dispatchers.IO,
    private val seleccionRepository: SeleccionRepository? = null
) : ViewModel() {
    private var carga: Job? = null
    private val escrituras = Channel<SeleccionGuardada>(Channel.UNLIMITED)
    private var seleccionLeida = false

    var uiState by mutableStateOf(
        DashboardUiState(
            lineaId = guardado[CLAVE_LINEA],
            anio = guardado[CLAVE_ANIO] ?: 2024,
            tipoComparacion = tipoGuardado(CLAVE_COMPARACION),
            tipoIndicador = tipoGuardado(CLAVE_INDICADOR),
            tipoHistoricoLinea = tipoGuardado(CLAVE_HISTORICO_LINEA)
        )
    )
        private set

    // Arranca la cola que guarda la selección en Room y carga los datos
    init {
        viewModelScope.launch(dispatcherCarga) {
            for (seleccion in escrituras) {
                try {
                    seleccionRepository?.guardar(seleccion)
                } catch (error: CancellationException) {
                    throw error
                } catch (error: Exception) {
                    android.util.Log.e("DashboardViewModel", "No se pudo guardar la selección", error)
                }
            }
        }
        cargar()
    }

    // Recibe lo que hace el usuario en la pantalla y actualiza el estado
    fun onEvent(evento: DashboardEvent) {
        if (evento == DashboardEvent.ReintentarCarga) {
            cargar()
            return
        }
        if (uiState.estado !in setOf(EstadoDashboard.LISTO, EstadoDashboard.SIN_COBERTURA)) return

        when (evento) {
            is DashboardEvent.LineaSeleccionada -> {
                if (uiState.lineas.none { it.id == evento.id }) return
                guardado[CLAVE_LINEA] = evento.id
                uiState = uiState.copy(lineaId = evento.id)
            }
            is DashboardEvent.AnioSeleccionado -> {
                if (evento.anio !in uiState.aniosDisponibles) return
                guardado[CLAVE_ANIO] = evento.anio
                uiState = uiState.copy(anio = evento.anio)
            }
            is DashboardEvent.FiltrosAplicados -> {
                if (uiState.lineas.none { it.id == evento.lineaId } ||
                    evento.anio !in uiState.aniosDisponibles
                ) return
                guardado[CLAVE_LINEA] = evento.lineaId
                guardado[CLAVE_ANIO] = evento.anio
                uiState = uiState.copy(lineaId = evento.lineaId, anio = evento.anio)
            }
            is DashboardEvent.MetricaComparada -> {
                guardado[CLAVE_COMPARACION] = evento.tipo.name
                uiState = uiState.copy(tipoComparacion = evento.tipo)
            }
            is DashboardEvent.IndicadorSeleccionado -> {
                guardado[CLAVE_INDICADOR] = evento.tipo.name
                uiState = uiState.copy(tipoIndicador = evento.tipo)
            }
            is DashboardEvent.HistoricoLineaSeleccionado -> {
                if (evento.tipo !in uiState.historicos) return
                guardado[CLAVE_HISTORICO_LINEA] = evento.tipo.name
                uiState = uiState.copy(tipoHistoricoLinea = evento.tipo)
                return
            }
            DashboardEvent.ReintentarCarga -> Unit
        }
        actualizarConsulta()
        if (evento is DashboardEvent.LineaSeleccionada || evento is DashboardEvent.AnioSeleccionado ||
            evento is DashboardEvent.FiltrosAplicados || evento is DashboardEvent.MetricaComparada
        ) guardarSeleccion()
    }

    // Primero lee la selección guardada en Room y después carga los datos del JSON
    private fun cargar() {
        carga?.cancel()
        uiState = uiState.copy(estado = EstadoDashboard.CARGANDO, mensaje = null)
        carga = viewModelScope.launch {
            if (!seleccionLeida) {
                val seleccion = withContext(dispatcherCarga) {
                    try {
                        seleccionRepository?.leer()
                    } catch (error: CancellationException) {
                        throw error
                    } catch (error: Exception) {
                        android.util.Log.e("DashboardViewModel", "No se pudo leer la selección", error)
                        null
                    }
                }
                if (seleccion != null) {
                    uiState = uiState.copy(
                        lineaId = uiState.lineaId ?: seleccion.lineaId,
                        anio = if (guardado.contains(CLAVE_ANIO)) uiState.anio else seleccion.anio,
                        tipoComparacion = if (guardado.contains(CLAVE_COMPARACION)) {
                            uiState.tipoComparacion
                        } else {
                            TipoIndicador.entries.find { it.name == seleccion.metrica }
                                ?: TipoIndicador.PROYECTOS_ACTIVOS
                        }
                    )
                }
                seleccionLeida = true
            }
            val resultado = withContext(dispatcherCarga) { repositorio.cargar() }
            when (resultado) {
                is EstadoCargaPanel.Listo -> prepararDatos(resultado)
                EstadoCargaPanel.Vacio -> uiState = uiState.copy(
                    estado = EstadoDashboard.VACIO,
                    lineas = emptyList(),
                    indicadores = emptyList(),
                    historicos = emptyMap(),
                    barras = emptyList(),
                    relacionados = null,
                    relacionadosSinFiltro = null
                )
                is EstadoCargaPanel.Error -> uiState = uiState.copy(
                    estado = EstadoDashboard.ERROR,
                    indicadores = emptyList(),
                    historicos = emptyMap(),
                    barras = emptyList(),
                    relacionados = null,
                    relacionadosSinFiltro = null,
                    mensaje = resultado.mensaje
                )
                EstadoCargaPanel.Cargando -> Unit
            }
        }
    }

    // Revisa que la línea y el año elegidos existan; si no, usa unos por defecto
    private fun prepararDatos(resultado: EstadoCargaPanel.Listo) {
        val datos = resultado.datos
        val lineas = datos.lineas
        val lineaId = uiState.lineaId?.takeIf { id -> lineas.any { it.id == id } }
            ?: lineas.first().id
        val anios = datos.coberturaPorLinea.values.flatten().distinct().sorted()
        val anio = uiState.anio.takeIf { it in anios }
            ?: if (2024 in anios) 2024 else anios.lastOrNull() ?: 2024
        guardado[CLAVE_LINEA] = lineaId
        guardado[CLAVE_ANIO] = anio
        guardado[CLAVE_COMPARACION] = uiState.tipoComparacion.name
        guardado[CLAVE_INDICADOR] = uiState.tipoIndicador.name
        guardado[CLAVE_HISTORICO_LINEA] = uiState.tipoHistoricoLinea.name
        uiState = uiState.copy(
            lineaId = lineaId,
            anio = anio,
            lineas = lineas,
            aniosDisponibles = anios
        )
        actualizarConsulta()
        guardarSeleccion()
    }

    // Manda la selección a la cola para guardarla en Room sin trabar la pantalla
    private fun guardarSeleccion() {
        val lineaId = uiState.lineaId ?: return
        escrituras.trySend(SeleccionGuardada(lineaId = lineaId, anio = uiState.anio,
            metrica = uiState.tipoComparacion.name))
    }

    // Vuelve a calcular indicadores, comparación y listas con la selección actual
    private fun actualizarConsulta() {
        val lineaId = uiState.lineaId ?: return
        val consulta = repositorio.consultarIndicadores(lineaId, uiState.anio)
        val barras = compararLineas()
        when (consulta) {
            is ResultadoIndicadoresPanel.Disponible -> {
                uiState = uiState.copy(
                    estado = EstadoDashboard.LISTO,
                    indicadores = consulta.indicadores,
                    historicos = consulta.indicadores.associate { indicador ->
                        indicador.indicador.tipo to prepararHistorico(indicador, uiState.aniosDisponibles)
                    },
                    barras = barras,
                    relacionados = consultarRelacionados(lineaId, uiState.tipoIndicador),
                    relacionadosSinFiltro = consultarRelacionados(lineaId),
                    mensaje = null
                )
            }
            is ResultadoIndicadoresPanel.SinCobertura -> uiState = uiState.copy(
                estado = EstadoDashboard.SIN_COBERTURA,
                indicadores = emptyList(),
                historicos = emptyMap(),
                barras = barras,
                relacionados = null,
                relacionadosSinFiltro = null,
                mensaje = "Sin datos para esta línea y año"
            )
            is ResultadoIndicadoresPanel.Error -> uiState = uiState.copy(
                estado = EstadoDashboard.ERROR,
                indicadores = emptyList(),
                historicos = emptyMap(),
                barras = emptyList(),
                relacionados = null,
                relacionadosSinFiltro = null,
                mensaje = consulta.mensaje
            )
            ResultadoIndicadoresPanel.Cargando -> uiState = uiState.copy(
                estado = EstadoDashboard.CARGANDO,
                indicadores = emptyList(),
                historicos = emptyMap(),
                barras = emptyList(),
                relacionados = null,
                relacionadosSinFiltro = null
            )
        }
    }

    // Trae proyectos, investigadores y publicaciones; devuelve null si no se pudo consultar
    private fun consultarRelacionados(
        lineaId: String,
        tipo: TipoIndicador? = null
    ): DatosRelacionados? = when (val resultado = repositorio.consultarRelacionados(lineaId, uiState.anio, tipo)) {
        is ResultadoConsultaPanel.Disponible -> resultado.datos
        is ResultadoConsultaPanel.Vacio -> resultado.datos
        else -> null
    }

    // Arma las barras del gráfico; la línea con el valor más alto queda al 100 %
    private fun compararLineas(): List<BarraIndicador> {
        val valores = uiState.lineas.map { linea ->
            val consulta = repositorio.consultarIndicadores(linea.id, uiState.anio)
            val valor = (consulta as? ResultadoIndicadoresPanel.Disponible)
                ?.indicadores?.find { it.indicador.tipo == uiState.tipoComparacion }
                ?.valorSeleccionado
            linea to valor
        }
        val maximo = valores.mapNotNull { it.second }.maxOrNull() ?: 0.0
        return valores.map { (linea, valor) ->
            BarraIndicador(
                linea = linea,
                valor = valor,
                fraccion = if (valor != null && maximo > 0.0) {
                    (valor / maximo).toFloat().coerceIn(0f, 1f)
                } else {
                    0f
                },
                seleccionada = linea.id == uiState.lineaId
            )
        }
    }

    // Recupera el tipo de indicador guardado; si no hay, usa proyectos activos
    private fun tipoGuardado(clave: String): TipoIndicador {
        val nombre: String? = guardado[clave]
        return TipoIndicador.entries.find { it.name == nombre }
            ?: TipoIndicador.PROYECTOS_ACTIVOS
    }

    companion object {
        private const val CLAVE_LINEA = "dashboard.lineaId"
        private const val CLAVE_ANIO = "dashboard.anio"
        private const val CLAVE_COMPARACION = "dashboard.tipoComparacion"
        private const val CLAVE_INDICADOR = "dashboard.tipoIndicador"
        private const val CLAVE_HISTORICO_LINEA = "dashboard.tipoHistoricoLinea"

        fun factory(repositorio: LocalPanelRepository, seleccion: SeleccionRepository? = null): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                DashboardViewModel(createSavedStateHandle(), repositorio, seleccionRepository = seleccion)
            }
        }
    }
}
