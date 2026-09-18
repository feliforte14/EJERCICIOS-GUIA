package com.example.ejercicioskotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ejercicioskotlin.ui.theme.EjerciciosKotlinTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EjerciciosKotlinTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ListaTareas(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

// Estado mínimo de la app (lo que pide dibujar en papel antes de codear):
// 1) el texto que se está escribiendo ahora mismo
// 2) la colección de tareas ya agregadas
data class Tarea(
    val texto: String,
    val completada: Boolean = false
)

@Composable
fun ListaTareas(modifier: Modifier = Modifier) {
    var textoNuevo by remember { mutableStateOf("") }
    // mutableStateListOf: como mutableStateOf pero para listas — Compose
    // recompone cuando agregás/quitás elementos, no solo cuando reemplazás
    // la lista entera.
    val tareas = remember { mutableStateListOf<Tarea>() }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = textoNuevo,
            onValueChange = { textoNuevo = it },
            label = { Text("Nueva tarea") },
            modifier = Modifier.fillMaxWidth()
        )

        Row {
            Button(onClick = {
                // No agregar cadenas vacías (ni solo espacios).
                if (textoNuevo.isNotBlank()) {
                    tareas.add(Tarea(texto = textoNuevo))
                    textoNuevo = ""
                }
            }) {
                Text("Agregar")
            }

            // Desafío: botón "Borrar todas".
            Button(onClick = { tareas.clear() }) {
                Text("Borrar todas")
            }
        }

        // Desafío: mostrar la cantidad de tareas.
        Text("Tareas: ${tareas.size}")

        LazyColumn {
            itemsIndexed(tareas) { index, tarea ->
                TareaItem(
                    tarea = tarea,
                    onToggleCompletada = {
                        // Desafío: marcar una tarea como completada.
                        // Tarea es inmutable (data class con val), así que
                        // reemplazamos el elemento por una copia con el
                        // campo cambiado, en vez de mutarlo directo.
                        tareas[index] = tarea.copy(completada = !tarea.completada)
                    },
                    onEliminar = { tareas.removeAt(index) }
                )
            }
        }
    }
}

@Composable
fun TareaItem(
    tarea: Tarea,
    onToggleCompletada: () -> Unit,
    onEliminar: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = tarea.completada,
                onCheckedChange = { onToggleCompletada() }
            )
            Text(
                text = tarea.texto,
                textDecoration = if (tarea.completada) TextDecoration.LineThrough else TextDecoration.None
            )
        }

        IconButton(onClick = onEliminar) {
            Text("✕")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListaTareasPreview() {
    EjerciciosKotlinTheme {
        ListaTareas()
    }
}
