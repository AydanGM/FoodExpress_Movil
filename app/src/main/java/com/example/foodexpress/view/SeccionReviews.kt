package com.example.foodexpress.view

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.foodexpress.R

data class Resena(
    val nombre: String,
    val texto: String,
    val estrellas: Int,
    @DrawableRes val imagen: Int
)

@Composable
fun SeccionReviews() {
    val resenas = listOf(
        Resena(
            "Aydan G.",
            "La comida llegó rapidisimo y estaba deliciosa. ¡Muy recomendable!",
            5,
            R.drawable.foto_perfil_2
        ),
        Resena(
            "Marco C.",
            "Me encanta la variedad de opciones veganas. Siempre encuentro justo lo que quiero.",
            4,
            R.drawable.foto_perfil_3
        ),
        Resena(
            "Paz V.",
            "El servicio al cliente fue excepcional. Tuvieron un pequeño error con mi pedido y lo solucionaron rápidamente.",
            5,
            R.drawable.foto_perfil_4
        )
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Lo que dicen nuestros clientes",
            style = MaterialTheme.typography.headlineSmall,
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
            // Imagen de perfil del cliente.
            Image(
                painter = painterResource(id = resena.imagen),
                contentDescription = "Foto de perfil de ${resena.nombre}",
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))

            // El texto de la reseña.
            Text(
                text = resena.texto,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp),
                textAlign = TextAlign.Center
            )

            Text(
                text = "⭐".repeat(resena.estrellas),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Text(
                text = resena.nombre,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}