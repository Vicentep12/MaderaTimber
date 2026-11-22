package com.duoc.madertimber.data.repository

import com.duoc.madertimber.data.remote.InicioSesionRequest
import com.duoc.madertimber.data.remote.RegistroRequest
import com.duoc.madertimber.data.remote.XanoApi
import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.HttpException

// Usuario con sesión iniciada; el token solo se guarda en memoria mientras la app está abierta
data class SesionUsuario(val token: String, val nombre: String, val correo: String)

// Inicia sesión o crea la cuenta en Xano y guarda nombre y correo en Room
class SesionRepository(
    private val api: XanoApi,
    private val usuarios: UsuarioRepository
) {
    // Los ViewModels observan este valor para saber si hay alguien con sesión iniciada
    val sesion = MutableStateFlow<SesionUsuario?>(null)

    suspend fun iniciarSesion(correo: String, contrasena: String) {
        try {
            val respuesta = api.iniciarSesion(InicioSesionRequest(correo, contrasena))
            abrirSesion(respuesta.token)
        } catch (error: HttpException) {
            if (error.code() == 403) throw Exception("Correo o contraseña incorrectos.")
            throw error
        }
    }

    suspend fun registrar(nombre: String, correo: String, contrasena: String) {
        try {
            val respuesta = api.registrar(RegistroRequest(nombre, correo, contrasena))
            abrirSesion(respuesta.token)
        } catch (error: HttpException) {
            if (error.code() == 403) throw Exception("Ya existe una cuenta con ese correo.")
            throw error
        }
    }

    fun cerrarSesion() {
        sesion.value = null
    }

    // Con el token pide los datos del usuario (auth/me)
    private suspend fun abrirSesion(token: String) {
        val usuario = api.obtenerUsuario("Bearer $token")
        usuarios.guardar(usuario.nombre, usuario.correo)
        sesion.value = SesionUsuario(token, usuario.nombre, usuario.correo)
    }
}
