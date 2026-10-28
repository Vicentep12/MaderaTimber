package com.duoc.madertimber.data.model

data class Investigador(
    val id: String,
    val nombre: String,
    val especialidad: String,
    val vinculaciones: List<VinculacionInvestigador>
)