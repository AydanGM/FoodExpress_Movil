package com.example.foodexpress.view

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodexpress.R

data class ItemDieta(
    val titulo: String,
    val descripcion: String,
    @DrawableRes val imagen: Int
)

@Composable
fun CarruselDietas() {
    val itemsDieta = listOf(
        ItemDieta("\uD83C\uDF54 Comidas Tradicionales", "Restaurantes y comidas varias si no tienes una preferencia de dieta.", R.drawable.hamburguesa_con_guacamole),
        ItemDieta("\uD83E\uDD57 Comidas Veganas", "¿Buscas alimentos de origen vegetal? Explora opciones veganas", R.drawable.comida_vegana),
        ItemDieta("\uD83E\uDD66 Comidas Vegetarianas", "Explora deliciosas opciones vegetarianas que satisfacen tu paladar.", R.drawable.comida_vegetariana),
        ItemDieta("\uD83D\uDC1F Comidas Pescetarianas", "Si eres pescetariano, aquí encontrarás platos que incluyen pescado y mariscos.", R.drawable.comida_pescetariana),
        ItemDieta("\uD83C\uDF3E Comidas Sin Gluten", "Descubre opciones sin gluten para una alimentación saludable y segura.", R.drawable.sin_gluten),
        ItemDieta("\uD83E\uDD53 Dieta Keto", "“Platos bajos en carbohidratos y ricos en grasas para tu dieta keto.”", R.drawable.dieta_keto),
        ItemDieta("\uD83D\uDD4C Dieta Halal", "Encuentra comidas que cumplen con los requisitos de la dieta halal.", R.drawable.comidas_halal),
        ItemDieta("\uD83E\uDD5B Comidas Sin Lactosa", "Explora opciones de alimentos que son libres de lactosa.", R.drawable.comida_sin_lactosa)
    )

    Column {
        Text(
            text = "\uD83D\uDE0B Categorías de Dieta",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(itemsDieta) { item ->
                TarjetaDieta(item)
            }
        }
    }
}

@Composable
fun TarjetaDieta(itemDieta: ItemDieta) {
    Card(
        modifier = Modifier
            .width(280.dp)
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = itemDieta.imagen),
                contentDescription = itemDieta.titulo,
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = itemDieta.titulo,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = itemDieta.descripcion,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                modifier = Modifier
                        .heightIn(min = 32.dp)
                        .fillMaxWidth()

            )
        }
    }
}