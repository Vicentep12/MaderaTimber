package com.duoc.madertimber.ui.navigation

import androidx.compose.ui.test.onAllNodesWithText
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
}
