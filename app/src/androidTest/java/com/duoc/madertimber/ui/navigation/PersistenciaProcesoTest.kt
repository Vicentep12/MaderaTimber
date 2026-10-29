package com.duoc.madertimber.ui.navigation

import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.platform.app.InstrumentationRegistry
import com.duoc.madertimber.MainActivity
import org.junit.Assume.assumeTrue
import org.junit.Rule
import org.junit.Test

class PersistenciaProcesoTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun seleccionEntreProcesos() {
        val fase = InstrumentationRegistry.getArguments().getString("fase")
        assumeTrue(fase == "guardar" || fase == "leer")
        composeRule.onNodeWithText("Explorar como invitado").performClick()
        composeRule.waitUntil(10_000) {
            composeRule.onAllNodesWithText("Filtros").fetchSemanticsNodes().isNotEmpty()
        }
        if (fase == "guardar") {
            composeRule.onNodeWithText("Filtros").performClick()
            composeRule.onNodeWithTag("filtro-linea-L2").performClick()
            composeRule.onNodeWithTag("filtro-anio-2023").performClick()
            composeRule.onNodeWithTag("filtro-aplicar").performClick()
            composeRule.onNodeWithText("Comparación entre líneas").performScrollTo()
            composeRule.onNodeWithText("Publicaciones generadas").performScrollTo().performClick()
            composeRule.onNodeWithText("Publicaciones generadas").assertIsSelected()
            // Espera hasta observar la escritura confirmada en Room.
            val context = InstrumentationRegistry.getInstrumentation().targetContext
            val repo = com.duoc.madertimber.data.repository.SeleccionRepository(
                com.duoc.madertimber.data.repository.SeleccionDatabase.obtener(context).seleccionDao()
            )
            composeRule.waitUntil(10_000) {
                kotlinx.coroutines.runBlocking {
                    repo.leer()?.let { it.lineaId == "L2" && it.anio == 2023 &&
                        it.metrica == "PUBLICACIONES_GENERADAS" } == true
                }
            }
        } else {
            composeRule.onNodeWithText("Año: 2023").assertExists()
            composeRule.onNodeWithText("Comparación entre líneas").performScrollTo()
            composeRule.onNodeWithText("Publicaciones generadas en 2023 · publicaciones")
                .assertExists()
            composeRule.onNodeWithText("Publicaciones generadas").assertIsSelected()
            composeRule.onNodeWithText("Ver línea").performScrollTo().performClick()
            composeRule.onNodeWithText("Biomateriales de madera").assertExists()
            composeRule.activityRule.scenario.recreate()
            composeRule.onNodeWithText("Biomateriales de madera").assertExists()
        }
    }
}
