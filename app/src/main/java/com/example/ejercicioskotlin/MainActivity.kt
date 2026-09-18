package com.example.ejercicioskotlin

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
                    Calculadora(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

// Función separada de la UI: no es @Composable, solo lógica pura.
fun calcular(a: Double, b: Double, operacion: String): Double {
    return when (operacion) {
        "sumar" -> a + b
        "restar" -> a - b
        "multiplicar" -> a * b
        else -> 0.0
    }
}

@Composable
fun Calculadora(modifier: Modifier = Modifier) {
    var textoA by remember { mutableStateOf("") }
    var textoB by remember { mutableStateOf("") }
    var resultado by remember { mutableStateOf("") }

    fun ejecutar(operacion: String) {
        val a = textoA.toDoubleOrNull()
        val b = textoB.toDoubleOrNull()

        if (a == null || b == null) {
            resultado = "Error: ingresá dos números válidos"
            return
        }

        val valorCalculado = calcular(a, b, operacion)

        // Parte C: log con etiqueta reconocible para filtrar en Logcat.
        Log.d("CALCULADORA", "a=$a b=$b operacion=$operacion resultado=$valorCalculado")

        resultado = valorCalculado.toString()
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = textoA,
            onValueChange = { textoA = it },
            label = { Text("Número A") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = textoB,
            onValueChange = { textoB = it },
            label = { Text("Número B") },
            modifier = Modifier.fillMaxWidth()
        )

        Row {
            Button(onClick = { ejecutar("sumar") }) {
                Text("Sumar")
            }
            Button(onClick = { ejecutar("restar") }) {
                Text("Restar")
            }
            Button(onClick = { ejecutar("multiplicar") }) {
                Text("Multiplicar")
            }
        }

        Text("Resultado: $resultado")
    }
}

@Preview(showBackground = true)
@Composable
fun CalculadoraPreview() {
    EjerciciosKotlinTheme {
        Calculadora()
    }
}
