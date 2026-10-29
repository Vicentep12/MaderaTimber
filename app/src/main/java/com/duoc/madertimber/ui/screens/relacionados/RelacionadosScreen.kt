package com.duoc.madertimber.ui.screens.relacionados

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.duoc.madertimber.data.model.CategoriaRelacionada
import com.duoc.madertimber.data.model.DatosRelacionados
import com.duoc.madertimber.data.model.TipoIndicador
import com.duoc.madertimber.data.model.categoriasRelacionadas

@Composable
fun RelacionadosScreen(
    datos: DatosRelacionados?,
    lineaNombre: String,
    anio: Int,
    tipo: TipoIndicador?,
    mensaje: String?,
    onVolverClick: () -> Unit,
    onFichaClick: (CategoriaRelacionada, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val categorias = categoriasRelacionadas(tipo)
    var seleccion by rememberSaveable(tipo?.name) {
        mutableStateOf(categorias.first().name)
    }
    val categoria = categorias.find { it.name == seleccion } ?: categorias.first()
    val elementos = when (categoria) {
        CategoriaRelacionada.PROYECTOS -> datos?.proyectos.orEmpty().map {
            ElementoRelacionado(it.id, it.titulo, "Proyecto activo en $anio")
        }
        CategoriaRelacionada.INVESTIGADORES ->
            datos?.investigadores.orEmpty().map {
                ElementoRelacionado(it.id, it.nombre, "Especialidad: ${it.especialidad}")
            }
        CategoriaRelacionada.PUBLICACIONES ->
            datos?.publicaciones.orEmpty().map {
                ElementoRelacionado(it.id, it.titulo, "${it.tipo} · ${it.anio}")
            }
    }

    Column(
        modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Contenidos relacionados", style = MaterialTheme.typography.headlineMedium)
        Text("$lineaNombre · $anio · Datos demostrativos")
        if (datos == null) {
            Text(mensaje ?: "No se pudieron cargar los contenidos de esta selección.")
        } else {
            if (categorias.size > 1) {
                Row(
                    modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    categorias.forEach { opcion ->
                        FilterChip(
                            selected = categoria == opcion,
                            onClick = { seleccion = opcion.name },
                            label = { Text(opcion.etiqueta()) }
                        )
                    }
                }
            } else {
                Text(categoria.etiqueta(), style = MaterialTheme.typography.titleMedium)
            }
            Text("${categoria.etiqueta()}: ${elementos.size}")
            if (elementos.isEmpty()) {
                Text("No hay ${categoria.etiqueta().lowercase()} para esta línea y año.")
            }
            elementos.forEach { elemento ->
                Card(
                    modifier = Modifier.fillMaxWidth().clickable(role = Role.Button) {
                        onFichaClick(categoria, elemento.id)
                    }
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(elemento.titulo, style = MaterialTheme.typography.titleMedium)
                        Text(elemento.detalle, style = MaterialTheme.typography.bodySmall)
                        Text("Ver ficha", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
        Button(onClick = onVolverClick, modifier = Modifier.fillMaxWidth()) {
            Text("Volver")
        }
    }
}

private data class ElementoRelacionado(val id: String, val titulo: String, val detalle: String)

@Composable
fun FichaRelacionadaScreen(
    datos: DatosRelacionados,
    categoria: CategoriaRelacionada,
    itemId: String,
    onVolverClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Ficha de ${categoria.etiqueta().lowercase()}", style = MaterialTheme.typography.headlineMedium)
        Text("${datos.linea.nombre} · ${datos.anio} · Datos demostrativos")
        when (categoria) {
            CategoriaRelacionada.PROYECTOS -> {
                val proyecto = datos.proyectos.first { it.id == itemId }
                Text(proyecto.titulo, style = MaterialTheme.typography.titleLarge)
                proyecto.seguimientos.find { it.anio == datos.anio }?.let {
                    Text("Estado: ${it.estado.name.lowercase()}")
                    Text("Avance: ${it.avancePorcentaje} %")
                }
            }
            CategoriaRelacionada.INVESTIGADORES -> {
                val investigador = datos.investigadores.first { it.id == itemId }
                Text(investigador.nombre, style = MaterialTheme.typography.titleLarge)
                Text("Especialidad: ${investigador.especialidad}")
            }
            CategoriaRelacionada.PUBLICACIONES -> {
                val publicacion = datos.publicaciones.first { it.id == itemId }
                Text(publicacion.titulo, style = MaterialTheme.typography.titleLarge)
                Text("Tipo: ${publicacion.tipo}")
                Text("Año: ${publicacion.anio}")
            }
        }
        Button(onClick = onVolverClick, modifier = Modifier.fillMaxWidth()) {
            Text("Volver a la lista")
        }
    }
}

private fun CategoriaRelacionada.etiqueta(): String = when (this) {
    CategoriaRelacionada.PROYECTOS -> "Proyectos"
    CategoriaRelacionada.INVESTIGADORES -> "Investigadores"
    CategoriaRelacionada.PUBLICACIONES -> "Publicaciones"
}
