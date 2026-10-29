package com.duoc.madertimber.data.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class HistoricoIndicadorTest {
    @Test
    fun conservaHuecoYExcluyeValoresFuturos() {
        val historico = prepararHistorico(
            indicador(
                2024,
                ValorIndicador(2022, 4.0),
                ValorIndicador(2024, 8.0),
                ValorIndicador(2025, 10.0)
            ),
            listOf(2024, 2022)
        )

        assertEquals("Proyectos activos", historico.titulo)
        assertEquals("proyectos", historico.unidad)
        assertEquals(
            listOf(
                PuntoHistorico(2022, 4.0, false),
                PuntoHistorico(2023, null, false),
                PuntoHistorico(2024, 8.0, true)
            ),
            historico.puntos
        )
    }

    @Test
    fun unSoloValorPermaneceComoPuntoDelAnioSeleccionado() {
        val historico = prepararHistorico(
            indicador(2024, ValorIndicador(2024, 3.0)),
            listOf(2024)
        )

        assertEquals(listOf(PuntoHistorico(2024, 3.0, true)), historico.puntos)
    }

    @Test
    fun ausenciaTotalConservaLosAniosSinInventarCeros() {
        val historico = prepararHistorico(indicador(2024), listOf(2022, 2024))

        assertEquals(listOf(2022, 2023, 2024), historico.puntos.map { it.anio })
        assertEquals(3, historico.puntos.count { it.valor == null })
        assertFalse(historico.puntos.any { it.valor == 0.0 })
    }

    private fun indicador(anio: Int, vararg valores: ValorIndicador): IndicadorCalculado =
        IndicadorCalculado(
            indicador = Indicador(
                id = "L1-PROYECTOS_ACTIVOS",
                lineaId = "L1",
                nombre = "Proyectos activos",
                descripcion = "Proyectos activos al cierre del año",
                unidad = "proyectos",
                tipo = TipoIndicador.PROYECTOS_ACTIVOS,
                valores = valores.toList()
            ),
            anioSeleccionado = anio,
            valorSeleccionado = valores.find { it.anio == anio }?.valor,
            variacion = VariacionIndicador.SinComparacion
        )
}
