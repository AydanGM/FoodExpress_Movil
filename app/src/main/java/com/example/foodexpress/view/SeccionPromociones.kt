package com.example.foodexpress.view

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.foodexpress.R

data class Promocion(
    val titulo: String,
    val descripcion: String,
    @DrawableRes val imagen: Int
)

@Composable
fun SeccionPromociones() {
    val promociones = listOf(
        Promocion(
            "¡2x1 en Pizzas!",
            "Lleva dos y paga una. No te lo pierdas.",
            R.drawable.promo_pizzas
        ),
        Promocion(
            "20% OFF en comida vegetariana",
            "Toda la carta vegetariana con descuento.",
            R.drawable.promo_vegetariana
        )
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "🔥 Promociones para ti",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            promociones.forEach { promo ->
                TarjetaPromocion(promocion = promo, modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun TarjetaPromocion(promocion: Promocion, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.height(220.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = promocion.imagen),
                contentDescription = promocion.titulo,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f))
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = promocion.titulo,
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = promocion.descripcion,
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { /* TODO: Navegar a la promoción */ }) {
                    Text("Ver más")
                }
            }
        }
    }
}