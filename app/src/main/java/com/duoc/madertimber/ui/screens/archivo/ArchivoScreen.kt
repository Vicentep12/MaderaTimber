package com.duoc.madertimber.ui.screens.archivo

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.util.Locale

private data class DatosArchivo(val nombre: String, val tamanoBytes: Long?)

// Pantalla demostrativa del selector nativo de archivos: muestra los datos del archivo elegido y nada más
@Composable
fun ArchivoScreen(
    estado: ArchivoUiState,
    onArchivoElegido: (String, Long?, String?) -> Unit,
    onSeleccionCancelada: () -> Unit,
    onErrorDeLectura: () -> Unit,
    onQuitarArchivo: () -> Unit,
    onVolverClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // OpenDocument abre el selector del sistema y no necesita permisos de almacenamiento
    val selector = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri == null) {
            onSeleccionCancelada()
        } else {
            val datos = leerDatos(context, uri)
            if (datos == null) {
                onErrorDeLectura()
            } else {
                onArchivoElegido(datos.nombre, datos.tamanoBytes, context.contentResolver.getType(uri))
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Archivo de apoyo", style = MaterialTheme.typography.headlineSmall)
        Text(
            "Elige un archivo de tu dispositivo para ver su nombre, tamaño y tipo. " +
                "Es una demostración: el archivo no se sube ni se guarda en la aplicación."
        )
        if (estado.nombre != null) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Archivo elegido", style = MaterialTheme.typography.titleMedium)
                    Text("Nombre: ${estado.nombre}")
                    Text("Tamaño: ${formatoTamano(estado.tamanoBytes)}")
                    Text("Tipo: ${estado.tipo ?: "Desconocido"}")
                }
            }
        }
        estado.mensaje?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        Button(onClick = { selector.launch(arrayOf("*/*")) }, modifier = Modifier.fillMaxWidth()) {
            Text(if (estado.nombre == null) "Elegir archivo" else "Elegir otro archivo")
        }
        if (estado.nombre != null) {
            OutlinedButton(onClick = onQuitarArchivo, modifier = Modifier.fillMaxWidth()) {
                Text("Quitar selección")
            }
        }
        OutlinedButton(onClick = onVolverClick, modifier = Modifier.fillMaxWidth()) {
            Text("Volver")
        }
    }
}

// Pide al sistema solo el nombre y el tamaño del archivo, sin abrir su contenido
private fun leerDatos(context: Context, uri: Uri): DatosArchivo? = try {
    context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
        if (!cursor.moveToFirst()) return@use null
        val columnaNombre = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        val columnaTamano = cursor.getColumnIndex(OpenableColumns.SIZE)
        val nombre = if (columnaNombre >= 0) cursor.getString(columnaNombre) else null
        val tamano = if (columnaTamano >= 0 && !cursor.isNull(columnaTamano)) cursor.getLong(columnaTamano) else null
        nombre?.let { DatosArchivo(it, tamano) }
    }
} catch (error: Exception) {
    null
}

private fun formatoTamano(bytes: Long?): String = when {
    bytes == null -> "Desconocido"
    bytes < 1024 -> "$bytes bytes"
    bytes < 1024 * 1024 -> String.format(Locale.forLanguageTag("es-CL"), "%.1f KB", bytes / 1024.0)
    else -> String.format(Locale.forLanguageTag("es-CL"), "%.1f MB", bytes / (1024.0 * 1024.0))
}
