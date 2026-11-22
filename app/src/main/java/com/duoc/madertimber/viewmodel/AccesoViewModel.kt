package com.duoc.madertimber.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duoc.madertimber.data.model.AccesoErrores
import com.duoc.madertimber.data.model.AccesoValidador
import com.duoc.madertimber.data.remote.mensajeDeError
import com.duoc.madertimber.data.repository.SesionRepository
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
    val cargando: Boolean = false,
    val mensajeError: String? = null,
    val accesoExitoso: Boolean = false,
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
    data object CerrarError : AccesoEvento
}

abstract class AccesoViewModel(
    private val registrando: Boolean,
    private val sesiones: SesionRepository,
    private val usuarios: UsuarioRepository
) : ViewModel() {
    // Estado observable de solo lectura para la vista. La contraseña nunca se guarda.
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

    // Aplica lo que hace el usuario; al enviar valida y, si todo está bien, llama a la API
    fun onEvent(evento: AccesoEvento) {
        if (uiState.cargando) return
        val siguiente = when (evento) {
            is AccesoEvento.NombreCambiado -> uiState.copy(nombre = evento.valor)
            is AccesoEvento.CorreoCambiado -> uiState.copy(correo = evento.valor)
            is AccesoEvento.ContrasenaCambiada -> uiState.copy(contrasena = evento.valor)
            is AccesoEvento.ConfirmacionCambiada -> uiState.copy(confirmacion = evento.valor)
            AccesoEvento.AlternarContrasena -> uiState.copy(mostrarContrasena = !uiState.mostrarContrasena)
            AccesoEvento.AlternarConfirmacion -> uiState.copy(mostrarConfirmacion = !uiState.mostrarConfirmacion)
            AccesoEvento.Enviar -> uiState.copy(enviado = true)
            AccesoEvento.CerrarError -> uiState.copy(mensajeError = null)
        }
        val errores = if (siguiente.enviado) {
            AccesoValidador.validar(registrando, siguiente.nombre, siguiente.correo, siguiente.contrasena, siguiente.confirmacion)
        } else {
            AccesoErrores()
        }
        uiState = siguiente.copy(errores = errores)
        if (evento == AccesoEvento.Enviar && !errores.hayErrores) enviarCredenciales(siguiente)
    }

    // Registra la cuenta o inicia sesión en Xano; si falla deja el mensaje para mostrarlo
    private fun enviarCredenciales(estado: AccesoUiState) {
        uiState = uiState.copy(cargando = true, mensajeError = null)
        viewModelScope.launch {
            try {
                val correo = estado.correo.trim()
                if (registrando) {
                    sesiones.registrar(estado.nombre.trim(), correo, estado.contrasena)
                } else {
                    sesiones.iniciarSesion(correo, estado.contrasena)
                }
                correoGuardado = correo
                uiState = uiState.copy(cargando = false, accesoExitoso = true)
            } catch (error: Exception) {
                uiState = uiState.copy(cargando = false, mensajeError = mensajeDeError(error))
            }
        }
    }

    // Limpia el formulario; en el login vuelve a dejar el último correo guardado
    fun reiniciar() {
        uiState = AccesoUiState(correo = if (registrando) "" else correoGuardado)
    }
}

class InicioSesionViewModel(sesiones: SesionRepository, usuarios: UsuarioRepository) :
    AccesoViewModel(registrando = false, sesiones = sesiones, usuarios = usuarios)

class RegistroViewModel(sesiones: SesionRepository, usuarios: UsuarioRepository) :
    AccesoViewModel(registrando = true, sesiones = sesiones, usuarios = usuarios)
