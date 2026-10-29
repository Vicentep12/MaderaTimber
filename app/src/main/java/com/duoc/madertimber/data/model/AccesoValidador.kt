package com.duoc.madertimber.data.model

import androidx.core.util.PatternsCompat

data class AccesoErrores(
    val nombre: Boolean = false,
    val correo: Boolean = false,
    val contrasena: Boolean = false,
    val confirmacion: Boolean = false
) {
    val hayErrores: Boolean get() = nombre || correo || contrasena || confirmacion
}

object AccesoValidador {
    // Marca cada campo con error; nombre, largo de la clave y confirmación solo se revisan al registrarse
    fun validar(
        registrando: Boolean,
        nombre: String,
        correo: String,
        contrasena: String,
        confirmacion: String
    ): AccesoErrores = AccesoErrores(
        nombre = registrando && nombre.isBlank(),
        correo = !PatternsCompat.EMAIL_ADDRESS.matcher(correo.trim()).matches(),
        contrasena = contrasena.isBlank() || (registrando && contrasena.length < 8),
        confirmacion = registrando && confirmacion != contrasena
    )
}
