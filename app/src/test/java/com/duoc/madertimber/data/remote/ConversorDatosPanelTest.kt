package com.duoc.madertimber.data.remote

import com.duoc.madertimber.data.model.EstadoProyecto
import org.junit.Assert.assertEquals
import org.junit.Test

// Prueba de la conversión de las respuestas de Xano a los modelos del panel
class ConversorDatosPanelTest {

    @Test
    fun convierteLasCuatroListasEnDatosDelPanel() {
        val datos = ConversorDatosPanel.convertir(
            lineasApi = listOf(
                LineaDto("L1", "Construcción sustentable", "Descripción", "#2E7D32", listOf(2023, 2024))
            ),
            proyectosApi = listOf(
                ProyectoDto("P-L1-01", "L1", "Proyecto 1", listOf(SeguimientoDto(2024, "ACTIVO", 50.0)))
            ),
            investigadoresApi = listOf(
                InvestigadorDto("I-L1-01", "Investigadora 1", "Madera", listOf(VinculacionDto("L1", 2023)))
            ),
            publicacionesApi = listOf(
                PublicacionDto("U-L1-01", "L1", "Publicación 1", "Artículo", 2024)
            )
        )

        assertEquals("L1", datos.lineas.single().id)
        assertEquals(setOf(2023, 2024), datos.coberturaPorLinea["L1"])
        assertEquals(EstadoProyecto.ACTIVO, datos.proyectos.single().seguimientos.single().estado)
        assertEquals("L1", datos.investigadores.single().vinculaciones.single().lineaId)
        assertEquals(2024, datos.publicaciones.single().anio)
    }
}
