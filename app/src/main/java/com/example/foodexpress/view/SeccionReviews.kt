package com.example.foodexpress.view

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
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
import androidx.compose.ui.unit.sp
import com.example.foodexpress.R

/**
 * Define la estructura de datos para una única reseña de un cliente.
 * @param nombre El nombre del cliente que dejó la reseña.
 * @param texto El contenido de la reseña.
 * @param estrellas El número de estrellas (de 1 a 5) que dio el cliente.
 * @param imagen El ID del recurso `drawable` para la foto de perfil del cliente.
 */
data class Resena(
    val nombre: String,
    val texto: String,
    val estrellas: Int,
    @DrawableRes val imagen: Int
)

/**
 * Composable que muestra la sección completa de reseñas de clientes.
 */
@Composable
fun SeccionReviews() {
    // Lista inmutable con datos de ejemplo para las reseñas.
    // En una aplicación real, estos datos vendrían de una base de datos o una API.
    val resenas = listOf(
        Resena(
            "Aydan G.",
            "La comida llegó rapidisimo y estaba deliciosa. ¡Muy recomendable!",
            5,
            R.drawable.ic_launcher_background // Imagen de marcador de posición.
        ),
        Resena(
            "Marco C.",
            "Me encanta la variedad de opciones veganas. Siempre encuentro justo lo que quiero.",
            4,
            R.drawable.ic_launcher_background
        ),
        Resena(
            "Paz V.",
            "El servicio al cliente fue excepcional. Tuvieron un pequeño error con mi pedido y lo solucionaron rápidamente.",
            5,
            R.drawable.ic_launcher_background
        )
    )

    // Columna para organizar el título y la lista de reseñas.
    Column(
        horizontalAlignment = Alignment.CenterHorizontally // Centra el título y las tarjetas.
    ) {
        // Título de la sección.
        Text(
            text = "Lo que dicen nuestros clientes",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Columna para apilar verticalmente las tarjetas de reseña.
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp), // Añade un espacio de 16.dp entre cada reseña.
            modifier = Modifier.fillMaxWidth()
        ) {
            // Itera sobre la lista de reseñas y crea una `TarjetaResena` para cada una.
            resenas.forEach { resena ->
                TarjetaResena(resena)
            }
        }
    }
}

/**
 * Composable que muestra una única tarjeta de reseña.
 * @param resena El objeto `Resena` con los datos a mostrar.
 */
@Composable
fun TarjetaResena(resena: Resena) {
    // El componente `Card` proporciona el fondo, la sombra y los bordes.
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        // Columna para centrar todo el contenido de la tarjeta.
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Imagen de perfil del cliente.
            Image(
                painter = painterResource(id = resena.imagen),
                contentDescription = "Foto de perfil de ${resena.nombre}",
                modifier = Modifier
                    .size(60.dp) // Tamaño fijo para la foto de perfil.
                    .clip(CircleShape), // Recorta la imagen en forma de círculo.
                contentScale = ContentScale.Crop // Escala la imagen para que llene el círculo.
            )

            Spacer(modifier = Modifier.height(8.dp)) // Espacio vertical.

            // El texto de la reseña.
            Text(
                text = resena.texto,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 8.dp),
                textAlign = TextAlign.Center // Centra el texto de la reseña.
            )

            // Muestra las estrellas de la calificación.
            // `"⭐".repeat(resena.estrellas)` crea una cadena con tantos emojis de estrella como indique la calificación.
            Text(
                text = "⭐".repeat(resena.estrellas),
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            // El nombre del cliente que escribió la reseña.
            Text(
                text = resena.nombre,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
    }
}