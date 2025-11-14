package com.example.foodexpress.view

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodexpress.R

data class Categoria(
    val nombre: String,
    @DrawableRes val imagen: Int
)

@Composable
fun SeccionCategorias() {
    val categorias = listOf(
        Categoria("Vegana", R.drawable.comida_vegana),
        Categoria("Sin Gluten", R.drawable.sin_gluten),
        Categoria("Halal", R.drawable.comidas_halal),
        Categoria("Vegetariana", R.drawable.comida_vegetariana),
        Categoria("Pescetariana", R.drawable.comida_pescetariana),
        Categoria("Sin Lactosa", R.drawable.comida_sin_lactosa)
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "\uD83D\uDCCB Nuestras Categorías",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.height(240.dp), // Altura para 2 filas
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(categorias) { categoria ->
                TarjetaCategoria(categoria = categoria)
            }
        }
    }
}

@Composable
fun TarjetaCategoria(categoria: Categoria) {
    Card(
        modifier = Modifier.aspectRatio(1f), // Para que sea cuadrada
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Image(
                painter = painterResource(id = categoria.imagen),
                contentDescription = categoria.nombre,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.4f))
            )
            Text(
                text = categoria.nombre,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}