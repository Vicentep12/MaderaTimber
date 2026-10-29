package com.duoc.madertimber

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.duoc.madertimber.ui.screens.auth.LoginViewModel
import com.duoc.madertimber.ui.screens.auth.RegisterViewModel
import androidx.activity.compose.BackHandler
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import com.duoc.madertimber.ui.screens.auth.LoginScreen
import com.duoc.madertimber.ui.screens.auth.RegisterScreen
import com.duoc.madertimber.ui.screens.dashboard.DashboardScreen
import com.duoc.madertimber.ui.screens.investigadores.FichaInvestigadorScreen
import com.duoc.madertimber.ui.screens.investigadores.ListaInvestigadoresScreen
import com.duoc.madertimber.ui.screens.lineas.DetalleIndicadorScreen
import com.duoc.madertimber.ui.theme.MaderTimberTheme
import com.duoc.madertimber.ui.screens.lineas.DetalleLineaScreen
import com.duoc.madertimber.ui.screens.proyectos.FichaProyectosScreen
import com.duoc.madertimber.ui.screens.proyectos.ListaProyectosScreen
import com.duoc.madertimber.ui.screens.publicaciones.FichaPublicacionScreen
import com.duoc.madertimber.ui.screens.publicaciones.ListaPublicacionesScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        val registerViewModel = ViewModelProvider(this)[RegisterViewModel::class.java]
        enableEdgeToEdge()
        setContent {
            MaderTimberTheme {
                var currentScreen by rememberSaveable { mutableStateOf("login") }

                // Pila de navegación para manejar el historial de pantallas retrocedidas
                val backStack = remember { mutableStateListOf<String>() }

                // Función central de navegación hacia adelante
                fun navigateTo(destination: String) {
                    if (destination == "login" || destination == "register") {
                        loginViewModel.reset()
                        registerViewModel.reset()
                        backStack.clear()
                    } else if (currentScreen != destination) {
                        backStack.add(currentScreen)
                    }
                    currentScreen = destination
                }

                // Función para volver a la pantalla anterior
                fun navigateBack() {
                    if (backStack.isNotEmpty()) {
                        currentScreen = backStack.removeAt(backStack.lastIndex)
                    } else {
                        navigateTo("login")
                    }
                }

                // Handler para el botón físico/gesto de volver de Android
                BackHandler(enabled = currentScreen != "login") {
                    navigateBack()
                }
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    key(currentScreen) {
                        val modifier = Modifier.padding(innerPadding)
                        when (currentScreen) {
                            "register" -> RegisterScreen(
                                state = registerViewModel.uiState,
                                onEvent = registerViewModel::onEvent,
                                onLogin = { navigateTo("login") },
                                onGuest = { navigateTo("dashboard") },
                                modifier = modifier
                            )
                            "dashboard" -> DashboardScreen(
                                onVerLineaClick = { navigateTo("detalle-linea") },
                                onVerIndicadorClick = { navigateTo("detalle-indicador") },
                                onVerProyectosClick = { navigateTo("lista-proyectos") },
                                onVerInvestigadoresClick = { navigateTo("lista-investigadores") },
                                onVerPublicacionesClick = { navigateTo("lista-publicaciones") },
                                onCerrarSesionClick = { navigateTo("login") },
                                modifier = modifier
                            )
                            "lista-publicaciones" -> ListaPublicacionesScreen(
                                onVolverClick = {navigateTo("dashboard")},
                                onPublicacionClick = {navigateTo("dashboard")},
                                modifier = modifier

                            )
                            "detalle-linea" -> DetalleLineaScreen(
                                onVolverClick = { navigateBack() },
                                modifier = modifier
                            )

                            "detalle-indicador" -> DetalleIndicadorScreen(
                                onVolverClick = { navigateBack() },
                                modifier = modifier
                            )

                            // --- Proyectos ---
                            "lista-proyectos" -> ListaProyectosScreen(
                                onProyectoClick = { navigateTo("ficha-proyecto") },
                                onVolverClick = { navigateBack() },
                                modifier = modifier
                            )

                            "ficha-proyecto" -> FichaProyectosScreen(
                                onVolverClick = { navigateBack() },
                                modifier = modifier
                            )

                            // --- Investigadores ---
                            "lista-investigadores" -> ListaInvestigadoresScreen(
                                onInvestigadorClick = { navigateTo("ficha-investigador") },
                                onVolverClick = { navigateBack() },
                                modifier = modifier
                            )

                            "ficha-investigador" -> FichaInvestigadorScreen(
                                onVolverClick = { navigateBack() },
                                modifier = modifier
                            )

                            "ficha-publicacion" -> FichaPublicacionScreen(
                                onVolverClick = { navigateBack() },
                                modifier = modifier
                            )

                            else -> LoginScreen(
                                state = loginViewModel.uiState,
                                onEvent = loginViewModel::onEvent,
                                onRegister = { navigateTo("register") },
                                onGuest = { navigateTo("dashboard") },
                                modifier = modifier
                            )
                        }
                    }
                }
            }
        }
    }
}