package com.duoc.madertimber.ui.navigation

import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.duoc.madertimber.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppNavigationTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun vuelveAlPanelConElAnioSeleccionadoTrasAbrirRelacionados() {
        composeRule.onNodeWithText("Explorar como invitado").performClick()
        composeRule.waitUntil(10_000) {
            composeRule.onAllNodesWithText("2023").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithText("2023").performScrollTo().performClick()
        composeRule.onNodeWithText("Ver línea").performScrollTo().performClick()
        composeRule.onNodeWithText("Línea de investigación · 2023", substring = true).assertExists()
        composeRule.onNodeWithText("Ver contenidos relacionados").performScrollTo().performClick()
        composeRule.onNodeWithText("Contenidos relacionados").assertExists()
        composeRule.onNodeWithText("Volver").performScrollTo().performClick()
        composeRule.onNodeWithText("Volver al panel").performScrollTo().performClick()
        composeRule.onNodeWithText("Año: 2023").assertExists()
    }

    @Test
    fun filtrosDelModalSeConservanAlVolverYRecrearLaActividad() {
        composeRule.onNodeWithText("Explorar como invitado").performClick()
        composeRule.waitUntil(10_000) {
            composeRule.onAllNodesWithText("Filtros").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithText("Filtros").performClick()
        composeRule.onNodeWithTag("filtro-linea-L2").performClick()
        composeRule.onNodeWithTag("filtro-anio-2023").performClick()
        composeRule.onNodeWithTag("filtro-aplicar").performClick()

        composeRule.onNodeWithText("Año: 2023").assertExists()
        composeRule.onNodeWithText("Ver línea").performScrollTo().performClick()
        composeRule.onNodeWithText("Biomateriales de madera").assertExists()
        composeRule.onNodeWithText("Línea de investigación · 2023", substring = true).assertExists()
        composeRule.onNodeWithText("Volver al panel").performScrollTo().performClick()
        composeRule.onNodeWithText("Año: 2023").assertExists()

        composeRule.activityRule.scenario.recreate()
        composeRule.waitUntil(10_000) {
            composeRule.onAllNodesWithText("Año: 2023").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithText("Año: 2023").assertExists()
    }

    @Test
    fun detalleIndicadorMuestraHistoricoCalculado() {
        composeRule.onNodeWithText("Explorar como invitado").performClick()
        composeRule.waitUntil(10_000) {
            composeRule.onAllNodesWithText("Ver línea").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithText("Ver línea").performScrollTo().performClick()
        composeRule.onNodeWithText("Ver indicador seleccionado").performScrollTo().performClick()

        composeRule.onNodeWithTag("grafico-historico").performScrollTo().assertExists()
        composeRule.onNodeWithText("Evolución de Proyectos activos").assertExists()
        composeRule.onNodeWithText("2024 *").assertExists()
    }

    @Test
    fun detalleLineaMuestraCincoIndicadoresYCambiaHistoricoConNavegacion() {
        composeRule.onNodeWithText("Explorar como invitado").performClick()
        composeRule.waitUntil(10_000) {
            composeRule.onAllNodesWithText("Ver línea").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithText("Ver línea").performScrollTo().performClick()

        composeRule.onNodeWithText("Indicadores de 2024").assertExists()
        listOf(
            "PROYECTOS_ACTIVOS", "PUBLICACIONES_GENERADAS", "INVESTIGADORES_ASOCIADOS",
            "AVANCE_PROMEDIO", "IMPACTO_ESTIMADO"
        ).forEach { tipo ->
            composeRule.onNodeWithTag("indicador-linea-$tipo").assertExists()
        }
        composeRule.onNodeWithText("Proyectos asociados: 8").assertExists()
        composeRule.onNodeWithText("Investigadores asociados: 12").assertExists()
        composeRule.onNodeWithText("Publicaciones relacionadas: 15").assertExists()

        composeRule.onNodeWithTag("grafico-historico").performScrollTo()
        composeRule.onNodeWithTag("historico-linea-PUBLICACIONES_GENERADAS")
            .performScrollTo().performClick()
        composeRule.onNodeWithTag("historico-linea-PUBLICACIONES_GENERADAS").assertIsSelected()
        composeRule.onNodeWithTag("grafico-historico").performScrollTo()
        composeRule.onNodeWithText("Evolución de Publicaciones generadas").assertExists()
        composeRule.onNodeWithText("Ver indicador seleccionado").performScrollTo().performClick()
        composeRule.onNodeWithText("Publicaciones generadas").assertExists()
        composeRule.onNodeWithText("2024 · Datos demostrativos").assertExists()
        composeRule.onNodeWithText("Volver").performScrollTo().performClick()

        composeRule.onNodeWithText("Ver contenidos relacionados").performScrollTo().performClick()
        composeRule.onNodeWithText("Contenidos relacionados").assertExists()
    }
}
