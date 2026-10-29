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
import androidx.compose.ui.unit.dp
import com.duoc.madertimber.data.model.CategoriaRelacionada
import com.duoc.madertimber.data.model.DatosRelacionados
import com.duoc.madertimber.data.model.TipoIndicador
import com.duoc.madertimber.data.model.categoriasRelacionadas

@Composable
fun RelacionadosScreen(
    datos: DatosRelacionados?,
    tipo: TipoIndicador?,
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
        CategoriaRelacionada.PROYECTOS -> datos?.proyectos.orEmpty().map { it.id to it.titulo }
        CategoriaRelacionada.INVESTIGADORES ->
            datos?.investigadores.orEmpty().map { it.id to it.nombre }
        CategoriaRelacionada.PUBLICACIONES ->
            datos?.publicaciones.orEmpty().map { it.id to it.titulo }
    }

    Column(
        modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Contenidos relacionados", style = MaterialTheme.typography.headlineMedium)
        if (datos != null) {
            Text("${datos.linea.nombre} · ${datos.anio} · Datos demostrativos")
        }
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
        Text("${categoria.etiqueta()}: ${elementos.size}")
        if (elementos.isEmpty()) Text("No hay elementos para esta línea y año.")
        elementos.forEach { (id, titulo) ->
            Card(
                modifier = Modifier.fillMaxWidth().clickable {
                    onFichaClick(categoria, id)
                }
            ) {
                Text(titulo, modifier = Modifier.padding(16.dp))
            }
        }
        Button(onClick = onVolverClick, modifier = Modifier.fillMaxWidth()) {
            Text("Volver")
        }
    }
}

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
