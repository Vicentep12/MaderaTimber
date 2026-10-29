package com.duoc.madertimber.data.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class ValidacionRutasPanelTest {
    private val linea = LineaInvestigacion("L1", "Línea A", "Descripción", "#008000")
    private val proyecto = Proyecto("P1", "L1", "Proyecto A", emptyList())
    private val datos = DatosRelacionados(
        linea = linea,
        anio = 2023,
        proyectos = listOf(proyecto),
        investigadores = emptyList(),
        publicaciones = emptyList()
    )

    @Test
    fun aceptaLaSeleccionActualYRechazaIdsAniosYContextoDistintos() {
        assertNull(validarContextoRuta("L1", 2023, listOf(linea), listOf(2022, 2023, 2024), "L1", 2023))
        assertNotNull(validarContextoRuta("L9", 2023, listOf(linea), listOf(2022, 2023), "L1", 2023))
        assertNotNull(validarContextoRuta("L1", 2025, listOf(linea), listOf(2022, 2023), "L1", 2023))
        assertNotNull(validarContextoRuta("L1", 2022, listOf(linea), listOf(2022, 2023), "L1", 2023))
    }

    @Test
    fun fichaSoloAceptaUnIdPresenteEnLaCategoriaPermitida() {
        assertNull(validarFichaRuta(CategoriaRelacionada.PROYECTOS, "P1", datos, null))
        assertNotNull(validarFichaRuta(CategoriaRelacionada.PROYECTOS, "P9", datos, null))
        assertNotNull(
            validarFichaRuta(
                CategoriaRelacionada.PROYECTOS,
                "P1",
                datos,
                TipoIndicador.PUBLICACIONES_GENERADAS
            )
        )
    }

    @Test
    fun indicadorDeOrigenLimitaLasPestaniasDisponibles() {
        assertEquals(
            listOf(CategoriaRelacionada.PROYECTOS),
            categoriasRelacionadas(TipoIndicador.AVANCE_PROMEDIO)
        )
        assertEquals(CategoriaRelacionada.entries, categoriasRelacionadas(TipoIndicador.IMPACTO_ESTIMADO))
        assertEquals(CategoriaRelacionada.entries, categoriasRelacionadas(null))
    }

    @Test
    fun rechazaIndicadorQueNoCorrespondeALaLineaCargada() {
        val indicador = Indicador(
            "L1-PROYECTOS_ACTIVOS", "L1", "Proyectos", "Descripción", "proyectos",
            TipoIndicador.PROYECTOS_ACTIVOS, emptyList()
        )
        val calculado = IndicadorCalculado(indicador, 2023, 1.0, VariacionIndicador.SinComparacion)
        assertNull(validarIndicadorRuta(indicador.id, listOf(calculado)))
        assertNotNull(validarIndicadorRuta("L2-PROYECTOS_ACTIVOS", listOf(calculado)))
    }
}
