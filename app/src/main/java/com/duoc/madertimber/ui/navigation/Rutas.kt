package com.duoc.madertimber.ui.navigation

import android.net.Uri
import com.duoc.madertimber.data.model.CategoriaRelacionada
import com.duoc.madertimber.data.model.TipoIndicador

object Rutas {
    const val LOGIN = "login"
    const val REGISTRO = "registro"
    const val DASHBOARD = "dashboard"
    const val LINEA = "linea/{lineaId}/{anio}"
    const val INDICADOR = "indicador/{indicadorId}/{anio}"
    const val RELACIONADOS = "relacionados/{lineaId}/{anio}?tipo={tipo}"
    const val FICHA = "ficha/{categoria}/{itemId}/{lineaId}/{anio}?tipo={tipo}"

    fun linea(lineaId: String, anio: Int): String =
        "linea/${Uri.encode(lineaId)}/$anio"

    fun indicador(indicadorId: String, anio: Int): String =
        "indicador/${Uri.encode(indicadorId)}/$anio"

    fun relacionados(lineaId: String, anio: Int, tipo: TipoIndicador? = null): String =
        "relacionados/${Uri.encode(lineaId)}/$anio" +
            (tipo?.let { "?tipo=${it.name}" } ?: "")

    fun ficha(
        categoria: CategoriaRelacionada,
        itemId: String,
        lineaId: String,
        anio: Int,
        tipo: TipoIndicador?
    ): String = "ficha/${categoria.name}/${Uri.encode(itemId)}/${Uri.encode(lineaId)}/$anio" +
        (tipo?.let { "?tipo=${it.name}" } ?: "")
}
