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
import com.duoc.madertimber.data.model.IndicadorCalculado
import com.duoc.madertimber.data.model.LineaInvestigacion
import com.duoc.madertimber.data.model.TipoIndicador
import com.duoc.madertimber.data.repository.EstadoCargaPanel
import com.duoc.madertimber.data.repository.LocalPanelRepository
import com.duoc.madertimber.data.repository.ResultadoConsultaPanel
import com.duoc.madertimber.data.repository.ResultadoIndicadoresPanel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
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
    val lineas: List<LineaInvestigacion> = emptyList(),
    val aniosDisponibles: List<Int> = emptyList(),
    val indicadores: List<IndicadorCalculado> = emptyList(),
    val barras: List<BarraIndicador> = emptyList(),
    val relacionados: DatosRelacionados? = null,
    val mensaje: String? = null
) {
    val lineaSeleccionada: LineaInvestigacion?
        get() = lineas.find { it.id == lineaId }

    val indicadorSeleccionado: IndicadorCalculado?
        get() = indicadores.find { it.indicador.tipo == tipoIndicador }
}

sealed interface DashboardEvent {
    data class LineaSeleccionada(val id: String) : DashboardEvent
    data class AnioSeleccionado(val anio: Int) : DashboardEvent
    data class MetricaComparada(val tipo: TipoIndicador) : DashboardEvent
    data class IndicadorSeleccionado(val tipo: TipoIndicador) : DashboardEvent
    data object ReintentarCarga : DashboardEvent
}

class DashboardViewModel(
    private val guardado: SavedStateHandle,
    private val repositorio: LocalPanelRepository,
    private val dispatcherCarga: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {
    private var carga: Job? = null

    var uiState by mutableStateOf(
        DashboardUiState(
            lineaId = guardado[CLAVE_LINEA],
            anio = guardado[CLAVE_ANIO] ?: 2024,
            tipoComparacion = tipoGuardado(CLAVE_COMPARACION),
            tipoIndicador = tipoGuardado(CLAVE_INDICADOR)
        )
    )
        private set

    init {
        cargar()
    }

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
            is DashboardEvent.MetricaComparada -> {
                guardado[CLAVE_COMPARACION] = evento.tipo.name
                uiState = uiState.copy(tipoComparacion = evento.tipo)
            }
            is DashboardEvent.IndicadorSeleccionado -> {
                guardado[CLAVE_INDICADOR] = evento.tipo.name
                uiState = uiState.copy(tipoIndicador = evento.tipo)
            }
            DashboardEvent.ReintentarCarga -> Unit
        }
        actualizarConsulta()
    }

    private fun cargar() {
        carga?.cancel()
        uiState = uiState.copy(estado = EstadoDashboard.CARGANDO, mensaje = null)
        carga = viewModelScope.launch {
            val resultado = withContext(dispatcherCarga) { repositorio.cargar() }
            when (resultado) {
                is EstadoCargaPanel.Listo -> prepararDatos(resultado)
                EstadoCargaPanel.Vacio -> uiState = uiState.copy(
                    estado = EstadoDashboard.VACIO,
                    lineas = emptyList(),
                    indicadores = emptyList(),
                    barras = emptyList(),
                    relacionados = null
                )
                is EstadoCargaPanel.Error -> uiState = uiState.copy(
                    estado = EstadoDashboard.ERROR,
                    indicadores = emptyList(),
                    barras = emptyList(),
                    relacionados = null,
                    mensaje = resultado.mensaje
                )
                EstadoCargaPanel.Cargando -> Unit
            }
        }
    }

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
        uiState = uiState.copy(
            lineaId = lineaId,
            anio = anio,
            lineas = lineas,
            aniosDisponibles = anios
        )
        actualizarConsulta()
    }

    private fun actualizarConsulta() {
        val lineaId = uiState.lineaId ?: return
        val consulta = repositorio.consultarIndicadores(lineaId, uiState.anio)
        val barras = compararLineas()
        when (consulta) {
            is ResultadoIndicadoresPanel.Disponible -> {
                val relacionados = when (val resultado = repositorio.consultarRelacionados(
                    lineaId, uiState.anio, uiState.tipoIndicador
                )) {
                    is ResultadoConsultaPanel.Disponible -> resultado.datos
                    is ResultadoConsultaPanel.Vacio -> resultado.datos
                    else -> null
                }
                uiState = uiState.copy(
                    estado = EstadoDashboard.LISTO,
                    indicadores = consulta.indicadores,
                    barras = barras,
                    relacionados = relacionados,
                    mensaje = null
                )
            }
            is ResultadoIndicadoresPanel.SinCobertura -> uiState = uiState.copy(
                estado = EstadoDashboard.SIN_COBERTURA,
                indicadores = emptyList(),
                barras = barras,
                relacionados = null,
                mensaje = "Sin datos para esta línea y año"
            )
            is ResultadoIndicadoresPanel.Error -> uiState = uiState.copy(
                estado = EstadoDashboard.ERROR,
                indicadores = emptyList(),
                barras = emptyList(),
                relacionados = null,
                mensaje = consulta.mensaje
            )
            ResultadoIndicadoresPanel.Cargando -> uiState = uiState.copy(
                estado = EstadoDashboard.CARGANDO,
                indicadores = emptyList(),
                barras = emptyList(),
                relacionados = null
            )
        }
    }

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

        fun factory(repositorio: LocalPanelRepository): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                DashboardViewModel(createSavedStateHandle(), repositorio)
            }
        }
    }
}
