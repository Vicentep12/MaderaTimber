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
import com.duoc.madertimber.data.model.HistoricoIndicador
import com.duoc.madertimber.data.model.IndicadorCalculado
import com.duoc.madertimber.data.model.TipoIndicador
import com.duoc.madertimber.data.model.VariacionIndicador
import com.duoc.madertimber.ui.components.GraficoHistorico
import java.util.Locale

@Composable
fun DetalleIndicadorScreen(
    indicador: IndicadorCalculado,
    historico: HistoricoIndicador,
    onVolverClick: () -> Unit,
    onVerRelacionadosClick: () -> Unit,
    lineaNombre: String = "",
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(indicador.indicador.nombre, style = MaterialTheme.typography.headlineLarge)
        if (lineaNombre.isNotEmpty()) Text(lineaNombre)
        Text("${indicador.anioSeleccionado} · Datos demostrativos")
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Valor seleccionado", style = MaterialTheme.typography.titleMedium)
                Text(valorVisible(indicador), style = MaterialTheme.typography.headlineMedium)
                Text("Unidad: ${indicador.indicador.unidad}")
            }
        }
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    "Variación frente a ${indicador.anioSeleccionado - 1}",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(variacionVisible(indicador.variacion), style = MaterialTheme.typography.titleLarge)
            }
        }
        GraficoHistorico(historico)
        Text("Cómo interpretarlo", style = MaterialTheme.typography.titleMedium)
        Text(interpretacion(indicador.indicador.tipo))
        Text(
            "Datos sintéticos de demostración; no son cifras oficiales de CENAMAD.",
            style = MaterialTheme.typography.bodySmall
        )
        Button(onClick = onVerRelacionadosClick, modifier = Modifier.fillMaxWidth()) {
            Text("Ver contenidos relacionados")
        }
        Button(onClick = onVolverClick, modifier = Modifier.fillMaxWidth()) {
            Text("Volver")
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

private fun variacionVisible(variacion: VariacionIndicador): String = when (variacion) {
    is VariacionIndicador.Porcentaje -> "${numeroConSigno(variacion.valor)} %"
    is VariacionIndicador.PuntosPorcentuales ->
        "${numeroConSigno(variacion.valor)} puntos porcentuales"
    VariacionIndicador.SinComparacion -> "Sin comparación"
    VariacionIndicador.BaseAnteriorCero -> "No calculable: base anterior cero"
}

private fun numeroConSigno(valor: Double): String {
    val numero = String.format(Locale.forLanguageTag("es-CL"), "%.1f", valor)
    return if (valor > 0) "+$numero" else numero
}

private fun interpretacion(tipo: TipoIndicador): String = when (tipo) {
    TipoIndicador.PROYECTOS_ACTIVOS ->
        "Cuenta los proyectos activos de la línea en el año seleccionado."
    TipoIndicador.PUBLICACIONES_GENERADAS ->
        "Cuenta las publicaciones de la línea registradas durante ese año."
    TipoIndicador.INVESTIGADORES_ASOCIADOS ->
        "Cuenta una vez a cada investigador vinculado a la línea durante ese año."
    TipoIndicador.AVANCE_PROMEDIO ->
        "Promedio del avance de los proyectos activos. Sin proyectos activos no hay promedio."
    TipoIndicador.IMPACTO_ESTIMADO ->
        "Índice demostrativo de actividad basado en proyectos, publicaciones e investigadores. " +
            "No mide la calidad de la investigación."
}
