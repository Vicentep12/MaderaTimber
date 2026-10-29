package com.duoc.madertimber.data.model

import org.junit.Assert.assertFalse
import org.junit.Assert.assertEquals
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

    @Test
    fun proyectoConLineaExistenteYListasVaciasNoSonReferenciasInvalidas() {
        val linea = linea("L1")
        val proyecto = proyecto("P1", "L1")

        assertFalse(tieneProyectosConLineaInexistente(listOf(proyecto), listOf(linea)))
        assertFalse(tieneProyectosConLineaInexistente(emptyList(), listOf(linea)))
        assertFalse(tieneProyectosConLineaInexistente(emptyList(), emptyList()))
    }

    @Test
    fun conjuntoValidoRespetaRelacionesYLimites() {
        val errores = validarIntegridadDatos(
            lineas = listOf(linea("L1"), linea("L2")),
            proyectos = listOf(
                proyecto("P1", "L1", listOf(seguimiento(2023, 0.0), seguimiento(2024, 100.0))),
                proyecto("P2", "L2", listOf(seguimiento(2024, 50.0)))
            ),
            investigadores = listOf(
                investigador("I1", listOf(
                    VinculacionInvestigador("L1", 2023),
                    VinculacionInvestigador("L1", 2024),
                    VinculacionInvestigador("L2", 2024)
                ))
            ),
            publicaciones = listOf(publicacion("U1", "L1")),
            indicadores = listOf(
                indicador("A1", "L1", TipoIndicador.PROYECTOS_ACTIVOS, listOf(ValorIndicador(2024, 1.0))),
                indicador("A2", "L1", TipoIndicador.PUBLICACIONES_GENERADAS),
                indicador("A3", "L2", TipoIndicador.PROYECTOS_ACTIVOS)
            )
        )

        assertTrue(errores.toString(), errores.isEmpty())
    }

    @Test
    fun conjuntoVacioNoTieneErroresDeIntegridad() {
        val errores = validarIntegridadDatos(
            lineas = emptyList(),
            proyectos = emptyList(),
            investigadores = emptyList(),
            publicaciones = emptyList(),
            indicadores = emptyList()
        )

        assertTrue(errores.isEmpty())
    }

    @Test
    fun idsDuplicadosSeDetectanPorCadaEntidad() {
        val errores = validarIntegridadDatos(
            lineas = listOf(linea("L1"), linea("L1")),
            proyectos = listOf(proyecto("P1", "L1"), proyecto("P1", "L1")),
            investigadores = listOf(investigador("I1"), investigador("I1")),
            publicaciones = listOf(publicacion("U1", "L1"), publicacion("U1", "L1")),
            indicadores = listOf(
                indicador("A1", "L1", TipoIndicador.PROYECTOS_ACTIVOS),
                indicador("A1", "L1", TipoIndicador.PUBLICACIONES_GENERADAS)
            )
        )

        assertEquals(
            setOf("Línea L1", "Proyecto P1", "Investigador I1", "Publicación U1", "Indicador A1"),
            errores.filter { it.tipo == TipoErrorIntegridad.ID_DUPLICADO }.map { it.detalle }.toSet()
        )
    }

    @Test
    fun referenciasInexistentesSeDetectanEnTodasLasEntidadesRelacionadas() {
        val errores = validarIntegridadDatos(
            lineas = listOf(linea("L1")),
            proyectos = listOf(proyecto("P1", "L2")),
            investigadores = listOf(investigador("I1", listOf(VinculacionInvestigador("L2", 2024)))),
            publicaciones = listOf(publicacion("U1", "L2")),
            indicadores = listOf(indicador("A1", "L2", TipoIndicador.PROYECTOS_ACTIVOS))
        )

        assertEquals(
            4,
            errores.count { it.tipo == TipoErrorIntegridad.LINEA_INEXISTENTE }
        )
    }

    @Test
    fun seguimientosRepetidosSoloSeDetectanDentroDelMismoProyecto() {
        val errores = validarIntegridadDatos(
            lineas = listOf(linea("L1")),
            proyectos = listOf(
                proyecto("P1", "L1", listOf(seguimiento(2024, 20.0), seguimiento(2024, 40.0))),
                proyecto("P2", "L1", listOf(seguimiento(2024, 60.0)))
            ),
            investigadores = emptyList(),
            publicaciones = emptyList(),
            indicadores = emptyList()
        )

        assertEquals(1, errores.count { it.tipo == TipoErrorIntegridad.SEGUIMIENTO_DUPLICADO })
    }

    @Test
    fun vinculacionesRepetidasSeDetectanPorLineaYAnio() {
        val errores = validarIntegridadDatos(
            lineas = listOf(linea("L1"), linea("L2")),
            proyectos = emptyList(),
            investigadores = listOf(
                investigador("I1", listOf(
                    VinculacionInvestigador("L1", 2023),
                    VinculacionInvestigador("L1", 2024),
                    VinculacionInvestigador("L2", 2024),
                    VinculacionInvestigador("L1", 2024)
                )),
                investigador("I2", listOf(VinculacionInvestigador("L1", 2024)))
            ),
            publicaciones = emptyList(),
            indicadores = emptyList()
        )

        assertEquals(1, errores.count { it.tipo == TipoErrorIntegridad.VINCULACION_DUPLICADA })
    }

    @Test
    fun avanceFueraDeCeroACienOnoFinitoEsInvalido() {
        val errores = validarIntegridadDatos(
            lineas = listOf(linea("L1")),
            proyectos = listOf(proyecto("P1", "L1", listOf(
                seguimiento(2021, -0.1),
                seguimiento(2022, 100.1),
                seguimiento(2023, Double.NaN),
                seguimiento(2024, Double.POSITIVE_INFINITY)
            ))),
            investigadores = emptyList(),
            publicaciones = emptyList(),
            indicadores = emptyList()
        )

        assertEquals(4, errores.count { it.tipo == TipoErrorIntegridad.AVANCE_FUERA_DE_RANGO })
    }

    @Test
    fun indicadorYValorAnualNoSePuedenRepetir() {
        val errores = validarIntegridadDatos(
            lineas = listOf(linea("L1")),
            proyectos = emptyList(),
            investigadores = emptyList(),
            publicaciones = emptyList(),
            indicadores = listOf(
                indicador("A1", "L1", TipoIndicador.PROYECTOS_ACTIVOS, listOf(
                    ValorIndicador(2024, 1.0), ValorIndicador(2024, 2.0)
                )),
                indicador("A2", "L1", TipoIndicador.PROYECTOS_ACTIVOS)
            )
        )

        assertEquals(1, errores.count { it.tipo == TipoErrorIntegridad.INDICADOR_DUPLICADO })
        assertEquals(1, errores.count { it.tipo == TipoErrorIntegridad.VALOR_ANUAL_DUPLICADO })
    }

    private fun linea(id: String) = LineaInvestigacion(id, "Línea $id", "Ejemplo", "#008000")

    private fun proyecto(
        id: String,
        lineaId: String,
        seguimientos: List<SeguimientoProyecto> = emptyList()
    ) = Proyecto(id, lineaId, "Proyecto $id", seguimientos)

    private fun seguimiento(anio: Int, avance: Double) =
        SeguimientoProyecto(anio, EstadoProyecto.ACTIVO, avance)

    private fun investigador(id: String, vinculaciones: List<VinculacionInvestigador> = emptyList()) =
        Investigador(id, "Investigador $id", "Especialidad", vinculaciones)

    private fun publicacion(id: String, lineaId: String) =
        Publicacion(id, lineaId, "Publicación $id", "Artículo", 2024)

    private fun indicador(
        id: String,
        lineaId: String,
        tipo: TipoIndicador,
        valores: List<ValorIndicador> = emptyList()
    ) = Indicador(id, lineaId, "Indicador $id", "Ejemplo", "unidad", tipo, valores)
}
