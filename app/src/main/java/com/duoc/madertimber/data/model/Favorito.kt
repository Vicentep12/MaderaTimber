package com.duoc.madertimber.data.model

// Proyecto, investigador o publicación que el usuario guardó en su cuenta
data class Favorito(
    val id: Int,
    val categoria: CategoriaRelacionada,
    val itemCodigo: String,
    val titulo: String
)
