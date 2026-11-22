package com.duoc.madertimber.data.repository

import com.duoc.madertimber.data.model.CategoriaRelacionada
import com.duoc.madertimber.data.model.Favorito
import com.duoc.madertimber.data.remote.FavoritoDto
import com.duoc.madertimber.data.remote.FavoritoRequest
import com.duoc.madertimber.data.remote.XanoApi

// Lista, agrega y quita favoritos en Xano con el token de la sesión
class FavoritosRepository(
    private val api: XanoApi,
    private val sesiones: SesionRepository
) {
    suspend fun listar(): List<Favorito> =
        api.obtenerFavoritos(autorizacion()).map { it.aFavorito() }

    suspend fun agregar(categoria: CategoriaRelacionada, itemCodigo: String, titulo: String): Favorito =
        api.agregarFavorito(autorizacion(), FavoritoRequest(categoria.name, itemCodigo, titulo)).aFavorito()

    suspend fun quitar(favoritoId: Int) {
        api.quitarFavorito(autorizacion(), favoritoId)
    }

    private fun autorizacion(): String = "Bearer ${sesiones.sesion.value?.token}"

    private fun FavoritoDto.aFavorito() =
        Favorito(id, CategoriaRelacionada.valueOf(categoria), itemCodigo, titulo)
}
