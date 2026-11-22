package com.duoc.madertimber.data.remote

import retrofit2.HttpException
import java.io.IOException

// Convierte un error de la API en un mensaje que el usuario pueda entender
fun mensajeDeError(error: Throwable): String = when {
    error is IOException -> "No hay conexión con el servidor. Revisa tu internet."
    error is HttpException && error.code() == 401 -> "Tu sesión expiró. Vuelve a iniciar sesión."
    error is HttpException && error.code() == 429 -> "Demasiadas solicitudes. Espera unos segundos e inténtalo de nuevo."
    error is HttpException && error.code() == 400 -> "No se pudo guardar el cambio."
    else -> error.message ?: "Ocurrió un error inesperado."
}
