package com.duoc.madertimber.ui.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.duoc.madertimber.ui.theme.MaderTimberTheme

@Composable
fun LoginScreen(
    state: AuthUiState,
    onEvent: (AuthEvent) -> Unit,
    onRegister: () -> Unit,
    onGuest: () -> Unit,
    modifier: Modifier = Modifier
) {
    AuthForm(false, state, onEvent, onRegister, onGuest, modifier)
}

@Composable
fun RegisterScreen(
    state: AuthUiState,
    onEvent: (AuthEvent) -> Unit,
    onLogin: () -> Unit,
    onGuest: () -> Unit,
    modifier: Modifier = Modifier
) {
    AuthForm(true, state, onEvent, onLogin, onGuest, modifier)
}

@Composable
private fun AuthForm(
    registering: Boolean,
    state: AuthUiState,
    onEvent: (AuthEvent) -> Unit,
    onSwitch: () -> Unit,
    onGuest: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize().imePadding()
            .verticalScroll(rememberScrollState()).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text("CENAMAD", style = MaterialTheme.typography.headlineLarge)
        Text("Conecta con la investigación", style = MaterialTheme.typography.titleMedium)
        Card(modifier = Modifier.widthIn(max = 480.dp).fillMaxWidth()) {
            Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    if (registering) "Crea tu cuenta" else "Bienvenido de nuevo",
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(if (registering) "Explora las líneas de investigación de CENAMAD."
                    else "Ingresa tus datos para iniciar sesión.")
                Text("Vista de demostración. Las cuentas aún no están habilitadas.",
                    style = MaterialTheme.typography.bodySmall)
                if (registering) {
                    OutlinedTextField(
                        value = state.name, onValueChange = { onEvent(AuthEvent.NameChanged(it)) },
                        label = { Text("Nombre completo") }, singleLine = true,
                        isError = state.errors.name,
                        supportingText = { if (state.errors.name) Text("Ingresa tu nombre.") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                OutlinedTextField(
                    value = state.email, onValueChange = { onEvent(AuthEvent.EmailChanged(it)) },
                    label = { Text("Correo electrónico") }, singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    isError = state.errors.email,
                    supportingText = { if (state.errors.email) Text("Ingresa un correo válido.") },
                    modifier = Modifier.fillMaxWidth()
                )
                PasswordField(
                    value = state.password, onValueChange = { onEvent(AuthEvent.PasswordChanged(it)) }, label = "Contraseña",
                    visible = state.showPassword, onToggle = { onEvent(AuthEvent.TogglePassword) },
                    error = state.errors.password,
                    message = if (registering) "Usa al menos 8 caracteres." else "Ingresa tu contraseña."
                )
                if (registering) {
                    PasswordField(
                        value = state.confirmation, onValueChange = { onEvent(AuthEvent.ConfirmationChanged(it)) },
                        label = "Confirma tu contraseña", visible = state.showPassword,
                        onToggle = { onEvent(AuthEvent.TogglePassword) },
                        error = state.errors.confirmation,
                        message = "Las contraseñas no coinciden."
                    )
                }
                Button(
                    onClick = { onEvent(AuthEvent.Submit) }, modifier = Modifier.fillMaxWidth()
                ) { Text(if (registering) "Crear cuenta" else "Iniciar sesión") }
                TextButton(onClick = onSwitch, modifier = Modifier.fillMaxWidth()) {
                    Text(if (registering) "Ya tengo cuenta · Iniciar sesión" else "No tengo cuenta · Registrarme")
                }
            }
        }
        TextButton(onClick = onGuest) { Text("Explorar como invitado") }
    }
    if (state.showNotice) {
        AlertDialog(
            onDismissRequest = { onEvent(AuthEvent.DismissNotice) },
            title = { Text("Demostración") },
            text = { Text("El acceso con cuenta todavía no está disponible. No se ha creado ninguna cuenta ni iniciado sesión. Puedes explorar el panel como invitado.") },
            confirmButton = {
                TextButton(onClick = { onEvent(AuthEvent.DismissNotice); onGuest() }) { Text("Explorar panel") }
            },
            dismissButton = {
                TextButton(onClick = { onEvent(AuthEvent.DismissNotice) }) { Text("Volver") }
            }
        )
    }
}

@Composable
private fun PasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    visible: Boolean,
    onToggle: () -> Unit,
    error: Boolean,
    message: String
) {
    OutlinedTextField(
        value = value, onValueChange = onValueChange,
        label = { Text(label) }, singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = { TextButton(onClick = onToggle) { Text(if (visible) "Ocultar" else "Mostrar") } },
        isError = error,
        supportingText = { if (error) Text(message) },
        modifier = Modifier.fillMaxWidth()
    )
}

@Preview(showBackground = true)
@Composable
private fun LoginPreview() {
    MaderTimberTheme { LoginScreen(AuthUiState(), onEvent = {}, onRegister = {}, onGuest = {}) }
}

@Preview(showBackground = true)
@Composable
private fun RegisterPreview() {
    MaderTimberTheme { RegisterScreen(AuthUiState(), onEvent = {}, onLogin = {}, onGuest = {}) }
}
