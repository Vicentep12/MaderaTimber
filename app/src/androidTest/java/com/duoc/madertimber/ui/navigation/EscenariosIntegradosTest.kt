package com.duoc.madertimber.ui.navigation

import androidx.compose.runtime.remember
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.test.platform.app.InstrumentationRegistry
import com.duoc.madertimber.data.repository.LocalPanelRepository
import com.duoc.madertimber.ui.screens.auth.LoginViewModel
import com.duoc.madertimber.ui.screens.auth.RegisterViewModel
import com.duoc.madertimber.ui.screens.dashboard.DashboardViewModel
import com.google.gson.JsonParser
import org.junit.Rule
import org.junit.Test

class EscenariosIntegradosTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun coberturaParcialYRutaInvalidaPermitenVolver() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val json = context.assets.open("cenamad_datos.json").bufferedReader().use { it.readText() }
        val raiz = JsonParser.parseString(json).asJsonObject
        raiz.getAsJsonArray("cobertura")[0].asJsonObject.getAsJsonArray("anios").add(2025)
        val datos = raiz.toString()
        lateinit var navegador: NavHostController
        composeRule.setContent {
            navegador = rememberNavController()
            val dashboard = remember {
                DashboardViewModel(SavedStateHandle(), LocalPanelRepository { datos.reader() })
            }
            AppNavigation(
                remember { LoginViewModel() }, remember { RegisterViewModel() }, dashboard,
                navController = navegador
            )
        }
        composeRule.onNodeWithText("Explorar como invitado").performClick()
        composeRule.waitUntil(10_000) {
            composeRule.onAllNodesWithText("Filtros").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithText("Filtros").performClick()
        composeRule.onNodeWithTag("filtro-anio-2025").performScrollTo().performClick()
        composeRule.onNodeWithTag("filtro-aplicar").performClick()
        composeRule.onNodeWithContentDescription(
            "Biomateriales de madera. Proyectos activos en 2025: Sin datos."
        ).assertExists()
        composeRule.onNodeWithContentDescription(
            "Construcción sustentable. Proyectos activos en 2025: 0. Línea seleccionada."
        ).assertExists()

        composeRule.runOnUiThread { navegador.navigate(Rutas.linea("inexistente", 2025)) }
        composeRule.onNodeWithText("La línea solicitada no existe.").assertExists()
        composeRule.onNodeWithText("Volver").performClick()
        composeRule.onNodeWithText("Año: 2025").assertExists()
    }
}
