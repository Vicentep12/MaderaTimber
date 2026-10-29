package com.duoc.madertimber.ui.screens.dashboard

import androidx.lifecycle.SavedStateHandle
import com.duoc.madertimber.data.model.TipoIndicador
import com.duoc.madertimber.data.repository.LocalPanelRepository
import java.io.StringReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DashboardViewModelTest {
    @Test
    fun detalleLineaSeleccionaHistoricoSinCambiarFiltrosYLoRestaura() = ejecutar {
        val guardado = SavedStateHandle()
        val repositorio = LocalPanelRepository { StringReader(datosDePrueba()) }
        val modelo = DashboardViewModel(guardado, repositorio, StandardTestDispatcher(testScheduler))
        advanceUntilIdle()

        val inicial = modelo.uiState.detalleLinea!!
        assertEquals(5, inicial.indicadores.size)
        assertEquals(TipoIndicador.PROYECTOS_ACTIVOS, inicial.tipoHistorico)
        assertEquals("Proyectos activos", inicial.historico?.titulo)

        modelo.onEvent(DashboardEvent.HistoricoLineaSeleccionado(TipoIndicador.PUBLICACIONES_GENERADAS))
        assertEquals("L1", modelo.uiState.lineaId)
        assertEquals(2024, modelo.uiState.anio)
        assertEquals(TipoIndicador.PUBLICACIONES_GENERADAS, modelo.uiState.detalleLinea?.tipoHistorico)
        assertEquals("Publicaciones generadas", modelo.uiState.detalleLinea?.historico?.titulo)
        assertEquals("PUBLICACIONES_GENERADAS", guardado.get<String>("dashboard.tipoHistoricoLinea"))

        val restaurado = DashboardViewModel(
            guardado,
            repositorio,
            StandardTestDispatcher(testScheduler)
        )
        advanceUntilIdle()
        assertEquals(TipoIndicador.PUBLICACIONES_GENERADAS, restaurado.uiState.detalleLinea?.tipoHistorico)
        assertEquals("Publicaciones generadas", restaurado.uiState.detalleLinea?.historico?.titulo)

        modelo.onEvent(DashboardEvent.FiltrosAplicados("L2", 2023))
        assertEquals(EstadoDashboard.SIN_COBERTURA, modelo.uiState.estado)
        assertTrue(modelo.uiState.detalleLinea!!.indicadores.isEmpty())
        assertNull(modelo.uiState.detalleLinea?.historico)
        assertEquals("Sin datos para esta línea y año", modelo.uiState.detalleLinea?.mensaje)
    }

    @Test
    fun cargaDatosEnSegundoPlanoYComienzaEnLaLineaYAnioPrevistos() = ejecutar {
        val modelo = DashboardViewModel(
            SavedStateHandle(),
            LocalPanelRepository { StringReader(datosDePrueba()) },
            StandardTestDispatcher(testScheduler)
        )

        assertEquals(EstadoDashboard.CARGANDO, modelo.uiState.estado)
        advanceUntilIdle()
        assertEquals(EstadoDashboard.LISTO, modelo.uiState.estado)
        assertEquals("L1", modelo.uiState.lineaId)
        assertEquals(2024, modelo.uiState.anio)
        assertEquals(TipoIndicador.PROYECTOS_ACTIVOS, modelo.uiState.tipoComparacion)
        assertEquals(2, modelo.uiState.lineas.size)
        assertEquals(listOf(2023, 2024), modelo.uiState.aniosDisponibles)
        assertEquals(5, modelo.uiState.indicadores.size)
        assertEquals(1.0, modelo.uiState.indicadorSeleccionado?.valorSeleccionado!!, 0.0)
        assertEquals(2, modelo.uiState.barras.size)
        assertTrue(modelo.uiState.barras.first().seleccionada)
    }

    @Test
    fun cambiaFiltrosYSenalaFaltaDeCoberturaSinInventarCeros() = ejecutar {
        val guardado = SavedStateHandle()
        val modelo = DashboardViewModel(
            guardado,
            LocalPanelRepository { StringReader(datosDePrueba()) },
            StandardTestDispatcher(testScheduler)
        )
        advanceUntilIdle()

        modelo.onEvent(DashboardEvent.AnioSeleccionado(2023))
        assertEquals(EstadoDashboard.LISTO, modelo.uiState.estado)
        assertEquals(2023, modelo.uiState.anio)
        assertNull(modelo.uiState.barras.single { it.linea.id == "L2" }.valor)

        modelo.onEvent(DashboardEvent.LineaSeleccionada("L2"))
        assertEquals(EstadoDashboard.SIN_COBERTURA, modelo.uiState.estado)
        assertEquals(2023, modelo.uiState.anio)
        assertTrue(modelo.uiState.indicadores.isEmpty())

        modelo.onEvent(DashboardEvent.AnioSeleccionado(2024))
        assertEquals(EstadoDashboard.LISTO, modelo.uiState.estado)
        assertEquals("L2", modelo.uiState.lineaId)
        modelo.onEvent(DashboardEvent.MetricaComparada(TipoIndicador.IMPACTO_ESTIMADO))
        assertEquals(TipoIndicador.IMPACTO_ESTIMADO, modelo.uiState.tipoComparacion)
        assertEquals(2, modelo.uiState.barras.size)
        assertEquals("L2", guardado.get<String>("dashboard.lineaId"))
        assertEquals(2024, guardado.get<Int>("dashboard.anio"))
        assertEquals("IMPACTO_ESTIMADO", guardado.get<String>("dashboard.tipoComparacion"))
    }

    @Test
    fun aplicaAmbosFiltrosDeUnaVezYLosRestauraSinCambiarLaMetrica() = ejecutar {
        val guardado = SavedStateHandle()
        val repositorio = LocalPanelRepository { StringReader(datosDePrueba()) }
        val modelo = DashboardViewModel(guardado, repositorio, StandardTestDispatcher(testScheduler))
        advanceUntilIdle()
        modelo.onEvent(DashboardEvent.MetricaComparada(TipoIndicador.IMPACTO_ESTIMADO))

        modelo.onEvent(DashboardEvent.FiltrosAplicados("L2", 2023))
        assertEquals("L2", modelo.uiState.lineaId)
        assertEquals(2023, modelo.uiState.anio)
        assertEquals(EstadoDashboard.SIN_COBERTURA, modelo.uiState.estado)
        assertEquals(TipoIndicador.IMPACTO_ESTIMADO, modelo.uiState.tipoComparacion)
        assertEquals("L2", guardado.get<String>("dashboard.lineaId"))
        assertEquals(2023, guardado.get<Int>("dashboard.anio"))

        modelo.onEvent(DashboardEvent.FiltrosAplicados("L9", 2024))
        assertEquals("L2", modelo.uiState.lineaId)
        assertEquals(2023, modelo.uiState.anio)

        val restaurado = DashboardViewModel(
            guardado,
            LocalPanelRepository { StringReader(datosDePrueba()) },
            StandardTestDispatcher(testScheduler)
        )
        advanceUntilIdle()
        assertEquals("L2", restaurado.uiState.lineaId)
        assertEquals(2023, restaurado.uiState.anio)
        assertEquals(TipoIndicador.IMPACTO_ESTIMADO, restaurado.uiState.tipoComparacion)
    }

    @Test
    fun restauraSeleccionGuardadaYPreparaIndicadorYRelacionados() = ejecutar {
        val guardado = SavedStateHandle(mapOf(
            "dashboard.lineaId" to "L1",
            "dashboard.anio" to 2023,
            "dashboard.tipoComparacion" to "INVESTIGADORES_ASOCIADOS",
            "dashboard.tipoIndicador" to "PUBLICACIONES_GENERADAS"
        ))
        val modelo = DashboardViewModel(
            guardado,
            LocalPanelRepository { StringReader(datosDePrueba()) },
            StandardTestDispatcher(testScheduler)
        )
        advanceUntilIdle()

        assertEquals(EstadoDashboard.LISTO, modelo.uiState.estado)
        assertEquals("L1", modelo.uiState.lineaId)
        assertEquals(2023, modelo.uiState.anio)
        assertEquals(TipoIndicador.INVESTIGADORES_ASOCIADOS, modelo.uiState.tipoComparacion)
        assertEquals(TipoIndicador.PUBLICACIONES_GENERADAS, modelo.uiState.tipoIndicador)
        assertEquals(0.0, modelo.uiState.indicadorSeleccionado?.valorSeleccionado!!, 0.0)
        assertTrue(modelo.uiState.relacionados?.estaVacio == true)

        modelo.onEvent(DashboardEvent.IndicadorSeleccionado(TipoIndicador.PROYECTOS_ACTIVOS))
        assertEquals("PROYECTOS_ACTIVOS", guardado.get<String>("dashboard.tipoIndicador"))
        assertEquals(listOf("P1"), modelo.uiState.relacionados?.proyectos?.map { it.id })
    }

    @Test
    fun ignoraFiltrosInvalidosYRestauraValoresPredeterminados() = ejecutar {
        val guardado = SavedStateHandle(mapOf(
            "dashboard.lineaId" to "L9",
            "dashboard.anio" to 2030,
            "dashboard.tipoComparacion" to "DESCONOCIDO"
        ))
        val modelo = DashboardViewModel(
            guardado,
            LocalPanelRepository { StringReader(datosDePrueba()) },
            StandardTestDispatcher(testScheduler)
        )
        advanceUntilIdle()

        assertEquals("L1", modelo.uiState.lineaId)
        assertEquals(2024, modelo.uiState.anio)
        assertEquals(TipoIndicador.PROYECTOS_ACTIVOS, modelo.uiState.tipoComparacion)
        modelo.onEvent(DashboardEvent.LineaSeleccionada("L9"))
        modelo.onEvent(DashboardEvent.AnioSeleccionado(2030))
        assertEquals("L1", modelo.uiState.lineaId)
        assertEquals(2024, modelo.uiState.anio)
        assertEquals("L1", guardado.get<String>("dashboard.lineaId"))
        assertEquals(2024, guardado.get<Int>("dashboard.anio"))
    }

    @Test
    fun distingueErrorYPermiteReintentarLaCarga() = ejecutar {
        var contenido = "{json inválido}"
        val modelo = DashboardViewModel(
            SavedStateHandle(),
            LocalPanelRepository { StringReader(contenido) },
            StandardTestDispatcher(testScheduler)
        )
        advanceUntilIdle()
        assertEquals(EstadoDashboard.ERROR, modelo.uiState.estado)
        assertTrue(modelo.uiState.mensaje != null)

        contenido = datosDePrueba()
        modelo.onEvent(DashboardEvent.ReintentarCarga)
        assertEquals(EstadoDashboard.CARGANDO, modelo.uiState.estado)
        advanceUntilIdle()
        assertEquals(EstadoDashboard.LISTO, modelo.uiState.estado)
        assertNull(modelo.uiState.mensaje)
    }

    @Test
    fun conjuntoSinLineasMuestraEstadoVacio() = ejecutar {
        val modelo = DashboardViewModel(
            SavedStateHandle(),
            LocalPanelRepository {
                StringReader("""{"lineas":[],"cobertura":[],"proyectos":[],"investigadores":[],"publicaciones":[]}""")
            },
            StandardTestDispatcher(testScheduler)
        )
        advanceUntilIdle()
        assertEquals(EstadoDashboard.VACIO, modelo.uiState.estado)
        assertTrue(modelo.uiState.indicadores.isEmpty())
    }

    private fun ejecutar(bloque: suspend TestScope.() -> Unit) = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        try {
            bloque()
        } finally {
            Dispatchers.resetMain()
        }
    }

    private fun datosDePrueba() = """{
        "lineas":[
            {"id":"L1","nombre":"Construcción","descripcion":"Ejemplo","color":"#008000"},
            {"id":"L2","nombre":"Biomateriales","descripcion":"Ejemplo","color":"#0000FF"}
        ],
        "cobertura":[
            {"lineaId":"L1","anios":[2023,2024]},
            {"lineaId":"L2","anios":[2024]}
        ],
        "proyectos":[
            {"id":"P1","lineaId":"L1","titulo":"Proyecto 1","seguimientos":[
                {"anio":2023,"estado":"ACTIVO","avancePorcentaje":40.0},
                {"anio":2024,"estado":"ACTIVO","avancePorcentaje":50.0}
            ]},
            {"id":"P2","lineaId":"L2","titulo":"Proyecto 2","seguimientos":[
                {"anio":2024,"estado":"ACTIVO","avancePorcentaje":80.0}
            ]}
        ],
        "investigadores":[
            {"id":"I1","nombre":"Investigador","especialidad":"Madera",
             "vinculaciones":[{"lineaId":"L1","anio":2024}]}
        ],
        "publicaciones":[
            {"id":"U1","lineaId":"L1","titulo":"Publicación","tipo":"Artículo","anio":2024}
        ]
    }"""
}
