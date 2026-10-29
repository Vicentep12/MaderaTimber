package com.duoc.madertimber.ui.screens.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.duoc.madertimber.model.auth.AuthErrors
import com.duoc.madertimber.model.auth.AuthValidator

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

abstract class AuthViewModel(private val registering: Boolean) : ViewModel() {
    // Estado observable de solo lectura para la vista. No se persisten credenciales.
    var uiState by mutableStateOf(AuthUiState())
        private set

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
    }

    fun reset() {
        uiState = AuthUiState()
    }
}

class LoginViewModel : AuthViewModel(registering = false)

class RegisterViewModel : AuthViewModel(registering = true)
