package com.example.foodexpress.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Resena(
    val nombre: String,
    val texto: String,
    val estrellas: Int
)

@Composable
fun SeccionReviews() {
    val resenas = listOf(
        Resena(
            "Aydan G.",
            "La comida llegó rapidisimo y estaba deliciosa. ¡Muy recomendable!",
            5
        ),
        Resena(
            "Marco C.",
            "Me encanta la variedad de opciones veganas. Siempre encuentro justo lo que quiero.",
            4
        ),
        Resena(
            "Paz V.",
            "El servicio al cliente fue excepcional. Tuvieron un pequeño error con mi pedido y lo solucionaron rápidamente.",
            5
        )
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Lo que dicen nuestros clientes",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            resenas.forEach { resena ->
                TarjetaResena(resena)
            }
        }
    }
}

@Composable
fun TarjetaResena(resena: Resena) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = resena.texto,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 8.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            // Estrellas
            Text(
                text = "⭐".repeat(resena.estrellas),
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Text(
                text = resena.nombre,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
    }
}