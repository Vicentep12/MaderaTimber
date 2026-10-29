package com.duoc.madertimber.ui.screens.dashboard

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.duoc.madertimber.data.model.LineaInvestigacion

@Composable
fun FiltrosPanelDialog(
    lineas: List<LineaInvestigacion>,
    anios: List<Int>,
    lineaIdActual: String?,
    anioActual: Int,
    onAplicar: (String, Int) -> Unit,
    onCancelar: () -> Unit
) {
    var lineaBorrador by rememberSaveable(lineaIdActual, anioActual) {
        mutableStateOf(lineaIdActual ?: lineas.firstOrNull()?.id.orEmpty())
    }
    var anioBorrador by rememberSaveable(lineaIdActual, anioActual) {
        mutableIntStateOf(anioActual)
    }

    AlertDialog(
        modifier = Modifier.testTag("modal-filtros"),
        onDismissRequest = onCancelar,
        title = { Text("Filtros del panel") },
        text = {
            Column(
                modifier = Modifier.heightIn(max = 420.dp).verticalScroll(rememberScrollState())
                    .selectableGroup(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Línea de investigación", style = MaterialTheme.typography.titleSmall)
                lineas.forEach { linea ->
                    Row(
                        modifier = Modifier.fillMaxWidth()
                            .heightIn(min = 48.dp)
                            .testTag("filtro-linea-${linea.id}")
                            .selectable(
                                selected = lineaBorrador == linea.id,
                                role = Role.RadioButton,
                                onClick = { lineaBorrador = linea.id }
                            )
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        RadioButton(selected = lineaBorrador == linea.id, onClick = null)
                        Spacer(Modifier.width(8.dp))
                        Text(linea.nombre, modifier = Modifier.padding(top = 12.dp))
                    }
                }
                Text("Año", style = MaterialTheme.typography.titleSmall)
                Row(
                    modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    anios.forEach { anio ->
                        FilterChip(
                            modifier = Modifier.testTag("filtro-anio-$anio"),
                            selected = anioBorrador == anio,
                            onClick = { anioBorrador = anio },
                            label = { Text(anio.toString()) }
                        )
                    }
                }
                Text("Los cambios se guardan al pulsar Aplicar.")
            }
        },
        confirmButton = {
            TextButton(
                modifier = Modifier.testTag("filtro-aplicar"),
                enabled = lineas.any { it.id == lineaBorrador } && anioBorrador in anios,
                onClick = { onAplicar(lineaBorrador, anioBorrador) }
            ) {
                Text("Aplicar")
            }
        },
        dismissButton = {
            TextButton(onClick = onCancelar) { Text("Cancelar") }
        }
    )
}
