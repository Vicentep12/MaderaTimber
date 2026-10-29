package com.duoc.madertimber.ui.navigation

import android.graphics.Bitmap
import android.os.SystemClock
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performSemanticsAction
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import com.duoc.madertimber.MainActivity
import java.io.File
import org.junit.Rule
import org.junit.Test

/** Recorrido reproducible de T15. Las capturas requieren inspección visual adicional. */
class RevisionVisualTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun capturaLasDoceVistasYCompruebaAccionesAlDesplazarse() {
        captura("11-login")
        pulsar("No tengo cuenta · Registrarme")
        captura("12-registro")
        composeRule.onNodeWithContentDescription("Mostrar: Confirma tu contraseña")
            .performScrollTo().performClick()
        composeRule.onNodeWithContentDescription("Ocultar: Confirma tu contraseña").assertExists()
        composeRule.onNodeWithContentDescription("Mostrar: Contraseña").assertExists()
        composeRule.onNodeWithContentDescription("Ocultar: Confirma tu contraseña").performClick()
        ver("Crear cuenta")
        captura("12-registro-inferior")
        pulsar("Explorar como invitado")
        composeRule.waitUntil(10_000) {
            composeRule.onAllNodesWithText("Filtros").fetchSemanticsNodes().isNotEmpty()
        }
        inicio()
        captura("01-dashboard")
        pulsar("Filtros")
        captura("02-filtros")
        composeRule.onNodeWithTag("filtro-anio-2024").performScrollTo().assertIsSelected()
        captura("02-filtros-inferior")
        pulsar("Cancelar", desplazar = false)
        ver("Año: 2024")
        captura("01-dashboard-tarjetas")
        composeRule.onNode(hasText("Impacto estimado") and hasText("78,0 pts"))
            .performScrollTo().assertIsDisplayed()
        captura("01-dashboard-indicadores")
        ver("Ver línea")
        captura("01-dashboard-comparacion")
        pulsar("Ver línea")
        captura("03-linea")
        composeRule.onNodeWithTag("grafico-historico").performScrollTo()
        captura("03-linea-historico")
        pulsar("Ver indicador seleccionado")
        captura("04-indicador")
        ver("Cómo interpretarlo")
        captura("04-indicador-historico")
        pulsar("Volver")
        pulsar("Ver contenidos relacionados")
        listOf("Proyectos", "Investigadores", "Publicaciones").forEachIndexed { indice, categoria ->
            inicio()
            pulsar(categoria)
            inicio()
            captura("${5 + indice * 2}-lista-${categoria.lowercase()}")
            composeRule.onAllNodes(hasClickAction() and hasText("Ver ficha"))[0]
                .performScrollTo().performClick()
            captura("${6 + indice * 2}-ficha-${categoria.lowercase()}")
            ver("Volver a la lista")
            captura("${6 + indice * 2}-ficha-inferior-${categoria.lowercase()}")
            pulsar("Volver a la lista")
        }
        pulsar("Volver")
        pulsar("Volver al panel")
        pulsar("Cerrar sesión / Salir")
        pulsar("Iniciar sesión")
        ver("Ingresa un correo válido.")
        captura("11-login-errores")
        composeRule.onNodeWithText("Correo electrónico").performScrollTo()
            .performClick().performTextInput("estudiante@example.com")
        captura("11-login-teclado")
    }

    private fun ver(texto: String) {
        composeRule.onNodeWithText(texto).performScrollTo().assertIsDisplayed()
    }

    private fun pulsar(texto: String, desplazar: Boolean = true) {
        if (desplazar) ver(texto)
        composeRule.onNodeWithText(texto).performClick()
    }

    private fun inicio() {
        composeRule.onAllNodes(SemanticsMatcher.keyIsDefined(SemanticsProperties.VerticalScrollAxisRange))[0]
            .performSemanticsAction(SemanticsActions.ScrollBy) { it(0f, -100_000f) }
    }

    private fun captura(nombre: String) {
        composeRule.waitForIdle()
        // UiAutomation captura la ventana: esperar también al compositor de Android/IME.
        SystemClock.sleep(700)
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val perfil = InstrumentationRegistry.getArguments().getString("perfil") ?: "normal"
        val carpeta = File(instrumentation.targetContext.getExternalFilesDir(null), "t15/$perfil")
        check(carpeta.mkdirs() || carpeta.isDirectory)
        val bitmap = checkNotNull(instrumentation.uiAutomation.takeScreenshot())
        File(carpeta, "$nombre.png").outputStream().use {
            check(bitmap.compress(Bitmap.CompressFormat.PNG, 100, it))
        }
        bitmap.recycle()
    }
}
