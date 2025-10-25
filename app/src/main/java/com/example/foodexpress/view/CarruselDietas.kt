package com.example.foodexpress.view

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
        ItemDieta("Comidas Tradicionales", "Investiga sobre restaurantes y comidas varias si no tienes una preferencia de dieta.", R.drawable.ic_launcher_background),
        ItemDieta("Comidas Veganas", "¿Quieres alimentos de origen vegetal? Aquí tienes una variedad de opciones veganas.", R.drawable.ic_launcher_background),
        ItemDieta("Comidas Vegetarianas", "Explora deliciosas opciones vegetarianas que satisfacen tu paladar.", R.drawable.ic_launcher_background),
        ItemDieta("Comidas Pescetarianas", "Si eres pescetariano, aquí encontrarás platos que incluyen pescado y mariscos.", R.drawable.ic_launcher_background),
        ItemDieta("Comidas Sin Gluten", "Descubre opciones sin gluten para una alimentación saludable y segura.", R.drawable.ic_launcher_background),
        ItemDieta("Dieta Keto", "Explora platos bajos en carbohidratos y altos en grasas para una dieta cetogénica.", R.drawable.ic_launcher_background),
        ItemDieta("Dieta Halal", "Encuentra comidas que cumplen con los requisitos de la dieta halal.", R.drawable.ic_launcher_background),
        ItemDieta("Comidas Sin Lactosa", "Explora opciones de alimentos que son libres de lactosa.", R.drawable.ic_launcher_background)
    )

    Column {
        Text(
            text = "Categorías de Dieta",
            fontSize = 20.sp,
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
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = itemDieta.descripcion,
                fontSize = 12.sp
            )
        }
    }
}