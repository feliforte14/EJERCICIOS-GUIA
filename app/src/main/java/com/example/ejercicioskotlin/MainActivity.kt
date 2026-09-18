package com.example.ejercicioskotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.ejercicioskotlin.ui.theme.EjerciciosKotlinTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Button


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EjerciciosKotlinTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ClasificadorEdad(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun ClasificadorEdad(modifier: Modifier = Modifier) {
    //mutableStateOf: no es una variable, es un objeto que compose puede "vigilar" para saber cuando cambio. Es un contenedor especial de estado.
    //remember: sin esto, cada vez que Compose recompone la funcion, ClasificadorEdad se ejecutaria de nuevo desde cero y mutableStateOf se volveria a crear con contenedor vacio ("")
    var nombre by remember { mutableStateOf("") }
    var edadTexto by remember { mutableStateOf("") }
    var resultado by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        // value muestra el estado actual; onValueChange lo actualiza en cada tecla.
        // Patrón unidireccional: usuario tipea -> evento -> cambia estado -> recompone.
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Ingrese Nombre:") }
        )
        OutlinedTextField(
            value = edadTexto,
            onValueChange = { edadTexto = it },
            label = { Text("Ingrese edad:") }
        )
        Button(onClick = {
            // toIntOrNull(): null safety. Si el texto no es un Int válido, devuelve
            // null en vez de crashear (a diferencia de toInt()).
            val edadNumero = edadTexto.toIntOrNull()
            // when como expresión: el chequeo de null va primero porque edadNumero
            // es Int? (nullable) — Kotlin no deja comparar "null < 18" directo.
            resultado = when {
                edadNumero == null -> "Error: no se ingreso nada"
                edadNumero < 0 -> "Dato no valido"
                edadNumero < 18 -> "Menor de edad"
                else -> "Mayor de edad"
            }
        }) {
            Text("Evaluar")
        }
        // resultado ya es String, no hace falta la plantilla "$resultado".
        Text(resultado)
    }
}

