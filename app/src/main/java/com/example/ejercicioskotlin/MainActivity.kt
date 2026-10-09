package com.example.ejercicioskotlin

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ejercicioskotlin.ui.theme.EjerciciosKotlinTheme

// EJERCICIO 12 — Primer contacto con el Android SDK: Context e Intents.
// Objetivo: darse cuenta de qué es Kotlin, qué es Compose y qué es Android SDK, y
// DELEGAR acciones a otras apps del dispositivo (compartir texto, abrir un link).
//
// Clasificación de lo que aparece en este archivo (pregunta de la guía):
//   val, fun, if, try/catch, apply  → KOTLIN   (el lenguaje; funciona igual fuera de Android)
//   @Composable, Button, Text, Column, Modifier, LocalContext → COMPOSE (librería de UI)
//   Intent, Uri, Context, Log, ActivityNotFoundException      → ANDROID SDK (APIs del sistema)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EjerciciosKotlinTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CompartirRecomendacion(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun CompartirRecomendacion(modifier: Modifier = Modifier) {
    // LocalContext.current: Compose nos entrega el Context de Android (acá, la Activity).
    // Context es el "puente" con el sistema: sin él no podemos lanzar otras pantallas/apps.
    // LocalContext es de Compose; lo que devuelve (Context) es del Android SDK.
    val context = LocalContext.current

    val mensaje = "Estoy aprendiendo Android con Compose"

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Compartir recomendación",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(text = mensaje)

        // ── PARTE A: Intent.ACTION_SEND (compartir texto) ──
        Button(onClick = {
            // Un Intent es un MENSAJE que describe una operación que queremos que se haga.
            // Este es un Intent IMPLÍCITO: no decimos qué app lo atiende, solo la acción
            // (ACTION_SEND) y el tipo de dato (texto plano). El sistema busca qué apps pueden.
            val intent = Intent(Intent.ACTION_SEND).apply {
                // apply { } es de Kotlin: configura el objeto y lo devuelve.
                type = "text/plain"                      // tipo MIME: qué clase de dato mandamos
                putExtra(Intent.EXTRA_TEXT, mensaje)     // "extra": el dato en sí que viaja en el Intent
            }
            // createChooser fuerza que aparezca el selector del sistema ("Compartir con..."),
            // aunque haya una app por defecto. Mostramos el selector, no elegimos nosotros.
            context.startActivity(Intent.createChooser(intent, "Compartir con"))
        }) {
            Text("Compartir")
        }

        // ── PARTE B: Intent.ACTION_VIEW (abrir una dirección web) ──
        OutlinedButton(onClick = {
            // ACTION_VIEW + una Uri: "mostrá este recurso". Para una URL http(s) el sistema
            // suele abrir el navegador. Uri.parse convierte el String en un objeto Uri.
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://developer.android.com"))
            try {
                context.startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                // Si ninguna app del dispositivo puede atender el Intent, startActivity lanza
                // esta excepción y la app se cerraría. La capturamos y dejamos un rastro en Logcat.
                Log.w("INTENT", "No hay ninguna app que pueda abrir este link", e)
            }
        }) {
            Text("Abrir documentación")
        }
    }
    // Nota: no hizo falta pedir NINGÚN permiso en el AndroidManifest. Compartir o abrir un link
    // con un Intent delega el trabajo en otra app, así que nuestra app no necesita el permiso.
}

@Preview(showBackground = true)
@Composable
fun CompartirRecomendacionPreview() {
    // En el Preview no hay una Activity real: LocalContext existe, pero lanzar un Intent
    // solo se puede probar ejecutando la app (Run) en el emulador o un dispositivo.
    EjerciciosKotlinTheme {
        CompartirRecomendacion()
    }
}
