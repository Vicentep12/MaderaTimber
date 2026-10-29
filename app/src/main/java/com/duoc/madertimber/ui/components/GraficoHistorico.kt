package com.duoc.madertimber.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.duoc.madertimber.data.model.HistoricoIndicador
import java.util.Locale

@Composable
fun GraficoHistorico(
    historico: HistoricoIndicador,
    modifier: Modifier = Modifier
) {
    val puntos = historico.puntos
    val valores = puntos.mapNotNull { it.valor }
    val resumen = puntos.joinToString("; ") { punto ->
        "${punto.anio}: ${punto.valor?.let { formatoValor(it, historico.unidad) } ?: "Sin datos"}" +
            if (punto.seleccionado) " (seleccionado)" else ""
    }

    Card(modifier = modifier.fillMaxWidth().testTag("grafico-historico")) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Evolución de ${historico.titulo}", style = MaterialTheme.typography.titleMedium)
            Text("Unidad: ${historico.unidad}", style = MaterialTheme.typography.bodySmall)
            if (valores.isEmpty()) {
                Text("Sin datos para el período seleccionado")
            } else {
                val linea = MaterialTheme.colorScheme.primary
                val guia = MaterialTheme.colorScheme.outlineVariant
                val puntoSeleccionado = MaterialTheme.colorScheme.tertiary
                Canvas(modifier = Modifier.fillMaxWidth().height(150.dp)) {
                    val margen = 16.dp.toPx()
                    val minimo = valores.minOrNull() ?: 0.0
                    val maximo = valores.maxOrNull() ?: 0.0
                    fun posicionY(valor: Double): Float = if (maximo == minimo) {
                        size.height / 2f
                    } else {
                        (size.height - margen -
                            ((valor - minimo) / (maximo - minimo)).toFloat() *
                            (size.height - 2f * margen))
                    }

                    drawLine(
                        guia,
                        start = Offset(0f, size.height - margen),
                        end = Offset(size.width, size.height - margen),
                        strokeWidth = 1.dp.toPx()
                    )
                    puntos.forEachIndexed { indice, punto ->
                        val valor = punto.valor ?: return@forEachIndexed
                        val x = size.width * (indice + 0.5f) / puntos.size
                        val y = posicionY(valor)
                        if (indice > 0) {
                            val anterior = puntos[indice - 1].valor
                            if (anterior != null) {
                                val xAnterior = size.width * (indice - 0.5f) / puntos.size
                                drawLine(
                                    linea,
                                    start = Offset(xAnterior, posicionY(anterior)),
                                    end = Offset(x, y),
                                    strokeWidth = 3.dp.toPx()
                                )
                            }
                        }
                        drawCircle(
                            color = if (punto.seleccionado) puntoSeleccionado else linea,
                            radius = if (punto.seleccionado) 7.dp.toPx() else 5.dp.toPx(),
                            center = Offset(x, y),
                            style = Stroke(width = 2.dp.toPx())
                        )
                    }
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    puntos.forEach { punto ->
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = punto.anio.toString() + if (punto.seleccionado) " *" else "",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                fontWeight = if (punto.seleccionado) FontWeight.Bold else FontWeight.Normal,
                                style = MaterialTheme.typography.labelMedium
                            )
                            Text(
                                text = punto.valor?.let { formatoValor(it, historico.unidad) }
                                    ?: "Sin datos",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }
                if (puntos.any { it.seleccionado }) {
                    Text("* Año seleccionado", style = MaterialTheme.typography.labelSmall)
                }
            }
            Text("Resumen: $resumen", style = MaterialTheme.typography.bodySmall)
        }
    }
}

private fun formatoValor(valor: Double, unidad: String): String {
    val numero = if (valor % 1.0 == 0.0) {
        valor.toInt().toString()
    } else {
        String.format(Locale.forLanguageTag("es-CL"), "%.1f", valor)
    }
    return if (unidad == "%") "$numero %" else "$numero $unidad"
}
