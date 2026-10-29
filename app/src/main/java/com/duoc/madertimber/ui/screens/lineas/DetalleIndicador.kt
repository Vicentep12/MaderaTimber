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
fun DetalleIndicadorScreen(
    onVolverClick: () -> Unit, // Función inyectada para navegar hacia atrás
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Encabezado
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Impacto Estimado", style = MaterialTheme.typography.headlineLarge)
            Text(
                "Detalle del indicador de actividad",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Tarjeta principal con el valor destacado
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Valor actual (2024)", style = MaterialTheme.typography.titleMedium)
                Text(
                    text = "78,0 pts",
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.primary // Resalta el número principal
                )
                Text(
                    text = "Este índice refleja el volumen de proyectos activos, investigadores vinculados y publicaciones científicas durante el año en curso.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // Tarjeta con el desglose de datos usando el mismo formato de filas
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Aporte por línea de investigación", style = MaterialTheme.typography.titleMedium)
                FilaDesglose(etiqueta = "Construcción sustentable", valor = "42,0 pts")
                FilaDesglose(etiqueta = "Biomateriales de madera", valor = "20,5 pts")
                FilaDesglose(etiqueta = "Procesos industriales", valor = "15,5 pts")
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

// Reutilizamos la lógica de filas que usamos en FichaInvestigador
@Composable
private fun FilaDesglose(etiqueta: String, valor: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = etiqueta,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1.5f)
        )
        Text(
            text = valor,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.weight(0.5f)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DetalleIndicadorPreview() {
    MaderTimberTheme {
        DetalleIndicadorScreen(
            onVolverClick = {}
        )
    }
}

