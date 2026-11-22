package com.duoc.madertimber.data.remote

import com.google.gson.annotations.SerializedName

// Datos tal como los envía y recibe la API de Xano

data class RegistroRequest(
    @SerializedName("name") val nombre: String,
    @SerializedName("email") val correo: String,
    @SerializedName("password") val contrasena: String
)

data class InicioSesionRequest(
    @SerializedName("email") val correo: String,
    @SerializedName("password") val contrasena: String
)

data class TokenResponse(
    @SerializedName("authToken") val token: String
)

data class UsuarioResponse(
    @SerializedName("name") val nombre: String,
    @SerializedName("email") val correo: String
)

data class LineaDto(
    val codigo: String,
    val nombre: String,
    val descripcion: String,
    val color: String,
    val anios: List<Int>
)

data class ProyectoDto(
    val codigo: String,
    @SerializedName("linea_codigo") val lineaCodigo: String,
    val titulo: String,
    val seguimientos: List<SeguimientoDto>
)

data class SeguimientoDto(
    val anio: Int,
    val estado: String,
    val avancePorcentaje: Double
)

data class InvestigadorDto(
    val codigo: String,
    val nombre: String,
    val especialidad: String,
    val vinculaciones: List<VinculacionDto>
)

data class VinculacionDto(
    val lineaId: String,
    val anio: Int
)

data class PublicacionDto(
    val codigo: String,
    @SerializedName("linea_codigo") val lineaCodigo: String,
    val titulo: String,
    val tipo: String,
    val anio: Int
)

data class FavoritoDto(
    val id: Int,
    val categoria: String,
    @SerializedName("item_codigo") val itemCodigo: String,
    val titulo: String
)

data class FavoritoRequest(
    val categoria: String,
    @SerializedName("item_codigo") val itemCodigo: String,
    val titulo: String
)
