package com.duoc.madertimber.ui.screens.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duoc.madertimber.data.model.AuthErrors
import com.duoc.madertimber.data.model.AuthValidator
import com.duoc.madertimber.data.repository.UsuarioRepository
import kotlinx.coroutines.launch

data class AuthUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmation: String = "",
    val showPassword: Boolean = false,
    val showConfirmation: Boolean = false,
    val submitted: Boolean = false,
    val showNotice: Boolean = false,
    val errors: AuthErrors = AuthErrors()
)

sealed interface AuthEvent {
    data class NameChanged(val value: String) : AuthEvent
    data class EmailChanged(val value: String) : AuthEvent
    data class PasswordChanged(val value: String) : AuthEvent
    data class ConfirmationChanged(val value: String) : AuthEvent
    data object TogglePassword : AuthEvent
    data object ToggleConfirmation : AuthEvent
    data object Submit : AuthEvent
    data object DismissNotice : AuthEvent
}

abstract class AuthViewModel(
    private val registering: Boolean,
    private val usuarios: UsuarioRepository
) : ViewModel() {
    // Estado observable de solo lectura para la vista. No se persisten credenciales.
    var uiState by mutableStateOf(AuthUiState())
        private set

    // Último correo guardado en Room, para dejarlo escrito en el login
    private var correoGuardado = ""

    init {
        if (!registering) {
            viewModelScope.launch {
                correoGuardado = usuarios.leer()?.correo.orEmpty()
                if (uiState.email.isEmpty()) uiState = uiState.copy(email = correoGuardado)
            }
        }
    }

    // Aplica lo que hace el usuario; al enviar valida y, si todo está bien, muestra el aviso
    fun onEvent(event: AuthEvent) {
        val next = when (event) {
            is AuthEvent.NameChanged -> uiState.copy(name = event.value)
            is AuthEvent.EmailChanged -> uiState.copy(email = event.value)
            is AuthEvent.PasswordChanged -> uiState.copy(password = event.value)
            is AuthEvent.ConfirmationChanged -> uiState.copy(confirmation = event.value)
            AuthEvent.TogglePassword -> uiState.copy(showPassword = !uiState.showPassword)
            AuthEvent.ToggleConfirmation -> uiState.copy(showConfirmation = !uiState.showConfirmation)
            AuthEvent.Submit -> uiState.copy(submitted = true)
            AuthEvent.DismissNotice -> uiState.copy(showNotice = false)
        }
        val errors = if (next.submitted) {
            AuthValidator.validate(registering, next.name, next.email, next.password, next.confirmation)
        } else {
            AuthErrors()
        }
        uiState = next.copy(
            errors = errors,
            showNotice = if (event == AuthEvent.Submit) !errors.hasErrors else next.showNotice
        )
        if (event == AuthEvent.Submit && !errors.hasErrors) guardarUsuario(next)
    }

    // Guarda nombre y correo en Room; en el login se conserva el nombre que ya había
    private fun guardarUsuario(estado: AuthUiState) {
        viewModelScope.launch {
            try {
                val nombre = if (registering) estado.name.trim() else usuarios.leer()?.nombre.orEmpty()
                val correo = estado.email.trim()
                usuarios.guardar(nombre, correo)
                correoGuardado = correo
            } catch (error: Exception) {
                Log.e("AuthViewModel", "No se pudo guardar el usuario", error)
            }
        }
    }

    // Limpia el formulario; en el login vuelve a dejar el último correo guardado
    fun reset() {
        uiState = AuthUiState(email = if (registering) "" else correoGuardado)
    }
}

class LoginViewModel(usuarios: UsuarioRepository) : AuthViewModel(registering = false, usuarios = usuarios)

class RegisterViewModel(usuarios: UsuarioRepository) : AuthViewModel(registering = true, usuarios = usuarios)
