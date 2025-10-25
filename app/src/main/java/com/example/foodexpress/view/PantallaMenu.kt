package com.example.foodexpress.view

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodexpress.R

data class Comida(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    @DrawableRes val imagen: Int
)

val listaDeComidas = listOf(
    Comida(1, "Hamburguesa con Guacamole", "Carne de res, guacamole, tocino, queso cheddar y jalapeños.", 10.99, R.drawable.hamburguesa_con_guacamole),
    Comida(2, "Pizza Vegana con Pesto", "Masa artesanal con pesto, champiñones, pimientos y queso vegano.", 14.50, R.drawable.pizza_vegana_casera_con_verduras_y_pesto),
    Comida(3, "Ensalada Griega Vegana", "Pepino, tomate, aceitunas, cebolla morada y queso feta vegano.", 8.25, R.drawable.ensalada_griega_vegana_4),
    Comida(4, "Lomo a lo Pobre", "Bistec de lomo, papas fritas, huevo frito y cebolla caramelizada.", 12.99, R.drawable.lomo_a_lo_pobre),
    Comida(5, "Macaroni & Cheese", "Clásica pasta con una cremosa salsa de queso cheddar.", 9.50, R.drawable.macaroni_cheese),
    Comida(6, "Hamburguesa de Soya", "Torta de soya, lechuga, tomate, pepinillos y mayonesa vegana.", 8.99, R.drawable.hamburgue_sasoya),
    Comida(7, "Chuleta de Cerdo", "Jugosa chuleta de cerdo a la parrilla con guarnición de puré de papas.", 11.99, R.drawable.chuleta)
)

@Composable
fun PantallaMenu(onAgregarClick: (Comida) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Menú Principal",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        items(listaDeComidas) { comida ->
            TarjetaComida(comida = comida, onAgregarClick = onAgregarClick)
        }
    }
}

@Composable
fun TarjetaComida(comida: Comida, onAgregarClick: (Comida) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            Image(
                painter = painterResource(id = comida.imagen),
                contentDescription = comida.nombre,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = comida.nombre,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = comida.descripcion,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.heightIn(min = 40.dp) // Ensures consistent height
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = String.format("$%.2f", comida.precio),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Button(onClick = { onAgregarClick(comida) }) {
                        Text("Añadir")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPantallaMenu() {
    PantallaMenu(onAgregarClick = {})
}