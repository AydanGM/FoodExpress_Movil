package com.example.foodexpress.view.componentes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun CampoTextoValidado(
    valor: String,
    onValorChange: (String) -> Unit,
    label: String,
    esError: Boolean,
    mensajeError: String?,
    esPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    leadingIcon: @Composable (() -> Unit)? = null
) {
    Column {
        OutlinedTextField(
            value = valor,
            onValueChange = onValorChange,
            label = { Text(label) },
            leadingIcon = leadingIcon,
            isError = esError,
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            visualTransformation = if (esPassword) PasswordVisualTransformation() else VisualTransformation.None,
            singleLine = true
        )
        AnimatedVisibility(visible = esError) {
            Text(
                text = mensajeError ?: "",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}