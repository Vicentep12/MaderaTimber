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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.duoc.madertimber.data.model.LineaInvestigacion
import com.duoc.madertimber.ui.theme.MaderTimberTheme

@Composable
fun DetalleLineaScreen(
    linea: LineaInvestigacion,
    anio: Int,
    onVolverClick: () -> Unit,
    onVerIndicadorClick: () -> Unit,
    onVerRelacionadosClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(linea.nombre, style = MaterialTheme.typography.headlineLarge)
        Text("Línea de investigación · $anio · Datos demostrativos")
        Card(modifier = Modifier.fillMaxWidth()) {
            Text(linea.descripcion, modifier = Modifier.padding(16.dp))
        }
        Button(onClick = onVerIndicadorClick, modifier = Modifier.fillMaxWidth()) {
            Text("Ver indicador seleccionado")
        }
        Button(onClick = onVerRelacionadosClick, modifier = Modifier.fillMaxWidth()) {
            Text("Ver contenidos relacionados")
        }
        Button(onClick = onVolverClick, modifier = Modifier.fillMaxWidth()) {
            Text("Volver al panel")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DetalleLineaPreview() {
    MaderTimberTheme {
        DetalleLineaScreen(
            linea = LineaInvestigacion("L1", "Línea de ejemplo", "Descripción demostrativa", "#008000"),
            anio = 2024,
            onVolverClick = {},
            onVerIndicadorClick = {},
            onVerRelacionadosClick = {}
        )
    }
}
