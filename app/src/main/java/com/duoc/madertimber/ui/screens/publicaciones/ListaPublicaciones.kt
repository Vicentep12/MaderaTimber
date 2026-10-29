package com.duoc.madertimber.ui.screens.publicaciones

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.duoc.madertimber.ui.theme.MaderTimberTheme

@Composable
fun ListaPublicacionesScreen(
    onPublicacionClick: () -> Unit, // Navega a la ficha de la publicación
    onVolverClick: () -> Unit,      // Vuelve al panel anterior (Dashboard)
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Encabezado de la lista
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Publicaciones", style = MaterialTheme.typography.headlineLarge)
            Text(
                "Documentos científicos y artículos",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Contenedor de la lista estática
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            PublicacionItem(
                titulo = "Impacto de Biomateriales en Construcción",
                onClick = onPublicacionClick
            )
            PublicacionItem(
                titulo = "Eficiencia Energética en Viviendas de Madera",
                onClick = onPublicacionClick
            )
            PublicacionItem(
                titulo = "Nuevas Tecnologías de Adhesivos Ecológicos",
                onClick = onPublicacionClick
            )
        }

        // Botón para retroceder
        Button(
            onClick = onVolverClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver")
        }
    }
}

// Componente reutilizable para cada fila de la lista
@Composable
private fun PublicacionItem(
    titulo: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() } // Hace la tarjeta interactiva
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = titulo,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f) // Permite que el texto largo ocupe el espacio sin empujar la flecha
            )
            Text(
                text = ">",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ListaPublicacionesPreview() {
    MaderTimberTheme {
        ListaPublicacionesScreen(
            onPublicacionClick = {},
            onVolverClick = {}
        )
    }
}

