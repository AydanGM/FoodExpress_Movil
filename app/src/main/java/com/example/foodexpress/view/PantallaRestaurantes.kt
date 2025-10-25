package com.example.foodexpress.view

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.foodexpress.R

data class Restaurante(
    val id: Int,
    val nombre: String,
    val direccion: String,
    val tipoComida: String,
    val calificacion: Double,
    @DrawableRes val imagen: Int
)

val listaDeRestaurantes = listOf(
    Restaurante(1, "Pizzería Bella Italia", "Av. del Libertador 1234", "Italiana", 4.5, R.drawable.promo_pizzas),
    Restaurante(2, "Rincón Vegetariano", "Calle 5 de Mayo 567", "Vegetariana", 4.2, R.drawable.promo_vegetariana),
    Restaurante(3, "La Parrilla de Juan", "Costanera Norte 890", "Carnes", 4.8, R.drawable.chuleta),
    Restaurante(4, "Todo Express", "Avenida Principal 101", "Comida Rápida", 4.3, R.drawable.delivery),
    Restaurante(5, "Sabor Casero", "Calle de la Soja 222", "Variada", 4.6, R.drawable.delivery)
)

@Composable
fun PantallaRestaurantes() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Restaurantes Cercanos",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        items(listaDeRestaurantes) { restaurante ->
            TarjetaRestaurante(restaurante = restaurante)
        }
    }
}

@Composable
fun TarjetaRestaurante(restaurante: Restaurante) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            Image(
                painter = painterResource(id = restaurante.imagen),
                contentDescription = restaurante.nombre,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = restaurante.nombre,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = restaurante.direccion,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = restaurante.tipoComida,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Calificación",
                            tint = Color(0xFFFFC107) // Amber color
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = restaurante.calificacion.toString(),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPantallaRestaurantes() {
    PantallaRestaurantes()
}