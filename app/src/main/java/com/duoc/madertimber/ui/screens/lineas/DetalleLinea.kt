package com.duoc.madertimber.ui.screens.lineas

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.duoc.madertimber.data.model.DatosRelacionados
import com.duoc.madertimber.data.model.HistoricoIndicador
import com.duoc.madertimber.data.model.IndicadorCalculado
import com.duoc.madertimber.data.model.LineaInvestigacion
import com.duoc.madertimber.data.model.TipoIndicador
import com.duoc.madertimber.data.model.nombre
import com.duoc.madertimber.ui.components.GraficoHistorico
import com.duoc.madertimber.ui.theme.MaderTimberTheme
import java.util.Locale

data class DetalleLineaUiState(
    val linea: LineaInvestigacion,
    val anio: Int,
    val indicadores: List<IndicadorCalculado>,
    val tipoHistorico: TipoIndicador,
    val historico: HistoricoIndicador?,
    val relacionados: DatosRelacionados?,
    val mensaje: String? = null
)

@Composable
fun DetalleLineaScreen(
    state: DetalleLineaUiState,
    onHistoricoSeleccionado: (TipoIndicador) -> Unit,
    onVerIndicadorClick: (TipoIndicador) -> Unit,
    onVerRelacionadosClick: () -> Unit,
    onVolverClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(state.linea.nombre, style = MaterialTheme.typography.headlineLarge)
        Text("Línea de investigación · ${state.anio} · Datos demostrativos")
        Card(modifier = Modifier.fillMaxWidth()) {
            Text(state.linea.descripcion, modifier = Modifier.padding(16.dp))
        }

        if (state.indicadores.isEmpty()) {
            Text(
                text = state.mensaje ?: "Sin datos para esta línea y año",
                modifier = Modifier.testTag("detalle-linea-sin-datos")
            )
        } else {
            Text("Indicadores de ${state.anio}", style = MaterialTheme.typography.titleLarge)
            state.indicadores.forEach { indicador ->
                Card(
                    modifier = Modifier.fillMaxWidth()
                        .testTag("indicador-linea-${indicador.indicador.tipo.name}")
                        .clickable(role = Role.Button) {
                            onVerIndicadorClick(indicador.indicador.tipo)
                        }
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(indicador.indicador.nombre, style = MaterialTheme.typography.titleMedium)
                        Text(valorVisible(indicador), style = MaterialTheme.typography.headlineSmall)
                        Text("Ver detalle del indicador", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            Text("Evolución anual", style = MaterialTheme.typography.titleLarge)
            Row(
                modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TipoIndicador.entries.forEach { tipo ->
                    FilterChip(
                        modifier = Modifier.testTag("historico-linea-${tipo.name}"),
                        selected = state.tipoHistorico == tipo,
                        onClick = { onHistoricoSeleccionado(tipo) },
                        label = { Text(tipo.nombre()) }
                    )
                }
            }
            state.historico?.let { GraficoHistorico(it) }
            Button(
                onClick = { onVerIndicadorClick(state.tipoHistorico) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ver indicador seleccionado")
            }

            Text("Contenidos relacionados", style = MaterialTheme.typography.titleLarge)
            val relacionados = state.relacionados
            if (relacionados != null) {
                Text("Proyectos asociados: ${relacionados.proyectos.size}")
                Text("Investigadores asociados: ${relacionados.investigadores.size}")
                Text("Publicaciones relacionadas: ${relacionados.publicaciones.size}")
                if (relacionados.estaVacio) {
                    Text("No hay contenidos para esta línea y año.")
                }
                Button(onClick = onVerRelacionadosClick, modifier = Modifier.fillMaxWidth()) {
                    Text("Ver contenidos relacionados")
                }
            }
        }
        Button(onClick = onVolverClick, modifier = Modifier.fillMaxWidth()) {
            Text("Volver al panel")
        }
    }
}

private fun valorVisible(indicador: IndicadorCalculado): String {
    val valor = indicador.valorSeleccionado ?: return "Sin datos"
    val numero = when (indicador.indicador.tipo) {
        TipoIndicador.PROYECTOS_ACTIVOS,
        TipoIndicador.PUBLICACIONES_GENERADAS,
        TipoIndicador.INVESTIGADORES_ASOCIADOS -> valor.toInt().toString()
        TipoIndicador.AVANCE_PROMEDIO,
        TipoIndicador.IMPACTO_ESTIMADO ->
            String.format(Locale.forLanguageTag("es-CL"), "%.1f", valor)
    }
    return "$numero ${indicador.indicador.unidad}"
}

@Preview(showBackground = true)
@Composable
private fun DetalleLineaPreview() {
    MaderTimberTheme {
        DetalleLineaScreen(
            state = DetalleLineaUiState(
                linea = LineaInvestigacion("L1", "Línea de ejemplo", "Descripción demostrativa", "#008000"),
                anio = 2024,
                indicadores = emptyList(),
                tipoHistorico = TipoIndicador.PROYECTOS_ACTIVOS,
                historico = null,
                relacionados = null,
                mensaje = "Sin datos para esta línea y año"
            ),
            onHistoricoSeleccionado = {},
            onVerIndicadorClick = {},
            onVerRelacionadosClick = {},
            onVolverClick = {}
        )
    }
}
