package com.duoc.madertimber.data.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class CalculosIndicadoresTest {
    @Test
    fun calculaLasCincoMetricasDesdeEntidadesDelAnioYLinea() {
        val datos = datos(
            lineas = listOf(linea("L1"), linea("L2")),
            cobertura = mapOf("L1" to setOf(2023, 2024), "L2" to setOf(2024)),
            proyectos = listOf(
                proyecto("P1", "L1", seguimiento(2024, EstadoProyecto.ACTIVO, 40.0)),
                proyecto("P2", "L1", seguimiento(2024, EstadoProyecto.ACTIVO, 60.0)),
                proyecto("P3", "L1", seguimiento(2024, EstadoProyecto.FINALIZADO, 100.0)),
                proyecto("P4", "L2", seguimiento(2024, EstadoProyecto.ACTIVO, 90.0))
            ),
            publicaciones = listOf(
                publicacion("U1", "L1", 2024),
                publicacion("U2", "L1", 2023),
                publicacion("U3", "L2", 2024)
            ),
            investigadores = listOf(
                investigador("I1", VinculacionInvestigador("L1", 2024), VinculacionInvestigador("L2", 2024)),
                investigador("I2", VinculacionInvestigador("L1", 2024)),
                investigador("I3", VinculacionInvestigador("L1", 2023))
            )
        )

        val indicadores = calcularIndicadores(datos, "L1", 2024).associateBy { it.indicador.tipo }
        assertEquals(5, indicadores.size)
        assertEquals(2.0, indicadores.getValue(TipoIndicador.PROYECTOS_ACTIVOS).valorSeleccionado!!, 0.0)
        assertEquals(1.0, indicadores.getValue(TipoIndicador.PUBLICACIONES_GENERADAS).valorSeleccionado!!, 0.0)
        assertEquals(2.0, indicadores.getValue(TipoIndicador.INVESTIGADORES_ASOCIADOS).valorSeleccionado!!, 0.0)
        assertEquals(50.0, indicadores.getValue(TipoIndicador.AVANCE_PROMEDIO).valorSeleccionado!!, 0.0)
        val impactoEsperado = 100.0 * (0.4 * 2 / 10 + 0.4 * 1 / 20 + 0.2 * 2 / 15)
        assertEquals(impactoEsperado, indicadores.getValue(TipoIndicador.IMPACTO_ESTIMADO).valorSeleccionado!!, 1e-10)
        assertTrue(
            validarIntegridadDatos(
                datos.lineas, datos.proyectos, datos.investigadores, datos.publicaciones,
                indicadores.values.map { it.indicador }
            ).isEmpty()
        )
    }

    @Test
    fun conjuntoCubiertoVacioDaConteosCeroSinInventarAvance() {
        val indicadores = calcularIndicadores(
            datos(cobertura = mapOf("L1" to setOf(2023, 2024))), "L1", 2024
        ).associateBy { it.indicador.tipo }

        for (tipo in TipoIndicador.entries - TipoIndicador.AVANCE_PROMEDIO) {
            assertEquals(0.0, indicadores.getValue(tipo).valorSeleccionado!!, 0.0)
            assertEquals(VariacionIndicador.BaseAnteriorCero, indicadores.getValue(tipo).variacion)
        }
        val avance = indicadores.getValue(TipoIndicador.AVANCE_PROMEDIO)
        assertNull(avance.valorSeleccionado)
        assertTrue(avance.indicador.valores.isEmpty())
        assertEquals(VariacionIndicador.SinComparacion, avance.variacion)
    }

    @Test
    fun historicoConHuecoNoUsaUnAnioAnteriorDistintoDelCalendario() {
        val datos = datos(
            cobertura = mapOf("L1" to setOf(2022, 2024)),
            proyectos = listOf(proyecto("P1", "L1",
                seguimiento(2022, EstadoProyecto.ACTIVO, 20.0),
                seguimiento(2024, EstadoProyecto.ACTIVO, 60.0)
            ))
        )

        val proyectos = calcularIndicadores(datos, "L1", 2024)
            .single { it.indicador.tipo == TipoIndicador.PROYECTOS_ACTIVOS }
        assertEquals(1.0, proyectos.valorSeleccionado!!, 0.0)
        assertEquals(listOf(2022, 2024), proyectos.historicoHastaSeleccionado.map { it.anio })
        assertEquals(VariacionIndicador.SinComparacion, proyectos.variacion)

        val avance = calcularIndicadores(datos, "L1", 2022)
            .single { it.indicador.tipo == TipoIndicador.AVANCE_PROMEDIO }
        assertEquals(20.0, avance.valorSeleccionado!!, 0.0)
        assertEquals(listOf(2022), avance.historicoHastaSeleccionado.map { it.anio })
        assertEquals(listOf(2022, 2024), avance.indicador.valores.map { it.anio })
    }

    @Test
    fun avanceOmiteAniosSinProyectosActivosAunqueHayaOtrosProyectos() {
        val datos = datos(
            cobertura = mapOf("L1" to setOf(2023, 2024)),
            proyectos = listOf(proyecto("P1", "L1",
                seguimiento(2023, EstadoProyecto.ACTIVO, 0.0),
                seguimiento(2024, EstadoProyecto.SUSPENDIDO, 80.0)
            ))
        )
        val indicadores = calcularIndicadores(datos, "L1", 2024).associateBy { it.indicador.tipo }

        assertEquals(0.0, indicadores.getValue(TipoIndicador.PROYECTOS_ACTIVOS).valorSeleccionado!!, 0.0)
        assertNull(indicadores.getValue(TipoIndicador.AVANCE_PROMEDIO).valorSeleccionado)
        assertEquals(listOf(2023), indicadores.getValue(TipoIndicador.AVANCE_PROMEDIO).indicador.valores.map { it.anio })
    }

    @Test
    fun variacionesRespetanSignoBaseCeroYUnidadDeAvance() {
        assertEquals(
            VariacionIndicador.Porcentaje(50.0),
            calcularVariacion(TipoIndicador.PUBLICACIONES_GENERADAS, 3.0, 2.0)
        )
        assertEquals(
            VariacionIndicador.Porcentaje(-50.0),
            calcularVariacion(TipoIndicador.PROYECTOS_ACTIVOS, 2.0, 4.0)
        )
        assertEquals(
            VariacionIndicador.BaseAnteriorCero,
            calcularVariacion(TipoIndicador.IMPACTO_ESTIMADO, 0.0, 0.0)
        )
        assertEquals(
            VariacionIndicador.BaseAnteriorCero,
            calcularVariacion(TipoIndicador.IMPACTO_ESTIMADO, 4.0, 0.0)
        )
        assertEquals(
            VariacionIndicador.PuntosPorcentuales(10.0),
            calcularVariacion(TipoIndicador.AVANCE_PROMEDIO, 10.0, 0.0)
        )
        assertEquals(
            VariacionIndicador.SinComparacion,
            calcularVariacion(TipoIndicador.AVANCE_PROMEDIO, null, 0.0)
        )
        assertEquals(
            VariacionIndicador.SinComparacion,
            calcularVariacion(TipoIndicador.IMPACTO_ESTIMADO, 0.0, null)
        )
    }

    @Test
    fun impactoSeLimitaACienCuandoSeSuperanLasMetas() {
        val datos = datos(
            proyectos = (1..12).map { proyecto("P$it", "L1", seguimiento(2024, EstadoProyecto.ACTIVO, 20.0)) },
            publicaciones = (1..25).map { publicacion("U$it", "L1", 2024) },
            investigadores = (1..18).map { investigador("I$it", VinculacionInvestigador("L1", 2024)) }
        )

        val impacto = calcularIndicadores(datos, "L1", 2024)
            .single { it.indicador.tipo == TipoIndicador.IMPACTO_ESTIMADO }
        assertEquals(100.0, impacto.valorSeleccionado!!, 1e-10)
    }

    private fun datos(
        lineas: List<LineaInvestigacion> = listOf(linea("L1")),
        cobertura: Map<String, Set<Int>> = mapOf("L1" to setOf(2024)),
        proyectos: List<Proyecto> = emptyList(),
        investigadores: List<Investigador> = emptyList(),
        publicaciones: List<Publicacion> = emptyList()
    ) = DatosPanel(lineas, cobertura, proyectos, investigadores, publicaciones)

    private fun linea(id: String) = LineaInvestigacion(id, "Línea $id", "Ejemplo", "#008000")

    private fun proyecto(id: String, lineaId: String, vararg seguimientos: SeguimientoProyecto) =
        Proyecto(id, lineaId, "Proyecto $id", seguimientos.toList())

    private fun seguimiento(anio: Int, estado: EstadoProyecto, avance: Double) =
        SeguimientoProyecto(anio, estado, avance)

    private fun publicacion(id: String, lineaId: String, anio: Int) =
        Publicacion(id, lineaId, "Publicación $id", "Artículo", anio)

    private fun investigador(id: String, vararg vinculaciones: VinculacionInvestigador) =
        Investigador(id, "Investigador $id", "Especialidad", vinculaciones.toList())
}
