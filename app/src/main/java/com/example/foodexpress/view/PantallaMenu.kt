package com.example.foodexpress.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun PantallaMenu() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("🍕 Pantalla de Menú - Aquí aparecerán los resultados de búsqueda")
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPantallaMenu() {
    PantallaMenu()
}