package com.duoc.madertimber

import android.os.Bundle
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import com.duoc.madertimber.ui.screens.auth.LoginScreen
import com.duoc.madertimber.ui.screens.auth.RegisterScreen
import com.duoc.madertimber.ui.screens.dashboard.DashboardScreen
import com.duoc.madertimber.ui.theme.MaderTimberTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaderTimberTheme {
                var screen by rememberSaveable { mutableStateOf("login") }
                BackHandler(enabled = screen != "login") { screen = "login" }
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    key(screen) {
                        val modifier = Modifier.padding(innerPadding)
                        when (screen) {
                            "register" -> RegisterScreen(
                                onLogin = { screen = "login" },
                                onGuest = { screen = "dashboard" },
                                modifier = modifier
                            )
                            "dashboard" -> DashboardScreen(modifier = modifier)
                            else -> LoginScreen(
                                onRegister = { screen = "register" },
                                onGuest = { screen = "dashboard" },
                                modifier = modifier
                            )
                        }
                    }
                }
            }
        }
    }
}
