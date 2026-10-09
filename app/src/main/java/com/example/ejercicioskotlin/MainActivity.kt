package com.example.ejercicioskotlin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ejercicioskotlin.ui.theme.EjerciciosKotlinTheme

// EJERCICIO 13 — Miniaplicación integradora: Organizador de cursada.
// Junta todo lo visto: Kotlin básico (data class, when, null-safety), Compose (estado, listas,
// state hoisting), recursos (strings.xml) y un primer uso del Android SDK (Intent, Log).
//
// ── ETAPA 1 · DISEÑO (lo que se pide pensar ANTES de programar) ──
//  Datos de cada actividad : titulo, materia, prioridad, completada      → data class Actividad
//  Estados de la pantalla  : titulo, materia, prioridad (lo que se escribe), actividades (la lista),
//                            filtro (qué se muestra) y mostrarError (aviso de validación)
//  Se DERIVA (no se guarda): cantidad de pendientes = actividades.count { !it.completada }
//  Composables reutilizables: FormularioActividad, ActividadItem, SelectorFiltro
//  Eventos del usuario     : escribir título/materia · elegir prioridad · Agregar · tildar ·
//                            Eliminar · cambiar filtro · Compartir resumen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EjerciciosKotlinTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    OrganizadorScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

// Modelo (Kotlin puro). prioridad: 1 = baja, 2 = media, 3 = alta.
// Es inmutable (todo val): para "completar" una actividad se reemplaza por una copia con copy().
data class Actividad(
    val titulo: String,
    val materia: String,
    val prioridad: Int,
    val completada: Boolean = false
)

// Opciones del filtro (desafío opcional). Un enum es ideal: son pocos valores fijos.
enum class FiltroActividades { TODAS, PENDIENTES, COMPLETADAS }

// ── Lógica separada de la UI (Kotlin puro: no sabe nada de Compose ni de Android) ──
fun cumpleFiltro(actividad: Actividad, filtro: FiltroActividades): Boolean = when (filtro) {
    FiltroActividades.TODAS -> true
    FiltroActividades.PENDIENTES -> !actividad.completada
    FiltroActividades.COMPLETADAS -> actividad.completada
}

fun armarResumen(encabezado: String, pendientes: List<Actividad>): String =
    encabezado + "\n" + pendientes.joinToString("\n") { "- ${it.titulo} (${it.materia})" }

// Un composable que DEVUELVE un valor se nombra en minúscula (convención de Compose).
// Necesita ser @Composable porque llama a stringResource.
@Composable
fun etiquetaPrioridad(prioridad: Int): String = stringResource(
    when (prioridad) {
        1 -> R.string.prioridad_baja
        2 -> R.string.prioridad_media
        else -> R.string.prioridad_alta
    }
)

// ── ETAPAS 2 y 3 · Pantalla: ACÁ vive TODO el estado (state hoisting) ──
// Los hijos (formulario, filas, filtro) no guardan estado: reciben valores y callbacks.
// actividadesIniciales existe para poder armar Previews con datos de ejemplo.
@Composable
fun OrganizadorScreen(
    modifier: Modifier = Modifier,
    actividadesIniciales: List<Actividad> = emptyList()
) {
    val context = LocalContext.current   // Android SDK: lo necesitamos para lanzar el Intent de compartir

    // Estado de lo que el usuario está escribiendo/eligiendo.
    var titulo by remember { mutableStateOf("") }
    var materia by remember { mutableStateOf("") }
    var prioridad by remember { mutableStateOf(1) }
    var mostrarError by remember { mutableStateOf(false) }
    var filtro by remember { mutableStateOf(FiltroActividades.TODAS) }
    // Estado de la colección: mutableStateListOf avisa a Compose al agregar/quitar/reemplazar.
    val actividades = remember {
        mutableStateListOf<Actividad>().apply { addAll(actividadesIniciales) }
    }

    // Valores DERIVADOS del estado: se recalculan en cada recomposición, no se guardan aparte.
    val pendientes = actividades.count { !it.completada }
    // withIndex() conserva la posición ORIGINAL de cada actividad dentro de "actividades".
    // Hace falta porque la lista que se muestra está filtrada y ordenada: el índice de la fila
    // en pantalla ya no coincide con el índice real y marcaríamos/borraríamos la actividad
    // equivocada.
    val visibles = actividades.withIndex()
        .filter { cumpleFiltro(it.value, filtro) }
        .sortedByDescending { it.value.prioridad }   // desafío: ordenar por prioridad (alta primero)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(R.string.titulo_pantalla),
            style = MaterialTheme.typography.headlineMedium
        )

        FormularioActividad(
            titulo = titulo, onTituloChange = { titulo = it },
            materia = materia, onMateriaChange = { materia = it },
            prioridad = prioridad, onPrioridadChange = { prioridad = it },
            mostrarError = mostrarError,
            onAgregar = {
                // Validación: no se agregan actividades con título o materia vacíos.
                if (titulo.isBlank() || materia.isBlank()) {
                    mostrarError = true
                } else {
                    // ETAPA 4: un buen lugar para un BREAKPOINT es la línea de abajo. Con Debug
                    // se puede inspeccionar titulo, materia y prioridad justo antes de agregar.
                    actividades.add(Actividad(titulo.trim(), materia.trim(), prioridad))
                    // ETAPA 4: mensaje de Logcat para diagnóstico (filtrar por la etiqueta ORGANIZADOR).
                    Log.d("ORGANIZADOR", "Agregada '${titulo.trim()}'. Total=${actividades.size}")
                    titulo = ""
                    materia = ""
                    prioridad = 1
                    mostrarError = false
                }
            }
        )

        // Requisito 8: cuántas están pendientes (derivado, no es estado propio).
        Text(stringResource(R.string.pendientes, pendientes))

        SelectorFiltro(filtroActual = filtro, onFiltroChange = { filtro = it })

        // weight(1f): la lista ocupa el espacio que sobra y el botón de compartir queda abajo.
        if (actividades.isEmpty()) {
            Text(stringResource(R.string.sin_actividades), Modifier.weight(1f))     // desafío: mensaje de vacío
        } else if (visibles.isEmpty()) {
            Text(stringResource(R.string.sin_resultados), Modifier.weight(1f))
        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                // Cada elemento es un IndexedValue: "indice" = posición real en "actividades".
                items(visibles) { (indice, actividad) ->
                    ActividadItem(
                        actividad = actividad,
                        // Requisito 6: marcar como completada (reemplazo por una copia).
                        onToggle = { actividades[indice] = actividad.copy(completada = !actividad.completada) },
                        // Requisito 7: eliminar.
                        onEliminar = { actividades.removeAt(indice) }
                    )
                }
            }
        }

        // Desafío: compartir el resumen con un Intent (igual que en el Ej. 12).
        OutlinedButton(
            onClick = {
                val resumen = armarResumen(
                    encabezado = context.getString(R.string.resumen_encabezado, pendientes),
                    pendientes = actividades.filter { !it.completada }
                )
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, resumen)
                }
                context.startActivity(Intent.createChooser(intent, null))
            },
            enabled = actividades.isNotEmpty(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.compartir_resumen))
        }
    }
}

// Composable SIN estado propio: todo entra por parámetro, todo evento sale por callback.
@Composable
fun FormularioActividad(
    titulo: String,
    onTituloChange: (String) -> Unit,
    materia: String,
    onMateriaChange: (String) -> Unit,
    prioridad: Int,
    onPrioridadChange: (Int) -> Unit,
    mostrarError: Boolean,
    onAgregar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        OutlinedTextField(
            value = titulo,
            onValueChange = onTituloChange,
            label = { Text(stringResource(R.string.campo_titulo)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = materia,
            onValueChange = onMateriaChange,
            label = { Text(stringResource(R.string.campo_materia)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        // Prioridad simple: tres RadioButton (1, 2, 3). selected sale del estado; el click avisa.
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(stringResource(R.string.prioridad))
            (1..3).forEach { p ->
                RadioButton(selected = prioridad == p, onClick = { onPrioridadChange(p) })
                Text(etiquetaPrioridad(p))
            }
        }
        if (mostrarError) {
            Text(
                text = stringResource(R.string.error_campos),
                color = MaterialTheme.colorScheme.error
            )
        }
        Button(onClick = onAgregar) {
            Text(stringResource(R.string.agregar))
        }
    }
}

@Composable
fun SelectorFiltro(
    filtroActual: FiltroActividades,
    onFiltroChange: (FiltroActividades) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        FiltroActividades.entries.forEach { opcion ->
            FilterChip(
                selected = filtroActual == opcion,
                onClick = { onFiltroChange(opcion) },
                label = {
                    Text(
                        stringResource(
                            when (opcion) {
                                FiltroActividades.TODAS -> R.string.filtro_todas
                                FiltroActividades.PENDIENTES -> R.string.filtro_pendientes
                                FiltroActividades.COMPLETADAS -> R.string.filtro_completadas
                            }
                        )
                    )
                }
            )
        }
    }
}

@Composable
fun ActividadItem(
    actividad: Actividad,
    onToggle: () -> Unit,
    onEliminar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(checked = actividad.completada, onCheckedChange = { onToggle() })
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = actividad.titulo,
                    // Una actividad completada se muestra tachada: la UI es función del estado.
                    textDecoration = if (actividad.completada) TextDecoration.LineThrough else TextDecoration.None
                )
                Text(
                    text = stringResource(
                        R.string.item_detalle,
                        actividad.materia,
                        etiquetaPrioridad(actividad.prioridad)
                    ),
                    style = MaterialTheme.typography.bodySmall
                )
            }
            TextButton(onClick = onEliminar) {
                Text(stringResource(R.string.eliminar))
            }
        }
    }
}

// ───────────────────────── PREVIEWS (ETAPA 4) ─────────────────────────
// Datos de ejemplo para ver la pantalla "llena" sin tener que cargar nada a mano.
private val actividadesDeEjemplo = listOf(
    Actividad("Entregar TP de Compose", "Desarrollo de Aplicaciones I", 3),
    Actividad("Leer apunte de persistencia", "Desarrollo de Aplicaciones I", 2, completada = true),
    Actividad("Practicar integrales", "Matemática", 1)
)

@Preview(name = "Con datos", showBackground = true)
@Composable
fun OrganizadorConDatosPreview() {
    EjerciciosKotlinTheme {
        Surface { OrganizadorScreen(actividadesIniciales = actividadesDeEjemplo) }
    }
}

@Preview(name = "Vacío", showBackground = true)
@Composable
fun OrganizadorVacioPreview() {
    EjerciciosKotlinTheme {
        Surface { OrganizadorScreen() }
    }
}

@Preview(name = "Una fila", showBackground = true)
@Composable
fun ActividadItemPreview() {
    EjerciciosKotlinTheme {
        Surface {
            ActividadItem(
                actividad = actividadesDeEjemplo[1],
                onToggle = {},
                onEliminar = {}
            )
        }
    }
}
