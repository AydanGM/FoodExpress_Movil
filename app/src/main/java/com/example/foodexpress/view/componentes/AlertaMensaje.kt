package com.example.foodexpress.view.componentes

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun AlertaMensaje(
    mensaje: String,
    onConfirm: () -> Unit
) {
    if (mensaje.isNotBlank()) {
        AlertDialog(
            onDismissRequest = onConfirm, // Se cierra al hacer clic fuera o con el botón
            title = { Text("Food Express") },
            text = { Text(mensaje) },
            confirmButton = {
                TextButton(onClick = onConfirm) {
                    Text("Aceptar")
                }
            }
        )
    }
}