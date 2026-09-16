package com.example.ejercicioskotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.ejercicioskotlin.ui.theme.EjerciciosKotlinTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EjerciciosKotlinTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    FichaDeEstudiante(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun FichaDeEstudiante(modifier: Modifier = Modifier) {
    val nombre = "Felipe"
    val edad = 20
    val promedio = 8.25
    val cursaProgramacion = true
    val anioProximo = edad + 1
    Column(modifier = modifier) {
        Text("Nombre del estudiante: $nombre")
        Text("Edad del estudiante: $edad")
        Text("Promedio del estudiante: $promedio")
        Text("Cursa programacion?: $cursaProgramacion")
        Text("edad proxima?: $anioProximo")
    }
}

@Preview(showBackground = true)
@Composable
fun FichaDeEstudiantePreview() {
    EjerciciosKotlinTheme {
        FichaDeEstudiante()
    }
}
