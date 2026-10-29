package com.duoc.madertimber.ui.screens.lineas

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.duoc.madertimber.ui.theme.MaderTimberTheme

@Composable
fun DetalleLineaScreen(
    onVolverClick: () -> Unit, // Inyectado desde el MainActivity o AppNavigation
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Encabezado de la vista
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Construcción Sustentable", style = MaterialTheme.typography.headlineLarge)
            Text(
                "Detalle de la línea de investigación",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Tarjeta principal con descripción y métricas
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Resumen general", style = MaterialTheme.typography.titleMedium)
                Text(
                    text = "Esta línea se enfoca en el desarrollo de sistemas constructivos en madera, promoviendo la eficiencia energética y la reducción de la huella de carbono en la industria.",
                    style = MaterialTheme.typography.bodyMedium
                )

                // Divisor o espacio visual implícito mediante el Arrangement.spacedBy
                DatoFila(etiqueta = "Proyectos activos", valor = "8")
                DatoFila(etiqueta = "Investigadores vinculados", valor = "12")
                DatoFila(etiqueta = "Publicaciones", valor = "15")
            }
        }

        // Tarjeta secundaria para destacar un impacto o avance
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Nivel de actividad (2024)", style = MaterialTheme.typography.titleMedium)
                Text(
                    text = "Alto",
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Línea con mayor crecimiento en el último semestre.",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // Botón para retroceder
        Button(
            onClick = onVolverClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver al panel")
        }
    }
}

// Componente reutilizable adaptado para mostrar estadísticas de la línea
@Composable
private fun DatoFila(etiqueta: String, valor: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = etiqueta,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = valor,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DetalleLineaPreview() {
    MaderTimberTheme {
        DetalleLineaScreen(
            onVolverClick = {}
        )
    }
}



