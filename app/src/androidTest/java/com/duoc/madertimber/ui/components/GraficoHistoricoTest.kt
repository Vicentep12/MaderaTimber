package com.duoc.madertimber.ui.components

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.duoc.madertimber.data.model.HistoricoIndicador
import com.duoc.madertimber.data.model.PuntoHistorico
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GraficoHistoricoTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun muestraHuecoUnidadYAnioSeleccionado() {
        composeRule.setContent {
            GraficoHistorico(
                HistoricoIndicador(
                    "Proyectos activos",
                    "proyectos",
                    listOf(
                        PuntoHistorico(2022, 4.0, false),
                        PuntoHistorico(2023, null, false),
                        PuntoHistorico(2024, 8.0, true)
                    )
                )
            )
        }

        composeRule.onNodeWithText("Evolución de Proyectos activos").assertExists()
        composeRule.onNodeWithText("Unidad: proyectos").assertExists()
        composeRule.onNodeWithText("2024 *").assertExists()
        composeRule.onNodeWithText("Sin datos").assertExists()
        composeRule.onNodeWithText("Resumen: 2022: 4 proyectos; 2023: Sin datos; " +
            "2024: 8 proyectos (seleccionado)").assertExists()
    }

    @Test
    fun muestraAusenciaTotalEnLugarDeUnGraficoVacio() {
        composeRule.setContent {
            GraficoHistorico(
                HistoricoIndicador(
                    "Avance promedio",
                    "%",
                    listOf(PuntoHistorico(2024, null, true))
                )
            )
        }

        composeRule.onNodeWithText("Sin datos para el período seleccionado").assertExists()
        composeRule.onNodeWithText("Resumen: 2024: Sin datos (seleccionado)").assertExists()
        composeRule.onNodeWithText("2024 *").assertDoesNotExist()
    }
}
