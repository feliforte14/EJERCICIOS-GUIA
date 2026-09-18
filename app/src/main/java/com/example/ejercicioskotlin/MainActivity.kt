package com.example.ejercicioskotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.ejercicioskotlin.ui.theme.EjerciciosKotlinTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.Button

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EjerciciosKotlinTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)   // espacio para no tapar barras del sistema
                            .padding(all = 26.dp)    // espaciado exterior propio de la pantalla
                    ) {
                        Text("FICHA DEL ESTUDIANTE")
                        // Datos organizados verticalmente: Column apila uno abajo del otro.
                        DatoEstudiante("Nombre:", "Ana")
                        // Row: fila con dos elementos lado a lado (horizontal, no vertical).
                        // spacedBy(16.dp) separa los dos DatoEstudiante para que no queden pegados.
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ){
                            DatoEstudiante("Carrera:","Sistemas")
                            DatoEstudiante("Anio:","1")
                        }
                        // Button sin comportamiento relevante: onClick vacío, solo para que
                        // exista visualmente en la pantalla.
                        Button({}) {
                            Text("Guardar")
                        }
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


@Preview(showBackground = true)
@Composable
fun DatoEstudiantePreview() {
    EjerciciosKotlinTheme {
        DatoEstudiante("Nombre", "Ana")
    }
}

// Experimento: background() ANTES de padding().
// El fondo pinta TODO el Column, y el padding empuja el contenido hacia
// adentro dejando ver el color alrededor del texto.
@Preview(showBackground = true)
@Composable
fun OrdenModifierBackgroundPrimeroPreview() {
    EjerciciosKotlinTheme {
        Column(
            modifier = Modifier
                .background(Color.Blue)
                .padding(16.dp)
        ) {
            Text("Fondo primero, padding despues")
        }
    }
}

// Experimento: padding() ANTES de background().
// El padding reserva espacio vacío (sin color todavía), y el background
// solo pinta lo que queda después de ese espacio — queda un borde sin color.
@Preview(showBackground = true)
@Composable
fun OrdenModifierPaddingPrimeroPreview() {
    EjerciciosKotlinTheme {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.Blue)
        ) {
            Text("Padding primero, fondo despues")
        }
    }
}
