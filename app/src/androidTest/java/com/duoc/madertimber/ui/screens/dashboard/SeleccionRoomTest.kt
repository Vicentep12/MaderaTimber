package com.duoc.madertimber.ui.screens.dashboard

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.lifecycle.SavedStateHandle
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.duoc.madertimber.data.model.TipoIndicador
import com.duoc.madertimber.data.repository.LocalPanelRepository
import com.duoc.madertimber.data.repository.SeleccionDatabase
import com.duoc.madertimber.data.repository.SeleccionGuardada
import com.duoc.madertimber.data.repository.SeleccionRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class SeleccionRoomTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun guardaRecuperaYDescartaSeleccionInvalida() {
        val contexto = InstrumentationRegistry.getInstrumentation().targetContext
        val base = Room.inMemoryDatabaseBuilder(contexto, SeleccionDatabase::class.java).build()
        val seleccion = SeleccionRepository(base.seleccionDao())
        try {
            lateinit var primero: DashboardViewModel
            composeRule.runOnUiThread {
                primero = DashboardViewModel(SavedStateHandle(), LocalPanelRepository(contexto),
                    seleccionRepository = seleccion)
            }
            composeRule.waitUntil(10_000) { primero.uiState.estado == EstadoDashboard.LISTO }
            composeRule.runOnUiThread {
                primero.onEvent(DashboardEvent.FiltrosAplicados("L2", 2023))
                primero.onEvent(DashboardEvent.MetricaComparada(TipoIndicador.PUBLICACIONES_GENERADAS))
            }
            composeRule.waitUntil(10_000) {
                runBlocking { seleccion.leer()?.metrica == "PUBLICACIONES_GENERADAS" }
            }
            lateinit var segundo: DashboardViewModel
            composeRule.runOnUiThread {
                segundo = DashboardViewModel(SavedStateHandle(), LocalPanelRepository(contexto),
                    seleccionRepository = seleccion)
            }
            composeRule.waitUntil(10_000) { segundo.uiState.estado == EstadoDashboard.LISTO }
            assertEquals("L2", segundo.uiState.lineaId)
            assertEquals(2023, segundo.uiState.anio)
            assertEquals(TipoIndicador.PUBLICACIONES_GENERADAS, segundo.uiState.tipoComparacion)

            runBlocking { seleccion.guardar(SeleccionGuardada(lineaId = "invalida", anio = 1900, metrica = "invalida")) }
            lateinit var tercero: DashboardViewModel
            composeRule.runOnUiThread {
                tercero = DashboardViewModel(SavedStateHandle(), LocalPanelRepository(contexto),
                    seleccionRepository = seleccion)
            }
            composeRule.waitUntil(10_000) { tercero.uiState.estado == EstadoDashboard.LISTO }
            assertEquals("L1", tercero.uiState.lineaId)
            assertEquals(2024, tercero.uiState.anio)
            assertEquals(TipoIndicador.PROYECTOS_ACTIVOS, tercero.uiState.tipoComparacion)
        } finally {
            base.close()
        }
    }
}
