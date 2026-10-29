package com.duoc.madertimber.ui.screens.lineas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.duoc.madertimber.data.model.IndicadorCalculado
import com.duoc.madertimber.data.model.HistoricoIndicador
import com.duoc.madertimber.data.model.TipoIndicador
import com.duoc.madertimber.ui.components.GraficoHistorico
import java.util.Locale

@Composable
fun DetalleIndicadorScreen(
    indicador: IndicadorCalculado,
    historico: HistoricoIndicador,
    onVolverClick: () -> Unit,
    onVerRelacionadosClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val valor = indicador.valorSeleccionado?.let {
        when (indicador.indicador.tipo) {
            TipoIndicador.PROYECTOS_ACTIVOS,
            TipoIndicador.PUBLICACIONES_GENERADAS,
            TipoIndicador.INVESTIGADORES_ASOCIADOS -> it.toInt().toString()
            else -> String.format(Locale.forLanguageTag("es-CL"), "%.1f", it)
        }
    } ?: "Sin datos"
    Column(
        modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(indicador.indicador.nombre, style = MaterialTheme.typography.headlineLarge)
        Text("${indicador.anioSeleccionado} · Datos demostrativos")
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Valor seleccionado", style = MaterialTheme.typography.titleMedium)
                Text("$valor ${indicador.indicador.unidad}", style = MaterialTheme.typography.headlineMedium)
                Text(indicador.indicador.descripcion)
            }
        }
        GraficoHistorico(historico)
        Button(onClick = onVerRelacionadosClick, modifier = Modifier.fillMaxWidth()) {
            Text("Ver contenidos relacionados")
        }
        Button(onClick = onVolverClick, modifier = Modifier.fillMaxWidth()) {
            Text("Volver")
        }
    }
}
