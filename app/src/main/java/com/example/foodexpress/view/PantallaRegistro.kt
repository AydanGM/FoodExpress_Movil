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

/**
 * Composable que define la pantalla de Registro de nuevos usuarios.
 * @param navController El controlador para gestionar la navegación (ej. ir a Login).
 * @param authViewModel El ViewModel que contiene la lógica y el estado del registro.
 * @param usuarioViewModel El ViewModel para gestionar el estado del usuario (aunque no se usa directamente aquí, se pasa por consistencia).
 */
@OptIn(ExperimentalMaterial3Api::class) // Anotación para usar componentes de Material 3 que aún son experimentales.
@Composable
fun PantallaRegistro(
    navController: NavController,
    authViewModel: AuthViewModel,
    usuarioViewModel: UsuarioViewModel
) {
    // Se suscribe al estado de autenticación del ViewModel. La UI se recompondrá cuando este estado cambie.
    val authState by authViewModel.authState.collectAsState()

    // `Scaffold` proporciona la estructura básica de una pantalla de Material Design.
    Scaffold(
        // Define la barra de navegación superior de la pantalla.
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Crear Cuenta") } // Título centrado en la barra superior.
            )
        }
    ) { innerPadding -> // `innerPadding` contiene el espacio ocupado por la TopAppBar.
        // `Column` es el contenedor principal que apila los elementos de la UI verticalmente.
        Column(
            modifier = Modifier
                .padding(innerPadding) // Aplica el padding para que el contenido no se solape con la TopAppBar.
                .fillMaxSize() // Ocupa toda la pantalla.
                .padding(32.dp) // Añade un padding general al contenido.
                .verticalScroll(rememberScrollState()), // Permite el scroll si el contenido no cabe en la pantalla.
            horizontalAlignment = Alignment.CenterHorizontally, // Centra todos los elementos horizontalmente.
            verticalArrangement = Arrangement.Center // Centra el grupo de elementos verticalmente.
        ) {
            // Columna para el formulario.
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp) // Añade un espacio de 16.dp entre cada campo de texto.
            ) {
                // Campo de texto reutilizable para el nombre.
                CampoTextoValidado(
                    valor = authState.usuario.nombre, // El valor del campo viene del estado del ViewModel.
                    onValorChange = authViewModel::onNombreChange, // Al cambiar el texto, se llama a la función del ViewModel.
                    label = "Nombre completo", // Etiqueta del campo.
                    esError = authState.errores.nombre != null, // El campo se marca en rojo si hay un error.
                    mensajeError = authState.errores.nombre // El mensaje de error a mostrar.
                )

                // Campo de texto para el correo electrónico.
                CampoTextoValidado(
                    valor = authState.usuario.correo,
                    onValorChange = authViewModel::onCorreoChange,
                    label = "Correo electrónico",
                    esError = authState.errores.correo != null,
                    mensajeError = authState.errores.correo,
                    keyboardType = androidx.compose.ui.text.input.KeyboardType.Email // Muestra el teclado de tipo email.
                )

                // Campo de texto para la contraseña.
                CampoTextoValidado(
                    valor = authState.usuario.password,
                    onValorChange = authViewModel::onPasswordChange,
                    label = "Contraseña",
                    esError = authState.errores.password != null,
                    mensajeError = authState.errores.password,
                    esPassword = true // Indica al componente que es un campo de contraseña para ocultar el texto.
                )

                // Campo de texto para confirmar la contraseña.
                CampoTextoValidado(
                    valor = authState.confirmPassword,
                    onValorChange = authViewModel::onConfirmPasswordChange,
                    label = "Confirmar contraseña",
                    esError = authState.errores.confirmPassword != null,
                    mensajeError = authState.errores.confirmPassword,
                    esPassword = true
                )

                // Botón de registro con estado de carga animado.
                AnimatedContent(targetState = authState.isLoading) { loading ->
                    Button(
                        onClick = { authViewModel.registrar() }, // Al pulsar, llama a la función de registro del ViewModel.
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !loading // El botón se deshabilita mientras está cargando.
                    ) {
                        if (loading) {
                            // Si está cargando, muestra un indicador de progreso circular.
                            CircularProgressIndicator()
                        } else {
                            // Si no, muestra el texto normal.
                            Text("Registrarse")
                        }
                    }
                }

                // Botón de texto para que el usuario pueda navegar a la pantalla de inicio de sesión si ya tiene una cuenta.
                TextButton(
                    onClick = { navController.navigate("login") }
                ) {
                    Text("¿Ya tienes una cuenta? Iniciar sesión")
                }
            }
        }
    }

    // `LaunchedEffect(Unit)` se ejecuta una sola vez cuando el Composable entra en la composición.
    // Se usa aquí para asegurar que el estado del formulario esté limpio al abrir la pantalla.
    LaunchedEffect(Unit) {
        authViewModel.limpiarEstado()
    }

    // `LaunchedEffect` que se ejecuta cada vez que el estado de `isAuthenticated` cambia.
    // Aunque esta pantalla es para registrarse, podría haber un flujo donde el registro también autentica al usuario.
    LaunchedEffect(authState.isAuthenticated) {
        if (authState.isAuthenticated) {
            // Inicia sesión en el `usuarioViewModel` con los datos del nuevo usuario.
            usuarioViewModel.iniciarSesion(authState.usuario.nombre, authState.usuario.correo)
            // Limpia cualquier mensaje de éxito.
            authViewModel.limpiarMensaje()
            // Navega a la pantalla de login (o podría ser a "inicio" directamente).
            navController.navigate("login") {
                // Limpia la pila de navegación para que el usuario no pueda volver a la pantalla de registro.
                popUpTo("registro") { inclusive = true }
            }
        }
    }
}