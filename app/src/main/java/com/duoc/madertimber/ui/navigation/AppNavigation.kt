package com.duoc.madertimber.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.duoc.madertimber.data.model.CategoriaRelacionada
import com.duoc.madertimber.data.model.TipoIndicador
import com.duoc.madertimber.data.model.validarContextoRuta
import com.duoc.madertimber.data.model.validarFichaRuta
import com.duoc.madertimber.data.model.validarIndicadorRuta
import com.duoc.madertimber.ui.screens.auth.LoginScreen
import com.duoc.madertimber.ui.screens.auth.LoginViewModel
import com.duoc.madertimber.ui.screens.auth.RegisterScreen
import com.duoc.madertimber.ui.screens.auth.RegisterViewModel
import com.duoc.madertimber.ui.screens.dashboard.DashboardEvent
import com.duoc.madertimber.ui.screens.dashboard.DashboardScreen
import com.duoc.madertimber.ui.screens.dashboard.DashboardUiState
import com.duoc.madertimber.ui.screens.dashboard.DashboardViewModel
import com.duoc.madertimber.ui.screens.dashboard.EstadoDashboard
import com.duoc.madertimber.ui.screens.lineas.DetalleIndicadorScreen
import com.duoc.madertimber.ui.screens.lineas.DetalleLineaScreen
import com.duoc.madertimber.ui.screens.relacionados.FichaRelacionadaScreen
import com.duoc.madertimber.ui.screens.relacionados.RelacionadosScreen

@Composable
fun AppNavigation(
    loginViewModel: LoginViewModel,
    registerViewModel: RegisterViewModel,
    dashboardViewModel: DashboardViewModel,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val estado = dashboardViewModel.uiState

    fun limpiarAcceso() {
        loginViewModel.reset()
        registerViewModel.reset()
    }

    fun abrirDashboard() {
        limpiarAcceso()
        navController.navigate(Rutas.DASHBOARD) {
            popUpTo(Rutas.LOGIN) { inclusive = false }
            launchSingleTop = true
        }
    }

    fun volver() {
        if (!navController.popBackStack()) navController.navigate(Rutas.DASHBOARD)
    }

    Scaffold(modifier = modifier.fillMaxSize()) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Rutas.LOGIN,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Rutas.LOGIN) {
                LoginScreen(
                    state = loginViewModel.uiState,
                    onEvent = loginViewModel::onEvent,
                    onRegister = {
                        limpiarAcceso()
                        navController.navigate(Rutas.REGISTRO)
                    },
                    onGuest = ::abrirDashboard
                )
            }
            composable(Rutas.REGISTRO) {
                BackHandler(enabled = !registerViewModel.uiState.showNotice) {
                    limpiarAcceso()
                    volver()
                }
                RegisterScreen(
                    state = registerViewModel.uiState,
                    onEvent = registerViewModel::onEvent,
                    onLogin = {
                        limpiarAcceso()
                        volver()
                    },
                    onGuest = ::abrirDashboard
                )
            }
            composable(Rutas.DASHBOARD) {
                fun abrirRelacionados(tipo: TipoIndicador) {
                    val lineaId = estado.lineaId ?: return
                    dashboardViewModel.onEvent(DashboardEvent.IndicadorSeleccionado(tipo))
                    navController.navigate(Rutas.relacionados(lineaId, estado.anio, tipo))
                }

                BackHandler {
                    limpiarAcceso()
                    volver()
                }
                DashboardScreen(
                    state = estado,
                    onEvent = dashboardViewModel::onEvent,
                    onVerLineaClick = {
                        estado.lineaId?.let { navController.navigate(Rutas.linea(it, estado.anio)) }
                    },
                    onVerIndicadorClick = {
                        val impacto = estado.indicadores.find {
                            it.indicador.tipo == TipoIndicador.IMPACTO_ESTIMADO
                        }
                        if (impacto != null) {
                            dashboardViewModel.onEvent(
                                DashboardEvent.IndicadorSeleccionado(TipoIndicador.IMPACTO_ESTIMADO)
                            )
                            navController.navigate(Rutas.indicador(impacto.indicador.id, estado.anio))
                        }
                    },
                    onVerProyectosClick = { abrirRelacionados(TipoIndicador.PROYECTOS_ACTIVOS) },
                    onVerInvestigadoresClick = {
                        abrirRelacionados(TipoIndicador.INVESTIGADORES_ASOCIADOS)
                    },
                    onVerPublicacionesClick = {
                        abrirRelacionados(TipoIndicador.PUBLICACIONES_GENERADAS)
                    },
                    onCerrarSesionClick = {
                        limpiarAcceso()
                        volver()
                    }
                )
            }
            composable(
                route = Rutas.LINEA,
                arguments = listOf(
                    navArgument("lineaId") { type = NavType.StringType },
                    navArgument("anio") { type = NavType.StringType }
                )
            ) { entrada ->
                val estadoLinea = dashboardViewModel.uiState
                val lineaId = entrada.arguments?.getString("lineaId")
                val anio = entrada.arguments?.getString("anio")?.toIntOrNull()
                ContenidoContextual(estadoLinea, lineaId, anio, ::volver) {
                    val detalle = estadoLinea.detalleLinea ?: return@ContenidoContextual
                    DetalleLineaScreen(
                        state = detalle,
                        onHistoricoSeleccionado = { tipo ->
                            dashboardViewModel.onEvent(DashboardEvent.HistoricoLineaSeleccionado(tipo))
                        },
                        onVolverClick = ::volver,
                        onVerIndicadorClick = { tipo ->
                            estadoLinea.indicadores.find { it.indicador.tipo == tipo }?.let { indicador ->
                                dashboardViewModel.onEvent(DashboardEvent.IndicadorSeleccionado(tipo))
                                navController.navigate(
                                    Rutas.indicador(indicador.indicador.id, estadoLinea.anio)
                                )
                            }
                        },
                        onVerRelacionadosClick = {
                            navController.navigate(Rutas.relacionados(detalle.linea.id, estadoLinea.anio))
                        }
                    )
                }
            }
            composable(
                route = Rutas.INDICADOR,
                arguments = listOf(
                    navArgument("indicadorId") { type = NavType.StringType },
                    navArgument("anio") { type = NavType.StringType }
                )
            ) { entrada ->
                val id = entrada.arguments?.getString("indicadorId")
                val anio = entrada.arguments?.getString("anio")?.toIntOrNull()
                val indicador = estado.indicadores.find { it.indicador.id == id }
                val historico = indicador?.let { estado.historicos[it.indicador.tipo] }
                ContenidoContextual(
                    estado, indicador?.indicador?.lineaId ?: estado.lineaId, anio, ::volver
                ) {
                    val error = validarIndicadorRuta(id, estado.indicadores)
                    if (error != null || indicador == null || historico == null) {
                        ErrorRutaScreen(error ?: "El indicador solicitado no existe.", ::volver)
                    } else {
                        DetalleIndicadorScreen(
                            indicador = indicador,
                            lineaNombre = estado.lineaSeleccionada?.nombre.orEmpty(),
                            historico = historico,
                            onVolverClick = ::volver,
                            onVerRelacionadosClick = {
                                dashboardViewModel.onEvent(
                                    DashboardEvent.IndicadorSeleccionado(indicador.indicador.tipo)
                                )
                                navController.navigate(
                                    Rutas.relacionados(
                                        indicador.indicador.lineaId,
                                        estado.anio,
                                        indicador.indicador.tipo
                                    )
                                )
                            }
                        )
                    }
                }
            }
            composable(
                route = Rutas.RELACIONADOS,
                arguments = listOf(
                    navArgument("lineaId") { type = NavType.StringType },
                    navArgument("anio") { type = NavType.StringType },
                    navArgument("tipo") {
                        type = NavType.StringType
                        nullable = true
                        defaultValue = null
                    }
                )
            ) { entrada ->
                val lineaId = entrada.arguments?.getString("lineaId")
                val anio = entrada.arguments?.getString("anio")?.toIntOrNull()
                val tipoTexto = entrada.arguments?.getString("tipo")
                val tipo = TipoIndicador.entries.find { it.name == tipoTexto }
                ContenidoContextual(estado, lineaId, anio, ::volver) {
                    when {
                        tipoTexto != null && tipo == null ->
                            ErrorRutaScreen("El tipo de indicador no es válido.", ::volver)
                        tipo != null && tipo != estado.tipoIndicador ->
                            ErrorRutaScreen("La selección cambió. Vuelve al panel.", ::volver)
                        else -> {
                            val linea = estado.lineaSeleccionada ?: return@ContenidoContextual
                            val datos = if (tipo == null) estado.relacionadosSinFiltro
                                else estado.relacionados
                            RelacionadosScreen(
                                datos = datos,
                                lineaNombre = linea.nombre,
                                anio = estado.anio,
                                tipo = tipo,
                                mensaje = if (estado.estado == EstadoDashboard.SIN_COBERTURA) {
                                    estado.mensaje
                                } else {
                                    null
                                },
                                onVolverClick = ::volver,
                                onFichaClick = { categoria, itemId ->
                                    if (lineaId != null && anio != null) {
                                        navController.navigate(
                                            Rutas.ficha(categoria, itemId, lineaId, anio, tipo)
                                        )
                                    }
                                }
                            )
                        }
                    }
                }
            }
            composable(
                route = Rutas.FICHA,
                arguments = listOf(
                    navArgument("categoria") { type = NavType.StringType },
                    navArgument("itemId") { type = NavType.StringType },
                    navArgument("lineaId") { type = NavType.StringType },
                    navArgument("anio") { type = NavType.StringType },
                    navArgument("tipo") {
                        type = NavType.StringType
                        nullable = true
                        defaultValue = null
                    }
                )
            ) { entrada ->
                val argumentos = entrada.arguments
                val lineaId = argumentos?.getString("lineaId")
                val anio = argumentos?.getString("anio")?.toIntOrNull()
                val tipoTexto = argumentos?.getString("tipo")
                val tipo = TipoIndicador.entries.find { it.name == tipoTexto }
                val categoria = CategoriaRelacionada.entries.find {
                    it.name == argumentos?.getString("categoria")
                }
                val itemId = argumentos?.getString("itemId")
                ContenidoContextual(estado, lineaId, anio, ::volver) {
                    val datos = if (tipo == null) estado.relacionadosSinFiltro
                        else estado.relacionados
                    val error = if (tipoTexto != null && tipo == null) {
                        "El tipo de indicador no es válido."
                    } else if (tipo != null && tipo != estado.tipoIndicador) {
                        "La selección cambió. Vuelve al panel."
                    } else {
                        validarFichaRuta(categoria, itemId, datos, tipo)
                    }
                    if (error != null || datos == null || categoria == null || itemId == null) {
                        ErrorRutaScreen(error ?: "El contenido solicitado no existe.", ::volver)
                    } else {
                        FichaRelacionadaScreen(datos, categoria, itemId, ::volver)
                    }
                }
            }
        }
    }
}

@Composable
private fun ContenidoContextual(
    estado: DashboardUiState,
    lineaId: String?,
    anio: Int?,
    onVolver: () -> Unit,
    contenido: @Composable () -> Unit
) {
    when (estado.estado) {
        EstadoDashboard.CARGANDO -> CircularProgressIndicator()
        EstadoDashboard.ERROR -> ErrorRutaScreen(
            estado.mensaje ?: "No se pudieron cargar los datos.", onVolver
        )
        EstadoDashboard.VACIO -> ErrorRutaScreen("No hay líneas disponibles.", onVolver)
        EstadoDashboard.LISTO, EstadoDashboard.SIN_COBERTURA -> {
            val error = validarContextoRuta(
                lineaId, anio, estado.lineas, estado.aniosDisponibles,
                estado.lineaId, estado.anio
            )
            if (error != null) ErrorRutaScreen(error, onVolver) else contenido()
        }
    }
}

@Composable
private fun ErrorRutaScreen(mensaje: String, onVolver: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(mensaje)
        Button(onClick = onVolver, modifier = Modifier.fillMaxWidth()) {
            Text("Volver")
        }
    }
}
