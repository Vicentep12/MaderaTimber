package com.duoc.madertimber.ui.screens.acceso

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duoc.madertimber.data.model.AccesoErrores
import com.duoc.madertimber.data.model.AccesoValidador
import com.duoc.madertimber.data.repository.UsuarioRepository
import kotlinx.coroutines.launch

data class AccesoUiState(
    val nombre: String = "",
    val correo: String = "",
    val contrasena: String = "",
    val confirmacion: String = "",
    val mostrarContrasena: Boolean = false,
    val mostrarConfirmacion: Boolean = false,
    val enviado: Boolean = false,
    val mostrarAviso: Boolean = false,
    val errores: AccesoErrores = AccesoErrores()
)

sealed interface AccesoEvento {
    data class NombreCambiado(val valor: String) : AccesoEvento
    data class CorreoCambiado(val valor: String) : AccesoEvento
    data class ContrasenaCambiada(val valor: String) : AccesoEvento
    data class ConfirmacionCambiada(val valor: String) : AccesoEvento
    data object AlternarContrasena : AccesoEvento
    data object AlternarConfirmacion : AccesoEvento
    data object Enviar : AccesoEvento
    data object CerrarAviso : AccesoEvento
}

abstract class AccesoViewModel(
    private val registrando: Boolean,
    private val usuarios: UsuarioRepository
) : ViewModel() {
    // Estado observable de solo lectura para la vista. No se persisten credenciales.
    var uiState by mutableStateOf(AccesoUiState())
        private set

    // Último correo guardado en Room, para dejarlo escrito en el login
    private var correoGuardado = ""

    init {
        if (!registrando) {
            viewModelScope.launch {
                correoGuardado = usuarios.leer()?.correo.orEmpty()
                if (uiState.correo.isEmpty()) uiState = uiState.copy(correo = correoGuardado)
            }
        }
    }

    // Aplica lo que hace el usuario; al enviar valida y, si todo está bien, muestra el aviso
    fun onEvent(evento: AccesoEvento) {
        val siguiente = when (evento) {
            is AccesoEvento.NombreCambiado -> uiState.copy(nombre = evento.valor)
            is AccesoEvento.CorreoCambiado -> uiState.copy(correo = evento.valor)
            is AccesoEvento.ContrasenaCambiada -> uiState.copy(contrasena = evento.valor)
            is AccesoEvento.ConfirmacionCambiada -> uiState.copy(confirmacion = evento.valor)
            AccesoEvento.AlternarContrasena -> uiState.copy(mostrarContrasena = !uiState.mostrarContrasena)
            AccesoEvento.AlternarConfirmacion -> uiState.copy(mostrarConfirmacion = !uiState.mostrarConfirmacion)
            AccesoEvento.Enviar -> uiState.copy(enviado = true)
            AccesoEvento.CerrarAviso -> uiState.copy(mostrarAviso = false)
        }
        val errores = if (siguiente.enviado) {
            AccesoValidador.validar(registrando, siguiente.nombre, siguiente.correo, siguiente.contrasena, siguiente.confirmacion)
        } else {
            AccesoErrores()
        }
        uiState = siguiente.copy(
            errores = errores,
            mostrarAviso = if (evento == AccesoEvento.Enviar) !errores.hayErrores else siguiente.mostrarAviso
        )
        if (evento == AccesoEvento.Enviar && !errores.hayErrores) guardarUsuario(siguiente)
    }

    // Guarda nombre y correo en Room; en el login se conserva el nombre que ya había
    private fun guardarUsuario(estado: AccesoUiState) {
        viewModelScope.launch {
            try {
                val nombre = if (registrando) estado.nombre.trim() else usuarios.leer()?.nombre.orEmpty()
                val correo = estado.correo.trim()
                usuarios.guardar(nombre, correo)
                correoGuardado = correo
            } catch (error: Exception) {
                Log.e("AccesoViewModel", "No se pudo guardar el usuario", error)
            }
        }
    }

    // Limpia el formulario; en el login vuelve a dejar el último correo guardado
    fun reiniciar() {
        uiState = AccesoUiState(correo = if (registrando) "" else correoGuardado)
    }
}

class InicioSesionViewModel(usuarios: UsuarioRepository) : AccesoViewModel(registrando = false, usuarios = usuarios)

class RegistroViewModel(usuarios: UsuarioRepository) : AccesoViewModel(registrando = true, usuarios = usuarios)
