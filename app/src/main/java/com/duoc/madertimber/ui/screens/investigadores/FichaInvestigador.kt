package com.duoc.madertimber.ui.screens.investigadores

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

// Vista estática de la ficha del investigador. Más adelante recibirá datos del ViewModel.
@Composable
fun FichaInvestigadorScreen(
    onVolverClick: () -> Unit, // Función de navegación que será inyectada desde AppNavigation
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp) // Espaciado principal similar al Dashboard
    ) {
        // Encabezado de la ficha
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Investigador A", style = MaterialTheme.typography.headlineLarge)
            Text(
                "Perfil ficticio",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant // Da un tono más suave al subtítulo
            )
        }

        // Tarjeta con los detalles del investigador
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                DetalleFila(etiqueta = "Especialidad", valor = "Construcción en madera")
                DetalleFila(etiqueta = "Línea", valor = "Construcción sustentable")
                DetalleFila(etiqueta = "Vinculación", valor = "2024")
            }
        }

        // Botón de acción para regresar
        Button(
            onClick = onVolverClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver a investigadores")
        }
    }
}

// Componente reutilizable para las filas de detalles, tal como hiciste con IndicadorCard
@Composable
private fun DetalleFila(etiqueta: String, valor: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween // Separa la etiqueta del valor
    ) {
        Text(
            text = etiqueta,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary, // Resalta la etiqueta con el color primario
            modifier = Modifier.weight(1f) // Ocupa una proporción del espacio
        )
        Text(
            text = valor,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1.5f) // Ocupa un poco más de espacio para textos largos
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FichaInvestigadorPreview() {
    MaderTimberTheme {
        FichaInvestigadorScreen(
            onVolverClick = {}
        )
    }
}
