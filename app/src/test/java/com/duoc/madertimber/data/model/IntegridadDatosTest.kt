package com.duoc.madertimber.data.model

import org.junit.Assert.assertFalse
import org.junit.Test
import org.junit.Assert.assertTrue

class IntegridadDatosTest {

    @Test
    fun detectaIdsRepetidos() {
        val primera = LineaInvestigacion(
            id = "L1",
            nombre = "Línea A",
            descripcion = "Ejemplo",
            color = "#008000"
        )

        val segunda = LineaInvestigacion(
            id = "L1",
            nombre = "Línea B",
            descripcion = "Otro ejemplo",
            color = "#0000FF"
        )

        val lineas = listOf(primera, segunda)
        assertTrue(tieneIdsDuplicados(lineas))
    }

    @Test
    fun idsDistintosNoSonDuplicados() {

        val primera = LineaInvestigacion(
            id = "L1",
            nombre = "Línea A",
            descripcion = "Ejemplo",
            color = "#008000"
        )

        val segunda = LineaInvestigacion(
            id = "L2",
            nombre = "Línea B",
            descripcion = "Otro ejemplo",
            color = "#0000FF"
        )

        val lineas = listOf(primera, segunda)
        assertFalse(tieneIdsDuplicados(lineas))
    }

    @Test
    fun listaVaciaNoTieneIdsDuplicados() {
        val lineas = emptyList<LineaInvestigacion>()

        assertFalse(tieneIdsDuplicados(lineas))
    }

    @Test
    fun detectaProyectoConLineaInexistente() {
        val linea = LineaInvestigacion(
            id = "L1",
            nombre = "Línea A",
            descripcion = "Ejemplo",
            color = "#008000"
        )
        val proyecto = Proyecto(
            id = "P1",
            lineaId = "L2",
            titulo = "Proyecto de ejemplo",
            seguimientos = emptyList()
        )

        assertTrue(
            tieneProyectosConLineaInexistente(
                proyectos = listOf(proyecto),
                lineas = listOf(linea)
            )
        )
    }
}