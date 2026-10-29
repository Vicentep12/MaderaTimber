package com.duoc.madertimber.ui.screens.proyectos

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
fun FichaProyectosScreen(
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
            Text("Desarrollo de Biomateriales", style = MaterialTheme.typography.headlineLarge)
            Text(
                "Ficha de proyecto",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Tarjeta de descripción y estado
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Descripción general", style = MaterialTheme.typography.titleMedium)
                Text(
                    text = "Este proyecto busca desarrollar nuevos biomateriales a partir de residuos de la industria maderera, con el fin de crear alternativas sustentables para la construcción en Chile.",
                    style = MaterialTheme.typography.bodyMedium
                )

                DatoFilaProyecto(etiqueta = "Estado", valor = "En ejecución")
                DatoFilaProyecto(etiqueta = "Fecha de inicio", valor = "Marzo 2024")
            }
        }

        // Tarjeta de equipo y detalles técnicos
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Detalles de ejecución", style = MaterialTheme.typography.titleMedium)
                DatoFilaProyecto(etiqueta = "Investigador principal", valor = "Dra. Ana Silva")
                DatoFilaProyecto(etiqueta = "Línea asociada", valor = "Construcción Sustentable")
                DatoFilaProyecto(etiqueta = "Financiamiento", valor = "Fondo CENAMAD")
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
private fun DatoFilaProyecto(etiqueta: String, valor: String, modifier: Modifier = Modifier) {
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
private fun FichaProyectosPreview() {
    MaderTimberTheme {
        FichaProyectosScreen(onVolverClick = {})
    }
}

