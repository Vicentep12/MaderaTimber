package com.duoc.madertimber.ui.screens.dashboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.duoc.madertimber.data.model.TipoIndicador
import com.duoc.madertimber.data.model.nombre
import com.duoc.madertimber.data.model.unidad
import com.duoc.madertimber.ui.theme.MaderTimberTheme
import java.util.Locale

@Composable
fun DashboardScreen(
    state: DashboardUiState,
    onEvent: (DashboardEvent) -> Unit,
    onVerLineaClick: () -> Unit = {},
    onVerIndicadorClick: () -> Unit = {},
    onVerProyectosClick: () -> Unit = {},
    onVerInvestigadoresClick: () -> Unit = {},
    onVerPublicacionesClick: () -> Unit = {},
    onCerrarSesionClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("CENAMAD", style = MaterialTheme.typography.headlineLarge)
        Text("Explora la investigación", style = MaterialTheme.typography.titleLarge)
        Text("Datos demostrativos de las líneas de investigación.")

        when (state.estado) {
            EstadoDashboard.CARGANDO -> CircularProgressIndicator()
            EstadoDashboard.VACIO -> Text("No hay líneas de investigación disponibles.")
            EstadoDashboard.ERROR -> {
                Text(state.mensaje ?: "No se pudieron cargar los datos.")
                Button(onClick = { onEvent(DashboardEvent.ReintentarCarga) }) {
                    Text("Reintentar")
                }
            }
            EstadoDashboard.LISTO, EstadoDashboard.SIN_COBERTURA -> {
                SelectorOpciones(
                    titulo = "Línea de investigación",
                    opciones = state.lineas,
                    seleccionada = state.lineaSeleccionada,
                    etiqueta = { it.nombre },
                    onSeleccionar = { onEvent(DashboardEvent.LineaSeleccionada(it.id)) }
                )
                SelectorOpciones(
                    titulo = "Año",
                    opciones = state.aniosDisponibles,
                    seleccionada = state.anio,
                    etiqueta = { it.toString() },
                    onSeleccionar = { onEvent(DashboardEvent.AnioSeleccionado(it)) }
                )

                Card(modifier = Modifier.fillMaxWidth().clickable(onClick = onVerLineaClick)) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            state.lineaSeleccionada?.nombre ?: "Línea no disponible",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text("Año: " + state.anio)
                        Text("Datos demostrativos", color = MaterialTheme.colorScheme.primary)
                    }
                }

                if (state.estado == EstadoDashboard.SIN_COBERTURA) {
                    Text(state.mensaje ?: "Sin datos para esta línea y año")
                } else {
                    val proyectos = state.valor(TipoIndicador.PROYECTOS_ACTIVOS)
                    val publicaciones = state.valor(TipoIndicador.PUBLICACIONES_GENERADAS)
                    val investigadores = state.valor(TipoIndicador.INVESTIGADORES_ASOCIADOS)
                    val impacto = state.valor(TipoIndicador.IMPACTO_ESTIMADO)
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        IndicadorCard(
                            "Proyectos activos",
                            formatoValor(TipoIndicador.PROYECTOS_ACTIVOS, proyectos),
                            Modifier.weight(1f),
                            onClick = onVerProyectosClick
                        )
                        IndicadorCard(
                            "Publicaciones",
                            formatoValor(TipoIndicador.PUBLICACIONES_GENERADAS, publicaciones),
                            Modifier.weight(1f),
                            onClick = onVerPublicacionesClick
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        IndicadorCard(
                            "Investigadores",
                            formatoValor(TipoIndicador.INVESTIGADORES_ASOCIADOS, investigadores),
                            Modifier.weight(1f),
                            onClick = onVerInvestigadoresClick
                        )
                        IndicadorCard(
                            "Impacto estimado",
                            formatoValor(TipoIndicador.IMPACTO_ESTIMADO, impacto),
                            Modifier.weight(1f),
                            onClick = onVerIndicadorClick
                        )
                    }
                    Text(
                        "El impacto estimado es un índice demostrativo de actividad, " +
                            "no una medida de calidad científica.",
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                SelectorOpciones(
                    titulo = "Comparar líneas por",
                    opciones = TipoIndicador.entries,
                    seleccionada = state.tipoComparacion,
                    etiqueta = { it.nombre() },
                    onSeleccionar = { onEvent(DashboardEvent.MetricaComparada(it)) }
                )
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text("Comparación entre líneas", style = MaterialTheme.typography.titleMedium)
                        Text(
                            state.tipoComparacion.nombre() + " en " + state.anio +
                                " · " + state.tipoComparacion.unidad()
                        )
                        state.barras.forEach { barra ->
                            BarraComparativa(
                                nombre = barra.linea.nombre,
                                valor = formatoValor(state.tipoComparacion, barra.valor),
                                fraccion = barra.fraccion,
                                seleccionada = barra.seleccionada
                            )
                        }
                    }
                }
                Button(onClick = onVerLineaClick, modifier = Modifier.fillMaxWidth()) {
                    Text("Ver línea")
                }
            }
        }
        Button(onClick = onCerrarSesionClick, modifier = Modifier.fillMaxWidth()) {
            Text("Cerrar sesión / Salir")
        }
    }
}

private fun DashboardUiState.valor(tipo: TipoIndicador): Double? =
    indicadores.find { it.indicador.tipo == tipo }?.valorSeleccionado

private fun formatoValor(tipo: TipoIndicador, valor: Double?): String {
    if (valor == null) return "Sin datos"
    return when (tipo) {
        TipoIndicador.PROYECTOS_ACTIVOS,
        TipoIndicador.PUBLICACIONES_GENERADAS,
        TipoIndicador.INVESTIGADORES_ASOCIADOS -> valor.toInt().toString()
        TipoIndicador.AVANCE_PROMEDIO ->
            String.format(Locale.forLanguageTag("es-CL"), "%.1f %%", valor)
        TipoIndicador.IMPACTO_ESTIMADO ->
            String.format(Locale.forLanguageTag("es-CL"), "%.1f pts", valor)
    }
}

@Composable
private fun <T> SelectorOpciones(
    titulo: String,
    opciones: List<T>,
    seleccionada: T?,
    etiqueta: (T) -> String,
    onSeleccionar: (T) -> Unit
) {
    Text(titulo, style = MaterialTheme.typography.titleSmall)
    Row(
        modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        opciones.forEach { opcion ->
            FilterChip(
                selected = opcion == seleccionada,
                onClick = { onSeleccionar(opcion) },
                label = { Text(etiqueta(opcion)) }
            )
        }
    }
}

@Composable
private fun IndicadorCard(
    titulo: String,
    valor: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Card(modifier = modifier.clickable(onClick = onClick)) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(valor, style = MaterialTheme.typography.headlineSmall)
            Text(titulo, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun BarraComparativa(
    nombre: String,
    valor: String,
    fraccion: Float,
    seleccionada: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(nombre + ": " + valor + if (seleccionada) " · seleccionada" else "")
        LinearProgressIndicator(
            progress = { fraccion },
            modifier = Modifier.fillMaxWidth().height(8.dp),
            color = if (seleccionada) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.tertiary
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DashboardPreview() {
    MaderTimberTheme {
        DashboardScreen(
            state = DashboardUiState(estado = EstadoDashboard.VACIO),
            onEvent = {}
        )
    }
}
