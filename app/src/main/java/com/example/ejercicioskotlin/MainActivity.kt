package com.example.ejercicioskotlin

import android.content.res.Configuration
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ejercicioskotlin.ui.theme.EjerciciosKotlinTheme

// EJERCICIO 11 — Recursos Android y configuración visual.
// Partimos de la lista de tareas del Ejercicio 10 y movemos TODOS los textos visibles
// a res/values/strings.xml. Cambios respecto del Ej. 10:
//   1) Cada Text("...") fijo pasó a Text(stringResource(R.string.xxx)).
//   2) Se agregó res/values-en/strings.xml (traducción al inglés sin tocar este archivo).
//   3) Se agregaron varios @Preview: claro, oscuro, pantalla chica e inglés.

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

data class Tarea(
    val texto: String,
    val completada: Boolean = false
)

@Composable
fun ListaTareas(modifier: Modifier = Modifier) {
    // Estado mínimo (igual que en el Ej. 10): el texto que se escribe y la colección de tareas.
    var textoNuevo by remember { mutableStateOf("") }
    val tareas = remember { mutableStateListOf<Tarea>() }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // stringResource busca la clave en el strings.xml que corresponda al idioma del
        // dispositivo (values/ por defecto, values-en/ si el celular está en inglés).
        // Solo se puede llamar desde un @Composable; fuera de uno se usa context.getString(...).
        Text(
            text = stringResource(R.string.titulo_tareas),
            style = MaterialTheme.typography.headlineMedium
        )

        OutlinedTextField(
            value = textoNuevo,
            onValueChange = { textoNuevo = it },
            label = { Text(stringResource(R.string.nueva_tarea)) },
            modifier = Modifier.fillMaxWidth()
        )

        Row {
            Button(onClick = {
                if (textoNuevo.isNotBlank()) {
                    tareas.add(Tarea(texto = textoNuevo))
                    textoNuevo = ""
                }
            }) {
                Text(stringResource(R.string.agregar))
            }
            Button(onClick = { tareas.clear() }) {
                Text(stringResource(R.string.borrar_todas))
            }
        }

        // Texto con argumento: en strings.xml está "Tareas: %1$d" y acá le pasamos el número.
        // El 2º parámetro de stringResource reemplaza a %1$d.
        Text(stringResource(R.string.cantidad_tareas, tareas.size))

        LazyColumn {
            itemsIndexed(tareas) { index, tarea ->
                TareaItem(
                    tarea = tarea,
                    onToggleCompletada = {
                        // Tarea es inmutable: se reemplaza por una copia con el campo cambiado.
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
            // tarea.texto NO sale de recursos: es un dato que escribe el usuario, no un texto
            // fijo de la interfaz. Los recursos son para el texto "de la app", no para sus datos.
            Text(
                text = tarea.texto,
                textDecoration = if (tarea.completada) TextDecoration.LineThrough else TextDecoration.None
            )
        }

        // Antes era un IconButton con "✕" hardcodeado; ahora el texto es traducible.
        TextButton(onClick = onEliminar) {
            Text(stringResource(R.string.eliminar))
        }
    }
}

// ───────────────────────── PREVIEWS ─────────────────────────
// Un @Preview dibuja el composable dentro de Android Studio, SIN emulador. Poner varios
// permite ver distintas configuraciones a la vez. Se envuelven en Surface para que el
// fondo y el color del texto sigan el tema (si no, en modo oscuro el texto no se vería).

// 1) Configuración normal (modo claro, idioma por defecto).
@Preview(name = "Claro", showBackground = true)
@Composable
fun ListaTareasPreview() {
    EjerciciosKotlinTheme {
        Surface { ListaTareas() }
    }
}

// 2) Modo oscuro: uiMode le dice al preview que simule el modo noche del sistema.
@Preview(
    name = "Oscuro",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun ListaTareasOscuroPreview() {
    EjerciciosKotlinTheme {
        Surface { ListaTareas() }
    }
}

// 3) Otro idioma: el mismo composable, leyendo values-en/strings.xml. Prueba que el código
//    no cambia para traducir.
@Preview(name = "Inglés", showBackground = true, locale = "en")
@Composable
fun ListaTareasInglesPreview() {
    EjerciciosKotlinTheme {
        Surface { ListaTareas() }
    }
}

// 4) Pantalla chica y fuente grande: para chequear que nada se corte.
@Preview(
    name = "Pantalla chica + fuente grande",
    showBackground = true,
    widthDp = 320,
    heightDp = 560,
    fontScale = 1.5f
)
@Composable
fun ListaTareasChicaPreview() {
    EjerciciosKotlinTheme {
        Surface { ListaTareas() }
    }
}
