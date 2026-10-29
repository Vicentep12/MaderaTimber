package com.duoc.madertimber

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.duoc.madertimber.ui.screens.auth.LoginViewModel
import com.duoc.madertimber.ui.screens.auth.RegisterViewModel
import androidx.activity.compose.BackHandler
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.key
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import com.duoc.madertimber.ui.screens.auth.LoginScreen
import com.duoc.madertimber.ui.screens.auth.RegisterScreen
import com.duoc.madertimber.ui.screens.dashboard.DashboardScreen
import com.duoc.madertimber.ui.screens.dashboard.DashboardViewModel
import com.duoc.madertimber.data.repository.LocalPanelRepository
import com.duoc.madertimber.ui.theme.MaderTimberTheme
import com.duoc.madertimber.ui.screens.lineas.DetalleLineaScreen
import com.duoc.madertimber.ui.screens.proyectos.FichaProyectosScreen
import com.duoc.madertimber.ui.screens.proyectos.ListaProyectosScreen
import com.duoc.madertimber.ui.screens.publicaciones.FichaPublicacionScreen
import com.duoc.madertimber.ui.screens.publicaciones.ListaPublicacionesScreen


class MainActivity : ComponentActivity() {
    private val dashboardViewModel: DashboardViewModel by viewModels {
        DashboardViewModel.factory(LocalPanelRepository(applicationContext))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        val registerViewModel = ViewModelProvider(this)[RegisterViewModel::class.java]
        enableEdgeToEdge()
        setContent {
            MaderTimberTheme {
                var screen by rememberSaveable { mutableStateOf("login") }
                fun navigate(destination: String) {
                    loginViewModel.reset()
                    registerViewModel.reset()
                    screen = destination
                }
                BackHandler(enabled = screen != "login") { navigate("login") }
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    key(screen) {
                        val modifier = Modifier.padding(innerPadding)
                        when (screen) {
                            "register" -> RegisterScreen(
                                state = registerViewModel.uiState,
                                onEvent = registerViewModel::onEvent,
                                onLogin = { navigate("login") },
                                onGuest = { navigate("dashboard") },
                                modifier = modifier
                            )
                            "dashboard" -> DashboardScreen(
                                state = dashboardViewModel.uiState,
                                onEvent = dashboardViewModel::onEvent,
                                modifier = modifier
                            )
                            "lista-publicaciones" -> ListaPublicacionesScreen(
                                onVolverClick = {navigate("dashboard")},
                                onPublicacionClick = {navigate("dashboard")},
                                modifier = modifier

                            )

                            else -> LoginScreen(
                                state = loginViewModel.uiState,
                                onEvent = loginViewModel::onEvent,
                                onRegister = { navigate("register") },
                                onGuest = { navigate("dashboard") },
                                modifier = modifier
                            )
                        }
                    }
                }
            }
        }
    }
}
