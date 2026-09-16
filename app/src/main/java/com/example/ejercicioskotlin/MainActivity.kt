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
                    Presentacion(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun Presentacion(modifier: Modifier = Modifier) {
    // Column es el layout: sin él, los tres Text se superponen unos sobre otros
    // en vez de apilarse verticalmente (esto es justo lo que pide probar el punto 3
    // de la experimentación).
    Column(modifier = modifier) {
        Text(text = "Felipe")
        Text(text = "Ingeniería en Sistemas")
        Text(text = "Quiero aprender Kotlin")
    }
}

@Preview(showBackground = true)
@Composable
fun PresentacionPreview() {
    EjerciciosKotlinTheme {
        Presentacion()
    }
}
