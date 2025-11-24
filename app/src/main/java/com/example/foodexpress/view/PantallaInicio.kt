package com.example.foodexpress.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun PantallaInicio() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Encabezado
        Text(
            text = "\uD83C\uDF55 Bienvenido a Food Express",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Tu plataforma de entrega de comida rápida favorita.",
            style = MaterialTheme.typography.bodyLarge
        )
        HorizontalDivider(
            modifier = Modifier.padding(vertical = 16.dp),
            thickness = DividerDefaults.Thickness,
            color = DividerDefaults.color
        )
        Spacer(modifier = Modifier.height(32.dp))

        // Dietas "disponibles"
        CarruselDietas()

        Spacer(modifier = Modifier.height(32.dp))

        // Promociones
        SeccionPromociones()

        Spacer(modifier = Modifier.height(32.dp))

        // Categorías
        SeccionCategorias()

        Spacer(modifier = Modifier.height(32.dp))

        // Reseñas
        SeccionReviews()
    }
}