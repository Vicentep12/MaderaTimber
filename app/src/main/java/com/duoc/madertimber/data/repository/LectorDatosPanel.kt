package com.duoc.madertimber.data.repository

import com.duoc.madertimber.data.model.DatosPanel
import com.duoc.madertimber.data.model.EstadoProyecto
import com.duoc.madertimber.data.model.Investigador
import com.duoc.madertimber.data.model.LineaInvestigacion
import com.duoc.madertimber.data.model.Proyecto
import com.duoc.madertimber.data.model.Publicacion
import com.duoc.madertimber.data.model.SeguimientoProyecto
import com.duoc.madertimber.data.model.VinculacionInvestigador
import com.duoc.madertimber.data.model.validarIntegridadDatos
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.Strictness
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import java.io.Reader

object LectorDatosPanel {
    fun leer(entrada: Reader): DatosPanel {
        val lectorJson = JsonReader(entrada).apply { strictness = Strictness.STRICT }
        val raiz = JsonParser.parseReader(lectorJson).comoObjeto("raíz")
        require(lectorJson.peek() == JsonToken.END_DOCUMENT) {
            "El archivo contiene datos adicionales después del objeto principal"
        }
        val lineas = raiz.arreglo("lineas").map { elemento ->
            val dato = elemento.comoObjeto("línea")
            LineaInvestigacion(
                id = dato.texto("id"),
                nombre = dato.texto("nombre"),
                descripcion = dato.texto("descripcion"),
                color = dato.texto("color")
            )
        }
        val cobertura = mutableMapOf<String, Set<Int>>()
        raiz.arreglo("cobertura").forEach { elemento ->
            val dato = elemento.comoObjeto("cobertura")
            val lineaId = dato.texto("lineaId")
            val anios = dato.arreglo("anios").map { it.entero("año de cobertura") }
            require(lineaId !in cobertura) { "Cobertura repetida para la línea $lineaId" }
            require(anios.size == anios.toSet().size) {
                "Años repetidos en la cobertura de $lineaId"
            }
            cobertura[lineaId] = anios.toSet()
        }
        val proyectos = raiz.arreglo("proyectos").map { elemento ->
            val dato = elemento.comoObjeto("proyecto")
            Proyecto(
                id = dato.texto("id"),
                lineaId = dato.texto("lineaId"),
                titulo = dato.texto("titulo"),
                seguimientos = dato.arreglo("seguimientos").map { valor ->
                    val seguimiento = valor.comoObjeto("seguimiento")
                    val estado = seguimiento.texto("estado")
                    SeguimientoProyecto(
                        anio = seguimiento.entero("anio"),
                        estado = EstadoProyecto.entries.find { it.name == estado }
                            ?: throw IllegalArgumentException("Estado de proyecto inválido: $estado"),
                        avancePorcentaje = seguimiento.decimal("avancePorcentaje")
                    )
                }
            )
        }
        val investigadores = raiz.arreglo("investigadores").map { elemento ->
            val dato = elemento.comoObjeto("investigador")
            Investigador(
                id = dato.texto("id"),
                nombre = dato.texto("nombre"),
                especialidad = dato.texto("especialidad"),
                vinculaciones = dato.arreglo("vinculaciones").map { valor ->
                    val vinculacion = valor.comoObjeto("vinculación")
                    VinculacionInvestigador(
                        lineaId = vinculacion.texto("lineaId"),
                        anio = vinculacion.entero("anio")
                    )
                }
            )
        }
        val publicaciones = raiz.arreglo("publicaciones").map { elemento ->
            val dato = elemento.comoObjeto("publicación")
            Publicacion(
                id = dato.texto("id"),
                lineaId = dato.texto("lineaId"),
                titulo = dato.texto("titulo"),
                tipo = dato.texto("tipo"),
                anio = dato.entero("anio")
            )
        }

        val errores = validarIntegridadDatos(
            lineas = lineas,
            proyectos = proyectos,
            investigadores = investigadores,
            publicaciones = publicaciones,
            indicadores = emptyList()
        )
        require(errores.isEmpty()) {
            "Datos inconsistentes: ${errores.joinToString { "${it.tipo}: ${it.detalle}" }}"
        }

        val idsLineas = lineas.mapTo(mutableSetOf()) { it.id }
        require(cobertura.keys.all { it in idsLineas }) {
            "La cobertura contiene una línea inexistente"
        }
        proyectos.forEach { proyecto ->
            proyecto.seguimientos.forEach { seguimiento ->
                exigirCobertura(cobertura, proyecto.lineaId, seguimiento.anio)
            }
        }
        investigadores.forEach { investigador ->
            investigador.vinculaciones.forEach { vinculacion ->
                exigirCobertura(cobertura, vinculacion.lineaId, vinculacion.anio)
            }
        }
        publicaciones.forEach { publicacion ->
            exigirCobertura(cobertura, publicacion.lineaId, publicacion.anio)
        }

        return DatosPanel(lineas, cobertura, proyectos, investigadores, publicaciones)
    }

    private fun exigirCobertura(cobertura: Map<String, Set<Int>>, lineaId: String, anio: Int) {
        require(anio in cobertura[lineaId].orEmpty()) {
            "La línea $lineaId no declara cobertura para $anio"
        }
    }

    private fun JsonElement.comoObjeto(nombre: String): JsonObject {
        require(isJsonObject) { "$nombre debe ser un objeto" }
        return asJsonObject
    }

    private fun JsonObject.arreglo(nombre: String): JsonArray {
        val valor = get(nombre)
        require(valor != null && valor.isJsonArray) { "$nombre debe ser una lista" }
        return valor.asJsonArray
    }

    private fun JsonObject.texto(nombre: String): String {
        val valor = get(nombre)
        require(valor != null && valor.isJsonPrimitive && valor.asJsonPrimitive.isString) {
            "$nombre debe ser texto"
        }
        return valor.asString
    }

    private fun JsonObject.entero(nombre: String): Int {
        val valor = get(nombre)
        require(valor != null) { "Falta $nombre" }
        return valor.entero(nombre)
    }

    private fun JsonElement.entero(nombre: String): Int {
        require(isJsonPrimitive && asJsonPrimitive.isNumber && asString.matches(Regex("-?\\d+"))) {
            "$nombre debe ser un entero"
        }
        return asString.toIntOrNull() ?: throw IllegalArgumentException("$nombre fuera de rango")
    }

    private fun JsonObject.decimal(nombre: String): Double {
        val valor = get(nombre)
        require(valor != null && valor.isJsonPrimitive && valor.asJsonPrimitive.isNumber) {
            "$nombre debe ser un número"
        }
        return valor.asDouble
    }
}
