package com.duoc.madertimber.ui.screens.archivo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

data class ArchivoUiState(
    val nombre: String? = null,
    val tamanoBytes: Long? = null,
    val tipo: String? = null,
    val mensaje: String? = null
)

class ArchivoViewModel : ViewModel() {
    var uiState by mutableStateOf(ArchivoUiState())
        private set

    // Solo guarda los datos del archivo; el contenido no se lee ni se sube a ningún lado
    fun archivoElegido(nombre: String, tamanoBytes: Long?, tipo: String?) {
        uiState = ArchivoUiState(nombre = nombre, tamanoBytes = tamanoBytes, tipo = tipo)
    }

    fun seleccionCancelada() {
        uiState = uiState.copy(mensaje = "No se eligió ningún archivo.")
    }

    fun errorDeLectura() {
        uiState = uiState.copy(mensaje = "No se pudo leer la información del archivo.")
    }

    fun quitarArchivo() {
        uiState = ArchivoUiState()
    }
}
