package com.duoc.madertimber.ui.screens.publicaciones

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
fun FichaPublicacionScreen(
    onVolverClick: () -> Unit, // Función de navegación
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
            Text(
                "Impacto de Biomateriales en Construcción",
                style = MaterialTheme.typography.headlineLarge
            )
            Text(
                "Detalle de publicación",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Tarjeta de Resumen (Abstract)
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Resumen", style = MaterialTheme.typography.titleMedium)
                Text(
                    text = "El estudio analiza la viabilidad térmica y estructural de nuevos compuestos de madera y polímeros reciclados, demostrando una mejora del 15% en la retención de calor frente a materiales tradicionales.",
                    style = MaterialTheme.typography.bodyMedium
                )

                DatoFilaPublicacion(etiqueta = "Autor principal", valor = "Dra. Ana Silva")
                DatoFilaPublicacion(etiqueta = "Fecha de publicación", valor = "Octubre 2024")
            }
        }

        // Tarjeta de Datos Técnicos
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Datos de la revista", style = MaterialTheme.typography.titleMedium)
                DatoFilaPublicacion(etiqueta = "Revista", valor = "Journal of Sustainable Timber")
                DatoFilaPublicacion(etiqueta = "Cuartil (Q)", valor = "Q1")
                DatoFilaPublicacion(etiqueta = "DOI", valor = "10.1016/j.jst.2024.10234")
            }
        }

        // Botón para retroceder
        Button(
            onClick = onVolverClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver a la lista")
        }
    }
}

// Componente reutilizable para las filas de datos
@Composable
private fun DatoFilaPublicacion(etiqueta: String, valor: String, modifier: Modifier = Modifier) {
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
private fun FichaPublicacionPreview() {
    MaderTimberTheme {
        FichaPublicacionScreen(onVolverClick = {})
    }
}

