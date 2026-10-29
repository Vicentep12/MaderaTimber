package com.duoc.madertimber.data.repository

import com.duoc.madertimber.data.model.EstadoProyecto
import java.io.File
import java.io.IOException
import java.io.StringReader
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class LocalPanelRepositoryTest {
    @Test
    fun archivoDeDemostracionTieneCoberturaYConteosDeLaMuestra() {
        val archivo = File("src/main/assets/${LocalPanelRepository.ARCHIVO_DATOS}")
        val repositorio = LocalPanelRepository { archivo.bufferedReader() }

        val carga = repositorio.cargar()
        assertTrue(carga.toString(), carga is EstadoCargaPanel.Listo)
        val datos = (carga as EstadoCargaPanel.Listo).datos
        assertEquals(3, datos.lineas.size)
        datos.lineas.forEach { linea ->
            assertEquals(setOf(2022, 2023, 2024), datos.coberturaPorLinea[linea.id])
            assertTrue(repositorio.consultar(linea.id, 2022) is ResultadoConsultaPanel.Disponible)
            assertTrue(repositorio.consultar(linea.id, 2023) is ResultadoConsultaPanel.Disponible)
        }

        val esperados = mapOf(
            "L1" to Triple(8, 15, 12),
            "L2" to Triple(5, 11, 9),
            "L3" to Triple(10, 18, 15)
        )
        esperados.forEach { (lineaId, conteos) ->
            val resultado = repositorio.consultar(lineaId, 2024)
            assertTrue(resultado.toString(), resultado is ResultadoConsultaPanel.Disponible)
            val relacionados = (resultado as ResultadoConsultaPanel.Disponible).datos
            val activos = relacionados.proyectos.count { proyecto ->
                proyecto.seguimientos.single { it.anio == 2024 }.estado == EstadoProyecto.ACTIVO
            }
            assertEquals(conteos.first, activos)
            assertEquals(conteos.second, relacionados.publicaciones.size)
            assertEquals(conteos.third, relacionados.investigadores.size)
            assertEquals(conteos.third, relacionados.investigadores.map { it.id }.distinct().size)
        }
    }

    @Test
    fun consultaDistingueCargaDatosVacioYCoberturaAusente() {
        val repositorio = LocalPanelRepository { StringReader(datosMinimos()) }

        assertEquals(ResultadoConsultaPanel.Cargando, repositorio.consultar("L1", 2024))
        assertTrue(repositorio.cargar() is EstadoCargaPanel.Listo)
        val disponible = repositorio.consultar("L1", 2024)
        assertTrue(disponible is ResultadoConsultaPanel.Disponible)
        assertEquals(1, (disponible as ResultadoConsultaPanel.Disponible).datos.proyectos.size)

        val vacio = repositorio.consultar("L1", 2025)
        assertTrue(vacio is ResultadoConsultaPanel.Vacio)
        assertTrue((vacio as ResultadoConsultaPanel.Vacio).datos.estaVacio)
        assertEquals(
            ResultadoConsultaPanel.SinCobertura("L1", 2023),
            repositorio.consultar("L1", 2023)
        )
    }

    @Test
    fun archivoAusenteYJsonInvalidoSonErroresDeCarga() {
        val ausente = LocalPanelRepository { throw IOException("Archivo no encontrado") }
        val invalido = LocalPanelRepository { StringReader("{esto no es JSON}") }

        assertTrue(ausente.cargar() is EstadoCargaPanel.Error)
        assertTrue(ausente.consultar("L1", 2024) is ResultadoConsultaPanel.Error)
        assertTrue(invalido.cargar() is EstadoCargaPanel.Error)
        assertTrue(invalido.consultar("L1", 2024) is ResultadoConsultaPanel.Error)
    }

    @Test
    fun conjuntoVacioSeDistingueDeArchivoInvalido() {
        val repositorio = LocalPanelRepository {
            StringReader("""{"lineas":[],"cobertura":[],"proyectos":[],"investigadores":[],"publicaciones":[]}""")
        }

        assertEquals(EstadoCargaPanel.Vacio, repositorio.cargar())
    }

    @Test
    fun lectorRechazaReferenciasYAnosFueraDeLaCoberturaDeclarada() {
        val referenciaInvalida = LocalPanelRepository {
            StringReader(datosMinimos(proyectoLineaId = "L9"))
        }
        val anioNoCubierto = LocalPanelRepository {
            StringReader(datosMinimos(proyectoAnio = 2023))
        }

        assertTrue(referenciaInvalida.cargar() is EstadoCargaPanel.Error)
        assertTrue(anioNoCubierto.cargar() is EstadoCargaPanel.Error)
    }

    @Test
    fun lectorRechazaCoberturaRepetidaYCamposObligatoriosAusentes() {
        val duplicada = LocalPanelRepository {
            StringReader(datosMinimos(cobertura = """[
                {"lineaId":"L1","anios":[2024,2025]},
                {"lineaId":"L1","anios":[2024]}
            ]"""))
        }
        val sinTitulo = LocalPanelRepository {
            StringReader(datosMinimos().replace("\"titulo\":\"Proyecto\",", ""))
        }

        assertTrue(duplicada.cargar() is EstadoCargaPanel.Error)
        assertTrue(sinTitulo.cargar() is EstadoCargaPanel.Error)
    }

    @Test
    fun lectorRechazaAniosDeCoberturaRepetidosYAvanceInvalido() {
        val aniosRepetidos = LocalPanelRepository {
            StringReader(datosMinimos(cobertura = """[{"lineaId":"L1","anios":[2024,2024]}]"""))
        }
        val avanceInvalido = LocalPanelRepository {
            StringReader(datosMinimos().replace("50.0", "101.0"))
        }

        assertTrue(aniosRepetidos.cargar() is EstadoCargaPanel.Error)
        assertTrue(avanceInvalido.cargar() is EstadoCargaPanel.Error)
    }

    @Test
    fun lectorRechazaJsonNoEstrictoYContenidoAdicional() {
        val nombreSinComillas = LocalPanelRepository {
            StringReader(datosMinimos().replace("\"lineas\":", "lineas:"))
        }
        val contenidoAdicional = LocalPanelRepository {
            StringReader(datosMinimos() + " true")
        }

        assertTrue(nombreSinComillas.cargar() is EstadoCargaPanel.Error)
        assertTrue(contenidoAdicional.cargar() is EstadoCargaPanel.Error)
    }

    private fun datosMinimos(
        proyectoLineaId: String = "L1",
        proyectoAnio: Int = 2024,
        cobertura: String = """[{"lineaId":"L1","anios":[2024,2025]}]"""
    ) = """{
        "lineas":[{"id":"L1","nombre":"Línea","descripcion":"Ejemplo","color":"#008000"}],
        "cobertura":$cobertura,
        "proyectos":[{"id":"P1","lineaId":"$proyectoLineaId","titulo":"Proyecto",
            "seguimientos":[{"anio":$proyectoAnio,"estado":"ACTIVO","avancePorcentaje":50.0}]}],
        "investigadores":[],
        "publicaciones":[]
    }"""
}
