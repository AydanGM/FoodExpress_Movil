package com.example.foodexpress.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Promocion(
    val titulo: String,
    val descripcion: String,
    val textoBoton: String
)

@Composable
fun SeccionPromociones() {
    val promociones = listOf(
        Promocion(
            "Todas las comidas vegetarianas 20% OFF",
            "Solo por hoy",
            "Ordenar ahora"
        ),
        Promocion(
            "Hamburguesa big-mamma 2x1",
            "Solo por hoy",
            "Ordenar ahora"
        ),
        Promocion(
            "Envio gratis en Pizzas La Cordillera",
            "Pedido mínimo $10.000",
            "Ordenar ahora"
        )
    )

    Column {
        Text(
            text = "🔥 Promociones Destacadas",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            promociones.forEach { promo ->
                TarjetaPromocion(promo)
            }
        }
    }
}

@Composable
fun TarjetaPromocion(promocion: Promocion) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Placeholder para imagen
            Box(
                modifier = Modifier
                    .height(80.dp)
                    .fillMaxWidth()
            ) {
                Text("🎯 Imagen Promo", modifier = Modifier.align(Alignment.Center))
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = promocion.titulo,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = promocion.descripcion,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = { /* TODO: Manejar orden */ }) {
                Text(promocion.textoBoton)
            }
        }
    }
}