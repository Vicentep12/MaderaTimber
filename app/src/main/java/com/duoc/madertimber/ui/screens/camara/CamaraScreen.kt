package com.duoc.madertimber.ui.screens.camara

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File

// Pantalla de la cámara: pide el permiso, abre la cámara y muestra la foto tomada
@Composable
fun CamaraScreen(onVolverClick: () -> Unit, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var fotoUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    var fotoTomada by rememberSaveable { mutableStateOf(false) }
    var mensaje by rememberSaveable { mutableStateOf<String?>(null) }

    val tomarFoto = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { exito ->
        fotoTomada = exito
        mensaje = if (exito) null else "No se tomó la foto."
    }

    // Crea un archivo temporal en la caché y se lo pasa a la cámara con FileProvider
    fun abrirCamara() {
        try {
            val archivo = File.createTempFile("madertimber_foto", ".jpg", context.cacheDir)
            val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", archivo)
            fotoUri = uri
            tomarFoto.launch(uri)
        } catch (e: ActivityNotFoundException) {
            mensaje = "No hay una aplicación de cámara disponible."
        } catch (e: Exception) {
            mensaje = "No se pudo preparar la foto."
        }
    }

    val pedirPermiso = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { concedido ->
        if (concedido) abrirCamara() else mensaje = "Permiso de cámara denegado. Actívalo en los ajustes para tomar fotos."
    }

    // Si ya hay permiso abre la cámara; si no, lo pide primero
    fun solicitarCamara() {
        mensaje = null
        val permiso = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
        if (permiso == PackageManager.PERMISSION_GRANTED) abrirCamara()
        else pedirPermiso.launch(Manifest.permission.CAMERA)
    }

    // Carga la foto en tamaño reducido para no gastar tanta memoria
    val imagen = remember(fotoUri, fotoTomada) {
        val uri = fotoUri
        if (!fotoTomada || uri == null) {
            null
        } else {
            runCatching {
                val limites = BitmapFactory.Options().apply { inJustDecodeBounds = true }
                context.contentResolver.openInputStream(uri)?.use { BitmapFactory.decodeStream(it, null, limites) }
                var muestra = 1
                while (limites.outWidth / muestra > 1600 || limites.outHeight / muestra > 1600) muestra *= 2
                val opciones = BitmapFactory.Options().apply { inSampleSize = muestra }
                context.contentResolver.openInputStream(uri)?.use { BitmapFactory.decodeStream(it, null, opciones) }
            }.getOrNull()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Foto de apoyo", style = MaterialTheme.typography.headlineSmall)
        Text(
            "Toma una foto de tus apuntes o de una muestra para consultarla junto a la línea de investigación. " +
                "La imagen es temporal y no se guarda en la aplicación."
        )
        if (imagen != null) {
            Image(
                bitmap = imagen.asImageBitmap(),
                contentDescription = "Foto tomada con la cámara",
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxWidth()
            )
        } else if (fotoTomada) {
            Text("No se pudo mostrar la foto.", color = MaterialTheme.colorScheme.error)
        }
        mensaje?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        Button(onClick = ::solicitarCamara, modifier = Modifier.fillMaxWidth()) {
            Text(if (imagen == null) "Tomar foto" else "Tomar otra foto")
        }
        OutlinedButton(onClick = onVolverClick, modifier = Modifier.fillMaxWidth()) {
            Text("Volver")
        }
    }
}
