package com.example.ejercicioskotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
                    ListaMaterias(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

// data class: modela UNA materia (los datos), separado de cómo se dibuja.
data class Materia(
    val nombre: String,
    val anio: Int,
    val aprobada: Boolean
)

val materias = listOf(
    Materia("Programacion I", 1, true),
    Materia("Matematica", 1, false),
    Materia("Sistemas Operativos", 2, true),
    Materia("Bases de Datos", 2, false),
    Materia("Desarrollo de Aplicaciones I", 3, true)
)

@Composable
fun ListaMaterias(modifier: Modifier = Modifier) {
    // Desafío 1: solo las aprobadas, con filter.
    val materiasAprobadas = materias.filter { it.aprobada }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Desafío 2: cantidad total con materias.size.
        Text("Total de materias: ${materias.size}")
        Text("Materias aprobadas: ${materiasAprobadas.size}")

        // LazyColumn: a diferencia de Column, solo dibuja las filas visibles en
        // pantalla (no todas de una) — clave para listas largas. items() recorre
        // la List y llama a MateriaItem por cada elemento.
        LazyColumn {
            items(materias) { materia ->
                MateriaItem(materia = materia)
            }
        }
    }
}

@Composable
fun MateriaItem(materia: Materia) {
    // Desafío 3: cambia visualmente el texto según "aprobada".
    val color = if (materia.aprobada) Color(0xFF2E7D32) else Color(0xFFC62828)
    val estado = if (materia.aprobada) "Aprobada" else "Pendiente"

    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(materia.nombre)
        Text("Año: ${materia.anio}")
        Text(text = estado, color = color)
    }
}

@Preview(showBackground = true)
@Composable
fun ListaMateriasPreview() {
    EjerciciosKotlinTheme {
        ListaMaterias()
    }
}
