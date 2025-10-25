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

data class ItemCarrito(
    val id: Int,
    val nombre: String,
    val precio: Double,
    var cantidad: Int
)

@Composable
fun PantallaCarrito(carritoViewModel: CarritoViewModel) {
    val uiState by carritoViewModel.uiState.collectAsState()
    PantallaCarritoContent(
        uiState = uiState,
        onEliminarClick = { item -> carritoViewModel.eliminarDelCarrito(item) }
    )
}

@Composable
fun PantallaCarritoContent(uiState: CarritoUiState, onEliminarClick: (ItemCarrito) -> Unit) {
    val itemsCarrito = uiState.items
    val total = uiState.total

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Mi Carrito",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (itemsCarrito.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Tu carrito está vacío.")
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(itemsCarrito) { item ->
                    TarjetaItemCarrito(item = item, onEliminarClick = onEliminarClick)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Total:", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(String.format("$%.2f", total), fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { /* TODO: Procesar pago */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Proceder al Pago")
            }
        }
    }
}

@Composable
fun TarjetaItemCarrito(item: ItemCarrito, onEliminarClick: (ItemCarrito) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(item.nombre, fontWeight = FontWeight.Bold)
                Text(String.format("$%.2f", item.precio))
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("x${item.cantidad}")
                IconButton(onClick = { onEliminarClick(item) }) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar item", tint = Color.Red)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPantallaCarrito() {
    val previewState = CarritoUiState(
        items = listOf(
            ItemCarrito(1, "Hamburguesa Clásica", 8.99, 2),
            ItemCarrito(2, "Papas Fritas", 3.49, 1)
        )
    )
    PantallaCarritoContent(uiState = previewState, onEliminarClick = {})
}