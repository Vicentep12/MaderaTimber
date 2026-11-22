package com.duoc.madertimber.data.remote

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

// Endpoints de Xano: el grupo de autenticación y el grupo del panel tienen rutas base distintas
interface XanoApi {

    @POST("$GRUPO_AUTENTICACION/auth/signup")
    suspend fun registrar(@Body datos: RegistroRequest): TokenResponse

    @POST("$GRUPO_AUTENTICACION/auth/login")
    suspend fun iniciarSesion(@Body datos: InicioSesionRequest): TokenResponse

    @GET("$GRUPO_AUTENTICACION/auth/me")
    suspend fun obtenerUsuario(@Header("Authorization") autorizacion: String): UsuarioResponse

    @GET("$GRUPO_PANEL/lineas")
    suspend fun obtenerLineas(): List<LineaDto>

    @GET("$GRUPO_PANEL/proyectos")
    suspend fun obtenerProyectos(): List<ProyectoDto>

    @GET("$GRUPO_PANEL/investigadores")
    suspend fun obtenerInvestigadores(): List<InvestigadorDto>

    @GET("$GRUPO_PANEL/publicaciones")
    suspend fun obtenerPublicaciones(): List<PublicacionDto>

    @GET("$GRUPO_PANEL/favoritos")
    suspend fun obtenerFavoritos(@Header("Authorization") autorizacion: String): List<FavoritoDto>

    @POST("$GRUPO_PANEL/favoritos")
    suspend fun agregarFavorito(
        @Header("Authorization") autorizacion: String,
        @Body datos: FavoritoRequest
    ): FavoritoDto

    @DELETE("$GRUPO_PANEL/favoritos/{favoritoId}")
    suspend fun quitarFavorito(
        @Header("Authorization") autorizacion: String,
        @Path("favoritoId") favoritoId: Int
    )

    companion object {
        const val URL_BASE = "https://x8ki-letl-twmt.n7.xano.io/"

        // Las rutas empiezan con "/" para que Retrofit no confunda "api:" con un esquema de URL
        const val GRUPO_AUTENTICACION = "/api:vZX-U2M6"
        const val GRUPO_PANEL = "/api:VKaJBDOl"
    }
}
