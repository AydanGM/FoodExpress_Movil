package com.example.foodexpress.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.foodexpress.view.componentes.AlertaMensaje
import com.example.foodexpress.view.componentes.CampoTextoValidado
import com.example.foodexpress.viewModel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaLogin(
    navController: NavController,
    viewModel: AuthViewModel = viewModel()
) {
    val authState by viewModel.authState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Iniciar Sesión") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Formulario
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CampoTextoValidado(
                    valor = authState.usuario.correo,
                    onValorChange = viewModel::onCorreoChange,
                    label = "Correo electrónico",
                    esError = authState.errores.correo != null,
                    mensajeError = authState.errores.correo,
                    keyboardType = KeyboardType.Email
                )

                CampoTextoValidado(
                    valor = authState.usuario.password,
                    onValorChange = viewModel::onPasswordChange,
                    label = "Contraseña",
                    esError = authState.errores.password != null,
                    mensajeError = authState.errores.password,
                    esPassword = true
                )

                Button(
                    onClick = { viewModel.login() },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !authState.isLoading
                ) {
                    if (authState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Iniciar Sesión")
                    }
                }

                TextButton(
                    onClick = { navController.navigate("registro") }
                ) {
                    Text("¿No tienes una cuenta? Regístrate aquí")
                }
            }
        }
    }

    // Navegación automática después de login exitoso
    LaunchedEffect(authState.isAuthenticated) {
        if (authState.isAuthenticated) {
            viewModel.limpiarMensaje()
            navController.navigate("inicio") {
                popUpTo("login") { inclusive = true }
            }
        }
    }

    // Mostrar alerta para mensajes de error
    if (authState.mensaje.isNotBlank() && !authState.isAuthenticated) {
        AlertaMensaje(
            mensaje = authState.mensaje,
            onConfirm = {
                viewModel.limpiarMensaje()
            }
        )
    }
}