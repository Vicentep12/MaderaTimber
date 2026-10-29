package com.duoc.madertimber.ui.screens.dashboard

import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.duoc.madertimber.data.model.LineaInvestigacion
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DashboardScreenTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun anunciaImagenYAusenciaYEmiteSeleccionDeLinea() {
        val lineas = listOf(
            LineaInvestigacion("L1", "Construcción sustentable", "A", "#008000"),
            LineaInvestigacion("L2", "Biomateriales de madera", "B", "#008000"),
            LineaInvestigacion("L3", "Procesos industriales", "C", "#008000")
        )
        var ultimoEvento: DashboardEvent? = null
        composeRule.setContent {
            DashboardScreen(
                state = DashboardUiState(
                    estado = EstadoDashboard.LISTO,
                    lineaId = "L1",
                    lineas = lineas,
                    anio = 2024,
                    aniosDisponibles = listOf(2022, 2023, 2024),
                    barras = listOf(
                        BarraIndicador(lineas[0], 8.0, 0.8f, true),
                        BarraIndicador(lineas[1], null, 0f, false),
                        BarraIndicador(lineas[2], 10.0, 1f, false)
                    )
                ),
                onEvent = { ultimoEvento = it }
            )
        }

        composeRule.onNodeWithContentDescription(
            "Paneles de madera, una planta joven y un edificio de madera en un paisaje montañoso"
        ).assertExists()
        composeRule.onNodeWithContentDescription(
            "Biomateriales de madera. Proyectos activos en 2024: Sin datos."
        ).assertExists()
        composeRule.onNodeWithText("Biomateriales de madera").performScrollTo().performClick()
        composeRule.runOnIdle {
            assertEquals(DashboardEvent.LineaSeleccionada("L2"), ultimoEvento)
        }
    }
}
