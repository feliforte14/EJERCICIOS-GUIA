package com.example.ejercicioskotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.ejercicioskotlin.ui.theme.EjerciciosKotlinTheme

class MainActivity : ComponentActivity() {
    // Punto de entrada de la Activity: se ejecuta una sola vez cuando el sistema la crea.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // savedInstanceState: trae el estado guardado si la Activity fue recreada

        enableEdgeToEdge()
        // Permite que el contenido se dibuje detrás de las barras de sistema (status bar,
        // navigation bar) para un look "edge to edge". Es lo que hace que el Scaffold
        // necesite después el innerPadding, para no tapar contenido detrás de esas barras.

        setContent {
            // setContent reemplaza al viejo setContentView(R.layout...) de las Views clásicas.
            // todo lo que esta dentro de este bloque es la UI declarativa hecha con Compose.

            EjerciciosKotlinTheme {
                // Envuelve la UI con el tema de la app (colores, tipografía, forma).
                // Es una función @Composable definida en ui/theme/Theme.kt.

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Scaffold da la estructura base de una pantalla Material
                    // (lugar para TopBar, BottomBar, FAB, etc., aunque acá no se usan).
                    // fillMaxSize() hace que ocupe toda la pantalla disponible.
                    // innerPadding es el espacio que hay que respetar para no quedar
                    // debajo de las barras de sistema (por el enableEdgeToEdge de arriba).

                    Greeting(
                        name = "Felipe",
                        modifier = Modifier.padding(innerPadding)
                        // Se le pasa el innerPadding como modifier para que el contenido
                        // no quede tapado por la status bar / navigation bar.
                    )
                }
            }
        }
    }
}

@Composable
// @Composable marca una función como parte de la UI declarativa: Compose la puede
// "recomponer" (volver a ejecutar) cada vez que cambian los datos de los que depende.
fun Greeting(name: String, modifier: Modifier = Modifier) {
    // name: parámetro de dato, lo que la función necesita mostrar.
    // modifier: parámetro convencional en Compose, permite que quien llama a este
    // Composable le agregue comportamiento externo (padding, tamaño, click, etc.)
    // sin que Greeting tenga que saber nada de eso. Por eso el default es Modifier
    // (sin modificaciones) y se recibe siempre como último parámetro con nombre.

    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
// @Preview le dice a Android Studio que renderice este Composable en el panel de
// vista previa del IDE, sin necesidad de correr la app en emulador o dispositivo.
// showBackground = true agrega un fondo para que se vea mejor en la preview.
@Composable
fun GreetingPreview() {
    // Función separada solo para la preview: no se usa en la app real.
    // Llama a Greeting con datos fijos ("Android") para poder visualizarlo.
    EjerciciosKotlinTheme {
        Greeting("Felipe")
    }
}
