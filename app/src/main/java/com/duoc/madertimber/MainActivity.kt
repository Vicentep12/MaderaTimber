package com.duoc.madertimber

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.duoc.madertimber.data.model.CategoriaRelacionada
import com.duoc.madertimber.data.model.TipoIndicador
import com.duoc.madertimber.data.model.validarContextoRuta
import com.duoc.madertimber.data.model.validarFichaRuta
import com.duoc.madertimber.data.model.validarIndicadorRuta
import com.duoc.madertimber.data.repository.LocalPanelRepository
import com.duoc.madertimber.data.repository.SeleccionDatabase
import com.duoc.madertimber.data.repository.SeleccionRepository
import com.duoc.madertimber.ui.screens.auth.LoginScreen
import com.duoc.madertimber.ui.screens.auth.LoginViewModel
import com.duoc.madertimber.ui.screens.auth.RegisterScreen
import com.duoc.madertimber.ui.screens.auth.RegisterViewModel
import com.duoc.madertimber.ui.screens.camara.CamaraScreen
import com.duoc.madertimber.ui.screens.dashboard.DashboardEvent
import com.duoc.madertimber.ui.screens.dashboard.DashboardScreen
import com.duoc.madertimber.ui.screens.dashboard.DashboardUiState
import com.duoc.madertimber.ui.screens.dashboard.DashboardViewModel
import com.duoc.madertimber.ui.screens.dashboard.EstadoDashboard
import com.duoc.madertimber.ui.screens.lineas.DetalleIndicadorScreen
import com.duoc.madertimber.ui.screens.lineas.DetalleLineaScreen
import com.duoc.madertimber.ui.screens.relacionados.FichaRelacionadaScreen
import com.duoc.madertimber.ui.screens.relacionados.RelacionadosScreen
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

object Rutas {
    const val LOGIN = "login"
    const val REGISTRO = "registro"
    const val DASHBOARD = "dashboard"
    const val CAMARA = "camara"
    const val LINEA = "linea/{lineaId}/{anio}"
    const val INDICADOR = "indicador/{indicadorId}/{anio}"
    const val RELACIONADOS = "relacionados/{lineaId}/{anio}?tipo={tipo}"
    const val FICHA = "ficha/{categoria}/{itemId}/{lineaId}/{anio}?tipo={tipo}"

    fun linea(lineaId: String, anio: Int): String =
        "linea/${Uri.encode(lineaId)}/$anio"

    fun indicador(indicadorId: String, anio: Int): String =
        "indicador/${Uri.encode(indicadorId)}/$anio"

    fun relacionados(lineaId: String, anio: Int, tipo: TipoIndicador? = null): String =
        "relacionados/${Uri.encode(lineaId)}/$anio" +
            (tipo?.let { "?tipo=${it.name}" } ?: "")

    fun ficha(
        categoria: CategoriaRelacionada,
        itemId: String,
        lineaId: String,
        anio: Int,
        tipo: TipoIndicador?
    ): String = "ficha/${categoria.name}/${Uri.encode(itemId)}/${Uri.encode(lineaId)}/$anio" +
        (tipo?.let { "?tipo=${it.name}" } ?: "")
}

@Composable
fun AppNavigation(
    loginViewModel: LoginViewModel,
    registerViewModel: RegisterViewModel,
    dashboardViewModel: DashboardViewModel,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
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

    fun salir() {
        limpiarAcceso()
        volver()
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
                BackHandler(enabled = !registerViewModel.uiState.showNotice, onBack = ::salir)
                RegisterScreen(
                    state = registerViewModel.uiState,
                    onEvent = registerViewModel::onEvent,
                    onLogin = ::salir,
                    onGuest = ::abrirDashboard
                )
            }
            composable(Rutas.DASHBOARD) {
                DestinoDashboard(dashboardViewModel, navController, ::salir)
            }
            composable(Rutas.CAMARA) {
                CamaraScreen(onVolverClick = ::volver)
            }
            composable(Rutas.LINEA, arguments = listOf(argumentoTexto("lineaId"), argumentoTexto("anio"))) { entrada ->
                DestinoLinea(entrada, dashboardViewModel, navController, ::volver)
            }
            composable(Rutas.INDICADOR, arguments = listOf(argumentoTexto("indicadorId"), argumentoTexto("anio"))) { entrada ->
                DestinoIndicador(entrada, dashboardViewModel, navController, ::volver)
            }
            composable(
                Rutas.RELACIONADOS,
                arguments = listOf(argumentoTexto("lineaId"), argumentoTexto("anio"), argumentoTipo)
            ) { entrada ->
                DestinoRelacionados(entrada, dashboardViewModel, navController, ::volver)
            }
            composable(
                Rutas.FICHA,
                arguments = listOf(
                    argumentoTexto("categoria"),
                    argumentoTexto("itemId"),
                    argumentoTexto("lineaId"),
                    argumentoTexto("anio"),
                    argumentoTipo
                )
            ) { entrada ->
                DestinoFicha(entrada, dashboardViewModel, ::volver)
            }
        }
    }
}

private fun argumentoTexto(nombre: String) = navArgument(nombre) { type = NavType.StringType }

private val argumentoTipo = navArgument("tipo") {
    type = NavType.StringType
    nullable = true
    defaultValue = null
}

private fun NavBackStackEntry.texto(nombre: String): String? = arguments?.getString(nombre)

private fun NavBackStackEntry.anio(): Int? = texto("anio")?.toIntOrNull()

private fun errorDeTipo(tipoTexto: String?, tipo: TipoIndicador?, estado: DashboardUiState): String? = when {
    tipoTexto != null && tipo == null -> "El tipo de indicador no es válido."
    tipo != null && tipo != estado.tipoIndicador -> "La selección cambió. Vuelve al panel."
    else -> null
}

@Composable
private fun DestinoDashboard(
    dashboardViewModel: DashboardViewModel,
    navController: NavHostController,
    onSalir: () -> Unit
) {
    val estado = dashboardViewModel.uiState

    fun abrirRelacionados(tipo: TipoIndicador) {
        val lineaId = estado.lineaId ?: return
        dashboardViewModel.onEvent(DashboardEvent.IndicadorSeleccionado(tipo))
        navController.navigate(Rutas.relacionados(lineaId, estado.anio, tipo))
    }

    BackHandler(onBack = onSalir)
    DashboardScreen(
        state = estado,
        onEvent = dashboardViewModel::onEvent,
        onVerLineaClick = {
            estado.lineaId?.let { navController.navigate(Rutas.linea(it, estado.anio)) }
        },
        onVerIndicadorClick = {
            val impacto = estado.indicadores.find { it.indicador.tipo == TipoIndicador.IMPACTO_ESTIMADO }
            if (impacto != null) {
                dashboardViewModel.onEvent(DashboardEvent.IndicadorSeleccionado(TipoIndicador.IMPACTO_ESTIMADO))
                navController.navigate(Rutas.indicador(impacto.indicador.id, estado.anio))
            }
        },
        onVerProyectosClick = { abrirRelacionados(TipoIndicador.PROYECTOS_ACTIVOS) },
        onVerInvestigadoresClick = { abrirRelacionados(TipoIndicador.INVESTIGADORES_ASOCIADOS) },
        onVerPublicacionesClick = { abrirRelacionados(TipoIndicador.PUBLICACIONES_GENERADAS) },
        onCamaraClick = { navController.navigate(Rutas.CAMARA) },
        onCerrarSesionClick = onSalir
    )
}

@Composable
private fun DestinoLinea(
    entrada: NavBackStackEntry,
    dashboardViewModel: DashboardViewModel,
    navController: NavHostController,
    onVolver: () -> Unit
) {
    val estado = dashboardViewModel.uiState
    ContenidoContextual(estado, entrada.texto("lineaId"), entrada.anio(), onVolver) {
        val detalle = estado.detalleLinea ?: return@ContenidoContextual
        DetalleLineaScreen(
            state = detalle,
            onHistoricoSeleccionado = { tipo ->
                dashboardViewModel.onEvent(DashboardEvent.HistoricoLineaSeleccionado(tipo))
            },
            onVolverClick = onVolver,
            onVerIndicadorClick = { tipo ->
                estado.indicadores.find { it.indicador.tipo == tipo }?.let { indicador ->
                    dashboardViewModel.onEvent(DashboardEvent.IndicadorSeleccionado(tipo))
                    navController.navigate(Rutas.indicador(indicador.indicador.id, estado.anio))
                }
            },
            onVerRelacionadosClick = {
                navController.navigate(Rutas.relacionados(detalle.linea.id, estado.anio))
            }
        )
    }
}

@Composable
private fun DestinoIndicador(
    entrada: NavBackStackEntry,
    dashboardViewModel: DashboardViewModel,
    navController: NavHostController,
    onVolver: () -> Unit
) {
    val estado = dashboardViewModel.uiState
    val id = entrada.texto("indicadorId")
    val indicador = estado.indicadores.find { it.indicador.id == id }
    val historico = indicador?.let { estado.historicos[it.indicador.tipo] }
    ContenidoContextual(estado, indicador?.indicador?.lineaId ?: estado.lineaId, entrada.anio(), onVolver) {
        val error = validarIndicadorRuta(id, estado.indicadores)
        if (error != null || indicador == null || historico == null) {
            ErrorRutaScreen(error ?: "El indicador solicitado no existe.", onVolver)
        } else {
            DetalleIndicadorScreen(
                indicador = indicador,
                lineaNombre = estado.lineaSeleccionada?.nombre.orEmpty(),
                historico = historico,
                onVolverClick = onVolver,
                onVerRelacionadosClick = {
                    dashboardViewModel.onEvent(DashboardEvent.IndicadorSeleccionado(indicador.indicador.tipo))
                    navController.navigate(
                        Rutas.relacionados(indicador.indicador.lineaId, estado.anio, indicador.indicador.tipo)
                    )
                }
            )
        }
    }
}

@Composable
private fun DestinoRelacionados(
    entrada: NavBackStackEntry,
    dashboardViewModel: DashboardViewModel,
    navController: NavHostController,
    onVolver: () -> Unit
) {
    val estado = dashboardViewModel.uiState
    val lineaId = entrada.texto("lineaId")
    val anio = entrada.anio()
    val tipoTexto = entrada.texto("tipo")
    val tipo = TipoIndicador.entries.find { it.name == tipoTexto }
    ContenidoContextual(estado, lineaId, anio, onVolver) {
        val errorTipo = errorDeTipo(tipoTexto, tipo, estado)
        val linea = estado.lineaSeleccionada
        if (errorTipo != null) {
            ErrorRutaScreen(errorTipo, onVolver)
        } else if (linea != null) {
            RelacionadosScreen(
                datos = if (tipo == null) estado.relacionadosSinFiltro else estado.relacionados,
                lineaNombre = linea.nombre,
                anio = estado.anio,
                tipo = tipo,
                mensaje = if (estado.estado == EstadoDashboard.SIN_COBERTURA) estado.mensaje else null,
                onVolverClick = onVolver,
                onFichaClick = { categoria, itemId ->
                    if (lineaId != null && anio != null) {
                        navController.navigate(Rutas.ficha(categoria, itemId, lineaId, anio, tipo))
                    }
                }
            )
        }
    }
}

@Composable
private fun DestinoFicha(
    entrada: NavBackStackEntry,
    dashboardViewModel: DashboardViewModel,
    onVolver: () -> Unit
) {
    val estado = dashboardViewModel.uiState
    val tipoTexto = entrada.texto("tipo")
    val tipo = TipoIndicador.entries.find { it.name == tipoTexto }
    val categoria = CategoriaRelacionada.entries.find { it.name == entrada.texto("categoria") }
    val itemId = entrada.texto("itemId")
    ContenidoContextual(estado, entrada.texto("lineaId"), entrada.anio(), onVolver) {
        val datos = if (tipo == null) estado.relacionadosSinFiltro else estado.relacionados
        val error = errorDeTipo(tipoTexto, tipo, estado) ?: validarFichaRuta(categoria, itemId, datos, tipo)
        if (error != null || datos == null || categoria == null || itemId == null) {
            ErrorRutaScreen(error ?: "El contenido solicitado no existe.", onVolver)
        } else {
            FichaRelacionadaScreen(datos, categoria, itemId, onVolver)
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
