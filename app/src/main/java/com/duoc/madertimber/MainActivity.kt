package com.duoc.madertimber

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.duoc.madertimber.data.repository.LocalPanelRepository
import com.duoc.madertimber.data.repository.SeleccionDatabase
import com.duoc.madertimber.data.repository.SeleccionRepository
import com.duoc.madertimber.ui.navigation.AppNavigation
import com.duoc.madertimber.ui.screens.auth.LoginViewModel
import com.duoc.madertimber.ui.screens.auth.RegisterViewModel
import com.duoc.madertimber.ui.screens.dashboard.DashboardViewModel
import com.duoc.madertimber.ui.theme.MaderTimberTheme

class MainActivity : ComponentActivity() {
    private val dashboardViewModel: DashboardViewModel by viewModels {
        DashboardViewModel.factory(
            LocalPanelRepository(applicationContext),
            SeleccionRepository(SeleccionDatabase.obtener(applicationContext).seleccionDao())
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        val registerViewModel = ViewModelProvider(this)[RegisterViewModel::class.java]
        enableEdgeToEdge()
        setContent {
            MaderTimberTheme {
                AppNavigation(loginViewModel, registerViewModel, dashboardViewModel)
            }
        }
    }
}
