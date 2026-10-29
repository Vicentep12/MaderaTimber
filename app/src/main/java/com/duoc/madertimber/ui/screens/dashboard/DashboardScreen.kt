package com.duoc.madertimber.ui.screens.dashboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.duoc.madertimber.ui.theme.MaderTimberTheme

// Primera vista estática. Más adelante, los datos vendrán del ViewModel.
@Composable
fun DashboardScreen(
    onVerLineaClick: () -> Unit = {},
    onVerIndicadorClick: () -> Unit = {},
    onVerProyectosClick: () -> Unit = {},
    onVerInvestigadoresClick: () -> Unit = {},
    onVerPublicacionesClick: () -> Unit = {},
    onCerrarSesionClick: () -> Unit = {},
    modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("CENAMAD", style = MaterialTheme.typography.headlineLarge)
        Text("Explora la investigación", style = MaterialTheme.typography.titleLarge)
        Text("Conoce la actividad de las líneas de investigación y su evolución.")

        Card(modifier = Modifier
            .fillMaxWidth()
            .clickable{onVerLineaClick()}) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Construcción sustentable", style = MaterialTheme.typography.titleMedium)
                Text("Año: 2024")
                Text("Datos demostrativos", color = MaterialTheme.colorScheme.primary)
            }
        }

        // Dos filas de tarjetas. weight reparte el ancho por igual.
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            IndicadorCard("Proyectos activos", "8", Modifier.weight(1f), onClick = onVerProyectosClick)
            IndicadorCard("Publicaciones", "15", Modifier.weight(1f), onClick = onVerPublicacionesClick )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            IndicadorCard("Investigadores", "12", Modifier.weight(1f), onClick = onVerInvestigadoresClick)
            IndicadorCard("Impacto estimado", "78,0 pts", Modifier.weight(1f), onClick = onVerIndicadorClick )
        }
        Text(
            "El impacto estimado es un índice demostrativo de actividad, " +
                "no una medida de calidad científica.",
            style = MaterialTheme.typography.bodySmall
        )

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Comparación entre líneas", style = MaterialTheme.typography.titleMedium)
                Text("Proyectos activos en 2024 · Escala de 0 a 10")
                BarraComparativa("Construcción sustentable", 8)
                BarraComparativa("Biomateriales de madera", 5)
                BarraComparativa("Procesos industriales", 10)
            }
        }
        Button(
            onClick = onVerLineaClick,
            modifier = Modifier.fillMaxWidth()
        ){
            Text("Ver línea")
        }

        Button(
            onClick = onCerrarSesionClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cerrar sesión / Salir")
        }
    }
}

// Reutilizamos la misma tarjeta cambiando solamente el título y el valor.
@Composable
private fun IndicadorCard(titulo: String, valor: String, modifier: Modifier = Modifier, onClick:()-> Unit={}) {
    Card(modifier = modifier.clickable{onClick()}) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(valor, style = MaterialTheme.typography.headlineSmall)
            Text(titulo, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun BarraComparativa(linea: String, proyectos: Int, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("$linea: $proyectos proyectos")
        // El mayor valor de esta muestra es 10: 8 / 10 ocupa el 80 % del ancho.
        LinearProgressIndicator(
            progress = { proyectos / 10f },
            modifier = Modifier.fillMaxWidth().height(8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DashboardPreview() {
    MaderTimberTheme {
        DashboardScreen()
    }
}
