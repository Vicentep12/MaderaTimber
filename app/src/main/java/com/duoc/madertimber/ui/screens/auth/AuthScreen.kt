package com.duoc.madertimber.ui.screens.auth

import android.util.Patterns
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.duoc.madertimber.ui.theme.MaderTimberTheme

@Composable
fun LoginScreen(onRegister: () -> Unit, onGuest: () -> Unit, modifier: Modifier = Modifier) {
    AuthForm(false, onRegister, onGuest, modifier)
}

@Composable
fun RegisterScreen(onLogin: () -> Unit, onGuest: () -> Unit, modifier: Modifier = Modifier) {
    AuthForm(true, onLogin, onGuest, modifier)
}

@Composable
private fun AuthForm(
    registering: Boolean,
    onSwitch: () -> Unit,
    onGuest: () -> Unit,
    modifier: Modifier = Modifier
) {
    var name by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    // Las contraseñas permanecen solo en memoria, nunca en el estado guardado.
    var password by remember { mutableStateOf("") }
    var confirmation by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var submitted by remember { mutableStateOf(false) }
    var showNotice by remember { mutableStateOf(false) }
    val nameError = registering && name.isBlank()
    val emailError = !Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()
    val passwordError = if (registering) password.length < 8 else password.isBlank()
    val confirmationError = registering && confirmation != password

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
                        value = name, onValueChange = { name = it },
                        label = { Text("Nombre completo") }, singleLine = true,
                        isError = submitted && nameError,
                        supportingText = { if (submitted && nameError) Text("Ingresa tu nombre.") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                OutlinedTextField(
                    value = email, onValueChange = { email = it },
                    label = { Text("Correo electrónico") }, singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    isError = submitted && emailError,
                    supportingText = { if (submitted && emailError) Text("Ingresa un correo válido.") },
                    modifier = Modifier.fillMaxWidth()
                )
                PasswordField(
                    value = password, onValueChange = { password = it }, label = "Contraseña",
                    visible = showPassword, onToggle = { showPassword = !showPassword },
                    error = submitted && passwordError,
                    message = if (registering) "Usa al menos 8 caracteres." else "Ingresa tu contraseña."
                )
                if (registering) {
                    PasswordField(
                        value = confirmation, onValueChange = { confirmation = it },
                        label = "Confirma tu contraseña", visible = showPassword,
                        onToggle = { showPassword = !showPassword },
                        error = submitted && confirmationError,
                        message = "Las contraseñas no coinciden."
                    )
                }
                Button(
                    onClick = {
                        submitted = true
                        if (!nameError && !emailError && !passwordError && !confirmationError) {
                            showNotice = true
                        }
                    }, modifier = Modifier.fillMaxWidth()
                ) { Text(if (registering) "Crear cuenta" else "Iniciar sesión") }
                TextButton(onClick = onSwitch, modifier = Modifier.fillMaxWidth()) {
                    Text(if (registering) "Ya tengo cuenta · Iniciar sesión" else "No tengo cuenta · Registrarme")
                }
            }
        }
        TextButton(onClick = onGuest) { Text("Explorar como invitado") }
    }
    if (showNotice) {
        AlertDialog(
            onDismissRequest = { showNotice = false },
            title = { Text("Demostración") },
            text = { Text("El acceso con cuenta todavía no está disponible. No se ha creado ninguna cuenta ni iniciado sesión. Puedes explorar el panel como invitado.") },
            confirmButton = {
                TextButton(onClick = { showNotice = false; onGuest() }) { Text("Explorar panel") }
            },
            dismissButton = {
                TextButton(onClick = { showNotice = false }) { Text("Volver") }
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
    MaderTimberTheme { LoginScreen(onRegister = {}, onGuest = {}) }
}

@Preview(showBackground = true)
@Composable
private fun RegisterPreview() {
    MaderTimberTheme { RegisterScreen(onLogin = {}, onGuest = {}) }
}
