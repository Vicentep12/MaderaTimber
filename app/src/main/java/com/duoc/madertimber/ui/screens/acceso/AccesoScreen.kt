package com.duoc.madertimber.ui.screens.acceso

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.duoc.madertimber.ui.theme.MaderaTimberTheme

@Composable
fun InicioSesionScreen(
    estado: AccesoUiState,
    onEvent: (AccesoEvento) -> Unit,
    onRegistro: () -> Unit,
    onInvitado: () -> Unit,
    modifier: Modifier = Modifier
) {
    FormularioAcceso(false, estado, onEvent, onRegistro, onInvitado, modifier)
}

@Composable
fun RegistroScreen(
    estado: AccesoUiState,
    onEvent: (AccesoEvento) -> Unit,
    onInicioSesion: () -> Unit,
    onInvitado: () -> Unit,
    modifier: Modifier = Modifier
) {
    FormularioAcceso(true, estado, onEvent, onInicioSesion, onInvitado, modifier)
}

@Composable
private fun FormularioAcceso(
    registrando: Boolean,
    estado: AccesoUiState,
    onEvent: (AccesoEvento) -> Unit,
    onCambiarFormulario: () -> Unit,
    onInvitado: () -> Unit,
    modifier: Modifier = Modifier
) {
    val administradorFoco = LocalFocusManager.current
    val controladorTeclado = LocalSoftwareKeyboardController.current
    val correoFoco = remember { FocusRequester() }
    val contrasenaFoco = remember { FocusRequester() }
    val confirmacionFoco = remember { FocusRequester() }

    LaunchedEffect(estado.mostrarAviso) {
        administradorFoco.clearFocus(force = true)
        controladorTeclado?.hide()
    }

    fun enviar() {
        administradorFoco.clearFocus(force = true)
        controladorTeclado?.hide()
        onEvent(AccesoEvento.Enviar)
    }

    fun salirDelFormulario(alSalir: () -> Unit) {
        administradorFoco.clearFocus(force = true)
        controladorTeclado?.hide()
        alSalir()
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
        if (registrando) {
            TextButton(
                onClick = { salirDelFormulario(onCambiarFormulario) },
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
                    if (registrando) "Crea tu cuenta" else "Bienvenido de nuevo",
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
                if (registrando) {
                    OutlinedTextField(
                        value = estado.nombre, onValueChange = { onEvent(AccesoEvento.NombreCambiado(it)) },
                        label = { Text("Nombre completo") }, singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = { correoFoco.requestFocus() }),
                        leadingIcon = { IconoCampo(Icons.Default.Person, "nombre", estado.errores.nombre) },
                        isError = estado.errores.nombre,
                        supportingText = { if (estado.errores.nombre) Text("Ingresa tu nombre.") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                OutlinedTextField(
                    value = estado.correo, onValueChange = { onEvent(AccesoEvento.CorreoCambiado(it)) },
                    label = { Text("Correo electrónico") }, singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(onNext = { contrasenaFoco.requestFocus() }),
                    leadingIcon = { IconoCampo(Icons.Default.Email, "correo electrónico", estado.errores.correo) },
                    isError = estado.errores.correo,
                    supportingText = { if (estado.errores.correo) Text("Ingresa un correo válido.") },
                    modifier = Modifier.fillMaxWidth().focusRequester(correoFoco)
                )
                CampoContrasena(
                    valor = estado.contrasena, alCambiar = { onEvent(AccesoEvento.ContrasenaCambiada(it)) }, etiqueta = "Contraseña",
                    visible = estado.mostrarContrasena, alAlternar = { onEvent(AccesoEvento.AlternarContrasena) },
                    error = estado.errores.contrasena,
                    mensaje = if (registrando) "Usa al menos 8 caracteres." else "Ingresa tu contraseña.",
                    pista = if (registrando) "Mínimo 8 caracteres" else null,
                    accionTeclado = if (registrando) ImeAction.Next else ImeAction.Done,
                    alAccionTeclado = {
                        if (registrando) confirmacionFoco.requestFocus() else enviar()
                    },
                    modifier = Modifier.focusRequester(contrasenaFoco)
                )
                if (registrando) {
                    CampoContrasena(
                        valor = estado.confirmacion, alCambiar = { onEvent(AccesoEvento.ConfirmacionCambiada(it)) },
                        etiqueta = "Confirma tu contraseña", visible = estado.mostrarConfirmacion,
                        alAlternar = { onEvent(AccesoEvento.AlternarConfirmacion) },
                        error = estado.errores.confirmacion,
                        mensaje = "Las contraseñas no coinciden.",
                        accionTeclado = ImeAction.Done,
                        alAccionTeclado = ::enviar,
                        modifier = Modifier.focusRequester(confirmacionFoco)
                    )
                }
                Button(
                    onClick = ::enviar, modifier = Modifier.fillMaxWidth()
                ) { Text(if (registrando) "Crear cuenta" else "Iniciar sesión") }
                TextButton(onClick = { salirDelFormulario(onCambiarFormulario) }, modifier = Modifier.fillMaxWidth()) {
                    Text(if (registrando) "Ya tengo cuenta · Iniciar sesión" else "No tengo cuenta · Registrarme")
                }
            }
        }
        TextButton(onClick = { salirDelFormulario(onInvitado) }) { Text("Explorar como invitado") }
        if (!registrando) {
            Text(
                "Enviar datos válidos abre el aviso de demostración.",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
    if (estado.mostrarAviso) {
        AlertDialog(
            onDismissRequest = { onEvent(AccesoEvento.CerrarAviso) },
            title = { Text("Demostración") },
            text = { Text("El acceso con cuenta todavía no está disponible. No se ha creado ninguna cuenta ni iniciado sesión. Puedes explorar el panel como invitado.") },
            confirmButton = {
                TextButton(onClick = { onEvent(AccesoEvento.CerrarAviso); onInvitado() }) { Text("Explorar panel") }
            },
            dismissButton = {
                TextButton(onClick = { onEvent(AccesoEvento.CerrarAviso) }) { Text("Volver") }
            }
        )
    }
}

@Composable
private fun CampoContrasena(
    valor: String,
    alCambiar: (String) -> Unit,
    etiqueta: String,
    visible: Boolean,
    alAlternar: () -> Unit,
    error: Boolean,
    mensaje: String,
    accionTeclado: ImeAction,
    alAccionTeclado: () -> Unit,
    modifier: Modifier = Modifier,
    pista: String? = null
) {
    val controlDebajo = LocalConfiguration.current.screenWidthDp < 360 ||
        LocalDensity.current.fontScale > 1.3f
    val controlVisibilidad: @Composable () -> Unit = {
        TextButton(
            onClick = alAlternar,
            modifier = Modifier.semantics {
                contentDescription = "${if (visible) "Ocultar" else "Mostrar"}: $etiqueta"
            }
        ) { Text(if (visible) "Ocultar" else "Mostrar") }
    }
    Column(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = valor, onValueChange = alCambiar,
            label = { Text(etiqueta) }, singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = accionTeclado
            ),
            keyboardActions = KeyboardActions(
                onNext = { alAccionTeclado() },
                onDone = { alAccionTeclado() }
            ),
            visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
            leadingIcon = { IconoCampo(Icons.Default.Lock, etiqueta.lowercase(), error) },
            trailingIcon = if (controlDebajo) null else controlVisibilidad,
            isError = error,
            supportingText = {
                if (error) Text(mensaje)
                else if (pista != null) Text(pista)
            },
            modifier = Modifier.fillMaxWidth()
        )
        if (controlDebajo) controlVisibilidad()
    }
}

// Ícono del campo; si hay error cambia a una advertencia en rojo
@Composable
private fun IconoCampo(icono: ImageVector, campo: String, error: Boolean) {
    Icon(
        imageVector = if (error) Icons.Default.Warning else icono,
        contentDescription = if (error) "Error en $campo" else null,
        tint = if (error) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Preview(showBackground = true)
@Composable
private fun InicioSesionPreview() {
    MaderaTimberTheme { InicioSesionScreen(AccesoUiState(), onEvent = {}, onRegistro = {}, onInvitado = {}) }
}

@Preview(showBackground = true)
@Composable
private fun RegistroPreview() {
    MaderaTimberTheme { RegistroScreen(AccesoUiState(), onEvent = {}, onInicioSesion = {}, onInvitado = {}) }
}
