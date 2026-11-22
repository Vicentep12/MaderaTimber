package com.duoc.madertimber.data.model

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

// Prueba de las reglas de validación del formulario de registro
class AccesoValidadorTest {

    @Test
    fun registroMarcaCorreoInvalidoClaveCortaYConfirmacionDistinta() {
        val errores = AccesoValidador.validar(
            registrando = true,
            nombre = "Ana Pérez",
            correo = "ana.duocuc.cl",
            contrasena = "corta",
            confirmacion = "otra"
        )

        assertTrue(errores.correo)
        assertTrue(errores.contrasena)
        assertTrue(errores.confirmacion)
        assertFalse(errores.nombre)
    }
}
