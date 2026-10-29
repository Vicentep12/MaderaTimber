package com.duoc.madertimber.ui.screens.lineas

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.duoc.madertimber.data.model.LineaInvestigacion
import com.duoc.madertimber.data.model.TipoIndicador
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DetalleLineaScreenTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun faltaDeCoberturaMuestraMensajeSinCerosNiAccesosVacios() {
        composeRule.setContent {
            DetalleLineaScreen(
                state = DetalleLineaUiState(
                    linea = LineaInvestigacion("L2", "Biomateriales de madera", "Materiales renovables", "#008000"),
                    anio = 2023,
                    indicadores = emptyList(),
                    tipoHistorico = TipoIndicador.PROYECTOS_ACTIVOS,
                    historico = null,
                    relacionados = null,
                    mensaje = "Sin datos para esta línea y año"
                ),
                onHistoricoSeleccionado = {},
                onVerIndicadorClick = {},
                onVerRelacionadosClick = {},
                onVolverClick = {}
            )
        }

        composeRule.onNodeWithText("Biomateriales de madera").assertExists()
        composeRule.onNodeWithText("Línea de investigación · 2023 · Datos demostrativos")
            .assertExists()
        composeRule.onNodeWithTag("detalle-linea-sin-datos").assertExists()
        composeRule.onNodeWithText("Ver indicador seleccionado").assertDoesNotExist()
        composeRule.onNodeWithText("Ver contenidos relacionados").assertDoesNotExist()
    }
}
