package com.example.foodexpress.view

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.foodexpress.view.componentes.CampoTextoValidado
import com.example.foodexpress.viewModel.AuthViewModel
import com.example.foodexpress.viewModel.UsuarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaRegistro(
    navController: NavController,
    authViewModel: AuthViewModel,
    usuarioViewModel: UsuarioViewModel
) {
    val authState by authViewModel.authState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Crear Cuenta") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(32.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Formulario
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CampoTextoValidado(
                    valor = authState.usuario.nombre,
                    onValorChange = authViewModel::onNombreChange,
                    label = "Nombre completo",
                    esError = authState.errores.nombre != null,
                    mensajeError = authState.errores.nombre
                )

                CampoTextoValidado(
                    valor = authState.usuario.correo,
                    onValorChange = authViewModel::onCorreoChange,
                    label = "Correo electrónico",
                    esError = authState.errores.correo != null,
                    mensajeError = authState.errores.correo,
                    keyboardType = androidx.compose.ui.text.input.KeyboardType.Email
                )

                CampoTextoValidado(
                    valor = authState.usuario.password,
                    onValorChange = authViewModel::onPasswordChange,
                    label = "Contraseña",
                    esError = authState.errores.password != null,
                    mensajeError = authState.errores.password,
                    esPassword = true
                )

                CampoTextoValidado(
                    valor = authState.confirmPassword,
                    onValorChange = authViewModel :: onConfirmPasswordChange,
                    label = "Confirmar contraseña",
                    esError = authState.errores.confirmPassword != null,
                    mensajeError = authState.errores.confirmPassword,
                    esPassword = true
                )

                AnimatedContent(targetState = authState.isLoading) { loading ->
                    Button(
                        onClick = { authViewModel.registrar() },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !loading
                    ) {
                        if (loading) {
                            CircularProgressIndicator()
                        } else {
                            Text("Registrarse")
                        }
                    }
                }


                TextButton(
                    onClick = { navController.navigate("login") }
                ) {
                    Text("¿Ya tienes una cuenta? Iniciar sesión")
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        authViewModel.limpiarEstado()
    }

    LaunchedEffect(authState.isAuthenticated) {
        if (authState.isAuthenticated) {
            usuarioViewModel.iniciarSesion(authState.usuario.nombre, authState.usuario.correo)
            authViewModel.limpiarMensaje()
            navController.navigate("login") {
                popUpTo("registro") { inclusive = true }
            }
        }
    }
}