package com.duoc.madertimber.ui.screens.investigadores

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
fun ListaInvestigadoresScreen(
    onInvestigadorClick: () -> Unit, // Navega a la ficha del investigador
    onVolverClick: () -> Unit,       // Vuelve a la pantalla anterior
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
            Text("Investigadores", style = MaterialTheme.typography.headlineLarge)
            Text(
                "Lista de investigadores vinculados",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Contenedor de la lista estática
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            InvestigadorItem(nombre = "Investigador A", onClick = onInvestigadorClick)
            InvestigadorItem(nombre = "Investigador B", onClick = onInvestigadorClick)
            InvestigadorItem(nombre = "Investigador C", onClick = onInvestigadorClick)
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

// Componente reutilizable para cada elemento de la lista
@Composable
private fun InvestigadorItem(
    nombre: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() } // Hace que toda la tarjeta sea cliqueable
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = nombre,
                style = MaterialTheme.typography.bodyLarge
            )
            // Símbolo de flecha simple para indicar navegación[cite: 2]
            Text(
                text = ">",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ListaInvestigadoresPreview() {
    MaderTimberTheme {
        ListaInvestigadoresScreen(
            onInvestigadorClick = {},
            onVolverClick = {}
        )
    }
}

