package com.duoc.madertimber.data.repository

import com.duoc.madertimber.data.model.TipoIndicador
import com.duoc.madertimber.data.model.VariacionIndicador
import java.io.File
import java.io.StringReader
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class IndicadoresRepositoryTest {
    @Test
    fun muestraDe2024ReproduceConteosEImpactosDeLasTresLineas() {
        val repositorio = repositorioDelAsset()
        assertTrue(repositorio.cargar() is EstadoCargaPanel.Listo)
        val esperados = mapOf(
            "L1" to listOf(8.0, 15.0, 12.0, 78.0),
            "L2" to listOf(5.0, 11.0, 9.0, 54.0),
            "L3" to listOf(10.0, 18.0, 15.0, 96.0)
        )

        esperados.forEach { (lineaId, valores) ->
            val consulta = repositorio.consultarIndicadores(lineaId, 2024)
            assertTrue(consulta.toString(), consulta is ResultadoIndicadoresPanel.Disponible)
            val disponibles = consulta as ResultadoIndicadoresPanel.Disponible
            assertEquals(listOf(2022, 2023, 2024), disponibles.aniosCubiertos)
            val indicadores = disponibles.indicadores.associateBy { it.indicador.tipo }
            assertEquals(5, indicadores.size)
            assertEquals(valores[0], indicadores.getValue(TipoIndicador.PROYECTOS_ACTIVOS).valorSeleccionado!!, 0.0)
            assertEquals(valores[1], indicadores.getValue(TipoIndicador.PUBLICACIONES_GENERADAS).valorSeleccionado!!, 0.0)
            assertEquals(valores[2], indicadores.getValue(TipoIndicador.INVESTIGADORES_ASOCIADOS).valorSeleccionado!!, 0.0)
            assertEquals(valores[3], indicadores.getValue(TipoIndicador.IMPACTO_ESTIMADO).valorSeleccionado!!, 1e-9)

            val relacionados = repositorio.consultarRelacionados(lineaId, 2024)
            assertTrue(relacionados is ResultadoConsultaPanel.Disponible)
            val datos = (relacionados as ResultadoConsultaPanel.Disponible).datos
            assertEquals(valores[0].toInt(), datos.proyectos.size)
            assertEquals(valores[1].toInt(), datos.publicaciones.size)
            assertEquals(valores[2].toInt(), datos.investigadores.size)
        }
    }

    @Test
    fun seleccionDe2023NoSeReemplazaPorElValorMasReciente() {
        val repositorio = repositorioDelAsset()
        repositorio.cargar()

        val resultado = repositorio.consultarIndicadores("L1", 2023)
        assertTrue(resultado is ResultadoIndicadoresPanel.Disponible)
        val proyectos = (resultado as ResultadoIndicadoresPanel.Disponible).indicadores
            .single { it.indicador.tipo == TipoIndicador.PROYECTOS_ACTIVOS }
        assertEquals(6.0, proyectos.valorSeleccionado!!, 0.0)
        assertEquals(8.0, proyectos.indicador.valores.single { it.anio == 2024 }.valor, 0.0)
        assertEquals(listOf(2022, 2023), proyectos.historicoHastaSeleccionado.map { it.anio })
        assertEquals(VariacionIndicador.Porcentaje(50.0), proyectos.variacion)
    }

    @Test
    fun relacionadosPorTipoExcluyenProyectosNoActivos() {
        val repositorio = LocalPanelRepository { StringReader(datosConProyectoFinalizado()) }
        assertTrue(repositorio.cargar() is EstadoCargaPanel.Listo)
        val sinFiltro = repositorio.consultar("L1", 2024) as ResultadoConsultaPanel.Disponible
        assertEquals(2, sinFiltro.datos.proyectos.size)

        val proyectos = repositorio.consultarRelacionados("L1", 2024, TipoIndicador.PROYECTOS_ACTIVOS)
            as ResultadoConsultaPanel.Disponible
        assertEquals(listOf("P1"), proyectos.datos.proyectos.map { it.id })
        assertTrue(proyectos.datos.investigadores.isEmpty())
        assertTrue(proyectos.datos.publicaciones.isEmpty())

        val avance = repositorio.consultarRelacionados("L1", 2024, TipoIndicador.AVANCE_PROMEDIO)
            as ResultadoConsultaPanel.Disponible
        assertEquals(listOf("P1"), avance.datos.proyectos.map { it.id })

        val publicaciones = repositorio.consultarRelacionados("L1", 2024, TipoIndicador.PUBLICACIONES_GENERADAS)
            as ResultadoConsultaPanel.Disponible
        assertEquals(1, publicaciones.datos.publicaciones.size)
        assertTrue(publicaciones.datos.proyectos.isEmpty())

        val investigadores = repositorio.consultarRelacionados("L1", 2024, TipoIndicador.INVESTIGADORES_ASOCIADOS)
            as ResultadoConsultaPanel.Disponible
        assertEquals(1, investigadores.datos.investigadores.size)
        assertTrue(investigadores.datos.proyectos.isEmpty())

        val impacto = repositorio.consultarRelacionados("L1", 2024, TipoIndicador.IMPACTO_ESTIMADO)
            as ResultadoConsultaPanel.Disponible
        assertEquals(1, impacto.datos.proyectos.size)
        assertEquals(1, impacto.datos.publicaciones.size)
        assertEquals(1, impacto.datos.investigadores.size)

        val indicadores = repositorio.consultarIndicadores("L1", 2024)
            as ResultadoIndicadoresPanel.Disponible
        assertEquals(1.0, indicadores.indicadores.single {
            it.indicador.tipo == TipoIndicador.PROYECTOS_ACTIVOS
        }.valorSeleccionado!!, 0.0)
        assertEquals(40.0, indicadores.indicadores.single {
            it.indicador.tipo == TipoIndicador.AVANCE_PROMEDIO
        }.valorSeleccionado!!, 0.0)
    }

    @Test
    fun anioCubiertoSinEntidadesEsCeroPeroSinCoberturaNoLoEs() {
        val repositorio = LocalPanelRepository { StringReader(datosConProyectoFinalizado()) }
        assertEquals(ResultadoIndicadoresPanel.Cargando, repositorio.consultarIndicadores("L1", 2024))
        repositorio.cargar()

        val vacio = repositorio.consultarRelacionados("L1", 2025)
        assertTrue(vacio is ResultadoConsultaPanel.Vacio)
        val indicadores = repositorio.consultarIndicadores("L1", 2025)
            as ResultadoIndicadoresPanel.Disponible
        assertEquals(0.0, indicadores.indicadores.single {
            it.indicador.tipo == TipoIndicador.IMPACTO_ESTIMADO
        }.valorSeleccionado!!, 0.0)
        assertNull(indicadores.indicadores.single {
            it.indicador.tipo == TipoIndicador.AVANCE_PROMEDIO
        }.valorSeleccionado)
        assertEquals(
            ResultadoIndicadoresPanel.SinCobertura("L1", 2023),
            repositorio.consultarIndicadores("L1", 2023)
        )
    }

    @Test
    fun errorDeCargaNoSeTransformaEnIndicadoresCero() {
        val repositorio = LocalPanelRepository { StringReader("{json inválido}") }
        assertTrue(repositorio.cargar() is EstadoCargaPanel.Error)
        assertTrue(repositorio.consultarIndicadores("L1", 2024) is ResultadoIndicadoresPanel.Error)
    }

    private fun repositorioDelAsset() = LocalPanelRepository {
        File("src/main/assets/${LocalPanelRepository.ARCHIVO_DATOS}").bufferedReader()
    }

    private fun datosConProyectoFinalizado() = """{
        "lineas":[{"id":"L1","nombre":"Línea","descripcion":"Ejemplo","color":"#008000"}],
        "cobertura":[{"lineaId":"L1","anios":[2024,2025]}],
        "proyectos":[
            {"id":"P1","lineaId":"L1","titulo":"Activo",
                "seguimientos":[{"anio":2024,"estado":"ACTIVO","avancePorcentaje":40.0}]},
            {"id":"P2","lineaId":"L1","titulo":"Finalizado",
                "seguimientos":[{"anio":2024,"estado":"FINALIZADO","avancePorcentaje":100.0}]}
        ],
        "investigadores":[{"id":"I1","nombre":"Investigador","especialidad":"Madera",
            "vinculaciones":[{"lineaId":"L1","anio":2024}]}],
        "publicaciones":[{"id":"U1","lineaId":"L1","titulo":"Publicación","tipo":"Artículo","anio":2024}]
    }"""
}
