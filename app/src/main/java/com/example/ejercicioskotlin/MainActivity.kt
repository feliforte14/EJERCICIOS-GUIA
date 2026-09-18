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
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Button
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EjerciciosKotlinTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(all = 26.dp)
                    ) {
                        Contador()
                    }
                }
            }
        }
    }
}

@Composable
fun Contador() {
    // remember + mutableStateOf: es lo que hace que Compose "observe" este valor
    // y vuelva a dibujar (recomponer) el Text cuando cambia.
    var contador by remember { mutableStateOf(0) }

    Text("Valor: $contador")

    Button(onClick = { contador++ }) {
        Text("+1")
    }

    Button(onClick = {
        // No puede ser menor que cero: solo decrementa si contador es mayor a 0.
        if (contador > 0) {
            contador--
        }
    }) {
        Text("-1")
    }

    // Reiniciar: reasigna el estado directo a 0, gatillando recomposición igual
    // que ++ o -- (es la misma variable observada, solo cambia el valor asignado).
    Button(onClick = { contador = 0 }) {
        Text("Reiniciar")
    }
}

