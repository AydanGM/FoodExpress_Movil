package com.example.foodexpress.view

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.foodexpress.view.componentes.AlertaMensaje
import com.example.foodexpress.viewModel.AuthViewModel
import com.example.foodexpress.viewModel.UsuarioViewModel
import kotlinx.coroutines.delay

@Composable
fun PantallaLogin(
    navController: NavController,
    authViewModel: AuthViewModel,
    usuarioViewModel: UsuarioViewModel
) {
    val authState by authViewModel.authState.collectAsState()

    LaunchedEffect(Unit) {
        authViewModel.limpiarEstado()
    }

    LaunchedEffect(authState.isAuthenticated) {
        if (authState.isAuthenticated) {

            delay(500)
            usuarioViewModel.iniciarSesion(authState.usuario.nombre, authState.usuario.correo)
            authViewModel.limpiarMensaje()
            navController.navigate("inicio") {
                popUpTo("login") { inclusive = true }
            }
        }
    }

    if (authState.mensaje.isNotBlank() && !authState.isAuthenticated) {
        AlertaMensaje(
            mensaje = authState.mensaje,
            onConfirm = { authViewModel.limpiarMensaje() }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        // Encabezado con icono
        Text(text = "🍕", fontSize = 80.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Bienvenido de nuevo",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Inicia sesión para continuar",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Formulario
        OutlinedTextField(
            value = authState.usuario.correo,
            onValueChange = authViewModel::onCorreoChange,
            label = { Text("Correo electrónico") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            isError = authState.errores.correo != null,
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
        AnimatedVisibility(visible = authState.errores.correo != null) {
            Text(
                text = authState.errores.correo ?: "",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp)
            )
        }


        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = authState.usuario.password,
            onValueChange = authViewModel::onPasswordChange,
            label = { Text("Contraseña") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            isError = authState.errores.password != null,
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            visualTransformation = PasswordVisualTransformation()
        )
        AnimatedVisibility(visible = authState.errores.password != null) {
            Text(
                text = authState.errores.password ?: "",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        AnimatedContent(targetState = authState.isLoading) { loading ->
            Button(
                onClick = { authViewModel.login() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = !loading,
                shape = MaterialTheme.shapes.large
            ) {
                if (loading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                } else {
                    Text("Iniciar Sesión", fontSize = 16.sp)
                }
            }
        }

        TextButton(onClick = { navController.navigate("registro") }) {
            Text("¿No tienes cuenta? Regístrate")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Divisor
        Row(verticalAlignment = Alignment.CenterVertically) {
            Divider(modifier = Modifier.weight(1f))
            Text("o", modifier = Modifier.padding(horizontal = 8.dp), color = Color.Gray)
            Divider(modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botones de redes sociales
        Button(onClick = { /* TODO */ }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black)) {
            Text("Continuar con Google")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { /* TODO */ }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1877F2))) {
            Text("Continuar con Facebook")
        }
    }
}