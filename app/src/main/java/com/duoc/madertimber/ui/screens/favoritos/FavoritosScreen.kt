package com.duoc.madertimber.ui.screens.favoritos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.duoc.madertimber.data.model.CategoriaRelacionada
import com.duoc.madertimber.data.model.Favorito
import com.duoc.madertimber.ui.theme.MaderaTimberTheme
import com.duoc.madertimber.viewmodel.FavoritosEvento
import com.duoc.madertimber.viewmodel.FavoritosUiState

// Lista los favoritos guardados en la cuenta, con opción de quitarlos
@Composable
fun FavoritosScreen(
    estado: FavoritosUiState,
    onEvent: (FavoritosEvento) -> Unit,
    onVolverClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Mis favoritos", style = MaterialTheme.typography.headlineMedium)
        when {
            !estado.sesionIniciada -> Text("Inicia sesión para guardar y ver tus favoritos.")
            estado.cargando && estado.favoritos.isEmpty() -> CircularProgressIndicator()
            estado.favoritos.isEmpty() -> Text(
                "Aún no tienes favoritos. Abre la ficha de un proyecto, investigador o publicación y toca «Guardar en favoritos»."
            )
            else -> estado.favoritos.forEach { favorito ->
                TarjetaFavorito(
                    favorito = favorito,
                    habilitada = !estado.cargando,
                    onQuitar = { onEvent(FavoritosEvento.Quitar(favorito)) }
                )
            }
        }
        estado.mensaje?.let { mensaje ->
            Text(mensaje, color = MaterialTheme.colorScheme.primary)
        }
        Button(onClick = onVolverClick, modifier = Modifier.fillMaxWidth()) { Text("Volver") }
    }
}

@Composable
private fun TarjetaFavorito(favorito: Favorito, habilitada: Boolean, onQuitar: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(Icons.Default.Star, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Column(modifier = Modifier.weight(1f)) {
                Text(favorito.titulo, style = MaterialTheme.typography.titleMedium)
                Text(favorito.categoria.nombreSingular(), style = MaterialTheme.typography.bodySmall)
            }
            IconButton(onClick = onQuitar, enabled = habilitada) {
                Icon(Icons.Default.Delete, contentDescription = "Quitar ${favorito.titulo} de favoritos")
            }
        }
    }
}

private fun CategoriaRelacionada.nombreSingular(): String = when (this) {
    CategoriaRelacionada.PROYECTOS -> "Proyecto"
    CategoriaRelacionada.INVESTIGADORES -> "Investigador/a"
    CategoriaRelacionada.PUBLICACIONES -> "Publicación"
}

@Preview(showBackground = true)
@Composable
private fun FavoritosPreview() {
    MaderaTimberTheme {
        FavoritosScreen(
            estado = FavoritosUiState(
                nombreUsuario = "Ana",
                favoritos = listOf(Favorito(1, CategoriaRelacionada.PROYECTOS, "P-L1-01", "Proyecto de ejemplo"))
            ),
            onEvent = {},
            onVolverClick = {}
        )
    }
}
