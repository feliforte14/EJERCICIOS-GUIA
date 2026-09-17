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
                    Column(modifier = Modifier.padding(innerPadding)) {
                        DatoEstudiante("Nombre", "Ana")
                        DatoEstudiante("Carrera", "Sistemas")
                        DatoEstudiante("Anio", "1")
                        Text(descripcionEdad(20))
                    }
                }
            }
        }
    }
}


@Composable
fun DatoEstudiante(etiqueta: String, valor: String) {
    Text("$etiqueta $valor")
}

// Función Kotlin normal (sin @Composable): recibe un Int y devuelve un String.
// No dibuja nada por sí sola, solo calcula texto — por eso después se muestra
// con Text(descripcionEdad(...)) desde un composable.
fun descripcionEdad(edad: Int): String {
    // Desafío del profe Adrián: if como expresión, devuelve directo el String
    // según la condición, sin necesidad de variables intermedias ni múltiples return.
    return if (edad >= 18) {
        "Edad: $edad anios (mayor de edad)"
    } else {
        "Edad: $edad anios (menor de edad)"
    }
}


@Preview(showBackground = true)
@Composable
fun DatoEstudiantePreview() {
    EjerciciosKotlinTheme {
        DatoEstudiante("Nombre", "Ana")
    }
}
