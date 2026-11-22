package com.duoc.madertimber.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duoc.madertimber.data.model.CategoriaRelacionada
import com.duoc.madertimber.data.model.Favorito
import com.duoc.madertimber.data.remote.mensajeDeError
import com.duoc.madertimber.data.repository.FavoritosRepository
import com.duoc.madertimber.data.repository.SesionRepository
import kotlinx.coroutines.launch

data class FavoritosUiState(
    val nombreUsuario: String? = null,
    val favoritos: List<Favorito> = emptyList(),
    val cargando: Boolean = false,
    val mensaje: String? = null
) {
    val sesionIniciada: Boolean get() = nombreUsuario != null

    fun buscar(categoria: CategoriaRelacionada, itemCodigo: String): Favorito? =
        favoritos.find { it.categoria == categoria && it.itemCodigo == itemCodigo }
}

sealed interface FavoritosEvento {
    data class Alternar(val categoria: CategoriaRelacionada, val itemCodigo: String, val titulo: String) : FavoritosEvento
    data class Quitar(val favorito: Favorito) : FavoritosEvento
}

// Mantiene la lista de favoritos del usuario y la sincroniza con la API
class FavoritosViewModel(
    private val sesiones: SesionRepository,
    private val repositorio: FavoritosRepository
) : ViewModel() {
    var uiState by mutableStateOf(FavoritosUiState())
        private set

    // Cuando alguien inicia sesión se descargan sus favoritos; al cerrarla se limpia la lista
    init {
        viewModelScope.launch {
            sesiones.sesion.collect { sesion ->
                uiState = FavoritosUiState(nombreUsuario = sesion?.nombre)
                if (sesion != null) llamarApi { uiState = uiState.copy(favoritos = repositorio.listar()) }
            }
        }
    }

    fun onEvent(evento: FavoritosEvento) {
        if (uiState.cargando) return
        when (evento) {
            is FavoritosEvento.Alternar -> {
                val existente = uiState.buscar(evento.categoria, evento.itemCodigo)
                if (existente != null) quitar(existente) else agregar(evento)
            }
            is FavoritosEvento.Quitar -> quitar(evento.favorito)
        }
    }

    fun cerrarSesion() = sesiones.cerrarSesion()

    private fun agregar(evento: FavoritosEvento.Alternar) = llamarApi {
        val nuevo = repositorio.agregar(evento.categoria, evento.itemCodigo, evento.titulo)
        uiState = uiState.copy(favoritos = uiState.favoritos + nuevo, mensaje = "Guardado en tus favoritos.")
    }

    private fun quitar(favorito: Favorito) = llamarApi {
        repositorio.quitar(favorito.id)
        uiState = uiState.copy(favoritos = uiState.favoritos - favorito, mensaje = "Quitado de tus favoritos.")
    }

    // Marca la carga mientras se llama a la API y muestra el error si algo falla
    private fun llamarApi(accion: suspend () -> Unit) {
        uiState = uiState.copy(cargando = true, mensaje = null)
        viewModelScope.launch {
            try {
                accion()
            } catch (error: Exception) {
                uiState = uiState.copy(mensaje = mensajeDeError(error))
            }
            uiState = uiState.copy(cargando = false)
        }
    }
}
