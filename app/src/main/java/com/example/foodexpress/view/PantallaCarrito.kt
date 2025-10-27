package com.example.foodexpress.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodexpress.viewModel.CarritoUiState
import com.example.foodexpress.viewModel.CarritoViewModel

/**
 * Define la estructura de datos para un único ítem dentro del carrito de compras.
 * Es mutable en la cantidad para poder ser modificada directamente.
 * @param id El ID del producto, heredado del objeto `Comida`.
 * @param nombre El nombre del producto.
 * @param precio El precio unitario del producto.
 * @param cantidad La cantidad de este producto en el carrito. Es una `var` para poder ser modificada.
 */
data class ItemCarrito(
    val id: Int,
    val nombre: String,
    val precio: Double,
    var cantidad: Int
)

/**
 * Composable "stateful" (con estado) de la pantalla del carrito.
 * Su responsabilidad principal es conectar la UI con el ViewModel.
 * @param carritoViewModel El ViewModel que gestiona el estado y la lógica del carrito.
 */
@Composable
fun PantallaCarrito(carritoViewModel: CarritoViewModel) {
    // Se suscribe al estado de la UI del `carritoViewModel`.
    // La UI se recompondrá automáticamente cada vez que el estado del carrito cambie.
    val uiState by carritoViewModel.uiState.collectAsState()
    // Llama a la versión "stateless" (sin estado) del Composable, pasándole el estado actual y las funciones de callback.
    PantallaCarritoContent(
        uiState = uiState,
        onEliminarClick = { item -> carritoViewModel.eliminarDelCarrito(item) } // Pasa la función para eliminar un ítem.
    )
}

/**
 * Composable "stateless" (sin estado) que dibuja la UI de la pantalla del carrito.
 * Es más fácil de previsualizar y reutilizar porque no depende directamente de un ViewModel.
 * @param uiState El estado actual del carrito que se debe mostrar.
 * @param onEliminarClick La función lambda a ejecutar cuando se pulsa el botón de eliminar un ítem.
 */
@Composable
fun PantallaCarritoContent(uiState: CarritoUiState, onEliminarClick: (ItemCarrito) -> Unit) {
    // Extrae la lista de ítems y el total del estado para un acceso más fácil.
    val itemsCarrito = uiState.items
    val total = uiState.total

    // `Column` es el contenedor principal que organiza la pantalla verticalmente.
    Column(
        modifier = Modifier
            .fillMaxSize() // Ocupa todo el espacio disponible.
            .padding(16.dp) // Añade un padding alrededor del contenido.
    ) {
        // Título de la pantalla.
        Text(
            text = "Mi Carrito",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Comprueba si el carrito está vacío.
        if (itemsCarrito.isEmpty()) {
            // Si está vacío, muestra un mensaje centrado.
            Box(
                modifier = Modifier.fillMaxSize(), // Ocupa todo el espacio disponible.
                contentAlignment = Alignment.Center // Centra su contenido.
            ) {
                Text("Tu carrito está vacío.")
            }
        } else {
            // Si no está vacío, muestra la lista de ítems y el resumen de la compra.
            // `LazyColumn` para la lista de ítems, que es eficiente para listas largas.
            LazyColumn(
                modifier = Modifier.weight(1f), // Ocupa todo el espacio vertical disponible, dejando sitio para el total.
                verticalArrangement = Arrangement.spacedBy(8.dp) // Espacio entre cada tarjeta de ítem.
            ) {
                // `items` itera sobre la lista y crea un `TarjetaItemCarrito` para cada ítem.
                items(itemsCarrito) { item ->
                    TarjetaItemCarrito(item = item, onEliminarClick = onEliminarClick)
                }
            }
            Spacer(modifier = Modifier.height(16.dp)) // Espacio antes del total.
            // `Row` para mostrar el texto "Total:" y el precio en la misma línea.
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween // Coloca los elementos en extremos opuestos.
            ) {
                Text("Total:", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(String.format("$%.2f", total), fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(16.dp)) // Espacio antes del botón de pago.
            // Botón para finalizar la compra.
            Button(
                onClick = { /* TODO: Implementar la lógica para procesar el pago */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Proceder al Pago")
            }
        }
    }
}

/**
 * Composable que muestra una única fila para un ítem en el carrito.
 * @param item El `ItemCarrito` con los datos a mostrar.
 * @param onEliminarClick La función a ejecutar cuando se pulsa el botón de eliminar.
 */
@Composable
fun TarjetaItemCarrito(item: ItemCarrito, onEliminarClick: (ItemCarrito) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        // `Row` para alinear los detalles del producto y el botón de eliminar.
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // `Column` para el nombre y el precio del producto.
            Column(modifier = Modifier.weight(1f)) { // `weight(1f)` hace que ocupe el espacio sobrante.
                Text(item.nombre, fontWeight = FontWeight.Bold)
                Text(String.format("$%.2f", item.precio))
            }
            // `Row` para la cantidad y el botón de eliminar.
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("x${item.cantidad}") // Muestra la cantidad del ítem.
                // Botón con un icono para eliminar el ítem.
                IconButton(onClick = { onEliminarClick(item) }) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar item", tint = Color.Red)
                }
            }
        }
    }
}

/**
 * Previsualización para la pantalla del carrito con datos de ejemplo.
 */
@Preview(showBackground = true)
@Composable
fun PreviewPantallaCarrito() {
    // Se crea un estado de UI de ejemplo para la previsualización.
    val previewState = CarritoUiState(
        items = listOf(
            ItemCarrito(1, "Hamburguesa Clásica", 8.99, 2),
            ItemCarrito(2, "Papas Fritas", 3.49, 1)
        )
    )
    // Se llama a la versión "stateless" del Composable con los datos de ejemplo.
    PantallaCarritoContent(uiState = previewState, onEliminarClick = {})
}