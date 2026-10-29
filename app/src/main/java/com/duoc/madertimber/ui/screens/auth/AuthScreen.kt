package com.duoc.madertimber.ui.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
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
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val emailFocus = remember { FocusRequester() }
    val passwordFocus = remember { FocusRequester() }
    val confirmationFocus = remember { FocusRequester() }

    LaunchedEffect(state.showNotice) {
        focusManager.clearFocus(force = true)
        keyboardController?.hide()
    }

    fun submit() {
        focusManager.clearFocus(force = true)
        keyboardController?.hide()
        onEvent(AuthEvent.Submit)
    }

    fun leaveForm(onLeave: () -> Unit) {
        focusManager.clearFocus(force = true)
        keyboardController?.hide()
        onLeave()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (registering) {
            TextButton(
                onClick = { leaveForm(onSwitch) },
                modifier = Modifier.align(Alignment.Start)
            ) {
                Text("← Volver al inicio")
            }
        }
        Text("CENAMAD", style = MaterialTheme.typography.headlineLarge)
        Text("Conecta con la investigación", style = MaterialTheme.typography.titleMedium)
        Card(modifier = Modifier.widthIn(max = 480.dp).fillMaxWidth()) {
            Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    if (registering) "Crea tu cuenta" else "Bienvenido de nuevo",
                    style = MaterialTheme.typography.headlineSmall
                )
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Text(
                        "Demostración · Las cuentas aún no están habilitadas.",
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                if (registering) {
                    OutlinedTextField(
                        value = state.name, onValueChange = { onEvent(AuthEvent.NameChanged(it)) },
                        label = { Text("Nombre completo") }, singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = { emailFocus.requestFocus() }),
                        isError = state.errors.name,
                        supportingText = { if (state.errors.name) Text("Ingresa tu nombre.") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                OutlinedTextField(
                    value = state.email, onValueChange = { onEvent(AuthEvent.EmailChanged(it)) },
                    label = { Text("Correo electrónico") }, singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(onNext = { passwordFocus.requestFocus() }),
                    isError = state.errors.email,
                    supportingText = { if (state.errors.email) Text("Ingresa un correo válido.") },
                    modifier = Modifier.fillMaxWidth().focusRequester(emailFocus)
                )
                PasswordField(
                    value = state.password, onValueChange = { onEvent(AuthEvent.PasswordChanged(it)) }, label = "Contraseña",
                    visible = state.showPassword, onToggle = { onEvent(AuthEvent.TogglePassword) },
                    error = state.errors.password,
                    message = if (registering) "Usa al menos 8 caracteres." else "Ingresa tu contraseña.",
                    hint = if (registering) "Mínimo 8 caracteres" else null,
                    imeAction = if (registering) ImeAction.Next else ImeAction.Done,
                    onImeAction = {
                        if (registering) confirmationFocus.requestFocus() else submit()
                    },
                    modifier = Modifier.focusRequester(passwordFocus)
                )
                if (registering) {
                    PasswordField(
                        value = state.confirmation, onValueChange = { onEvent(AuthEvent.ConfirmationChanged(it)) },
                        label = "Confirma tu contraseña", visible = state.showConfirmation,
                        onToggle = { onEvent(AuthEvent.ToggleConfirmation) },
                        error = state.errors.confirmation,
                        message = "Las contraseñas no coinciden.",
                        imeAction = ImeAction.Done,
                        onImeAction = ::submit,
                        modifier = Modifier.focusRequester(confirmationFocus)
                    )
                }
                Button(
                    onClick = ::submit, modifier = Modifier.fillMaxWidth()
                ) { Text(if (registering) "Crear cuenta" else "Iniciar sesión") }
                TextButton(onClick = { leaveForm(onSwitch) }, modifier = Modifier.fillMaxWidth()) {
                    Text(if (registering) "Ya tengo cuenta · Iniciar sesión" else "No tengo cuenta · Registrarme")
                }
            }
        }
        TextButton(onClick = { leaveForm(onGuest) }) { Text("Explorar como invitado") }
        if (!registering) {
            Text(
                "Enviar datos válidos abre el aviso de demostración.",
                style = MaterialTheme.typography.bodySmall
            )
        }
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
    message: String,
    imeAction: ImeAction,
    onImeAction: () -> Unit,
    modifier: Modifier = Modifier,
    hint: String? = null
) {
    val controlDebajo = LocalConfiguration.current.screenWidthDp < 360 ||
        LocalDensity.current.fontScale > 1.3f
    val controlVisibilidad: @Composable () -> Unit = {
        TextButton(
            onClick = onToggle,
            modifier = Modifier.semantics {
                contentDescription = "${if (visible) "Ocultar" else "Mostrar"}: $label"
            }
        ) { Text(if (visible) "Ocultar" else "Mostrar") }
    }
    Column(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value, onValueChange = onValueChange,
            label = { Text(label) }, singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = imeAction
            ),
            keyboardActions = KeyboardActions(
                onNext = { onImeAction() },
                onDone = { onImeAction() }
            ),
            visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = if (controlDebajo) null else controlVisibilidad,
            isError = error,
            supportingText = {
                if (error) Text(message)
                else if (hint != null) Text(hint)
            },
            modifier = Modifier.fillMaxWidth()
        )
        if (controlDebajo) controlVisibilidad()
    }
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
