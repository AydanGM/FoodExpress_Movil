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
import androidx.navigation.NavController
import com.example.foodexpress.view.componentes.AlertaMensaje
import com.example.foodexpress.viewModel.AuthViewModel
import com.example.foodexpress.viewModel.UsuarioViewModel
import kotlinx.coroutines.delay

/**
 * Composable que define la pantalla de Inicio de Sesión.
 * @param navController El controlador para gestionar la navegación (ej. ir a Registro).
 * @param authViewModel El ViewModel que contiene la lógica y el estado de la autenticación.
 * @param usuarioViewModel El ViewModel para actualizar el estado del usuario en la app una vez logueado.
 */
@Composable
fun PantallaLogin(
    navController: NavController,
    authViewModel: AuthViewModel,
    usuarioViewModel: UsuarioViewModel
) {
    // Se suscribe al estado de autenticación del ViewModel. La UI se recompondrá cuando este estado cambie.
    val authState by authViewModel.authState.collectAsState()

    // `LaunchedEffect(Unit)` ejecuta este bloque una sola vez cuando el Composable entra en la composición.
    // Se usa para limpiar el estado del ViewModel y evitar mostrar datos de intentos de login anteriores.
    LaunchedEffect(Unit) {
        authViewModel.limpiarEstado()
    }

    // `LaunchedEffect(authState.isAuthenticated)` ejecuta este bloque cada vez que el estado de `isAuthenticated` cambia.
    LaunchedEffect(authState.isAuthenticated) {
        // Si la autenticación es exitosa...
        if (authState.isAuthenticated) {
            delay(500) // Pequeña demora para que el usuario perciba la transición.
            // Informa al `usuarioViewModel` sobre el nuevo usuario que ha iniciado sesión.
            usuarioViewModel.iniciarSesion(authState.usuario.nombre, authState.usuario.correo)
            // Limpia cualquier mensaje de éxito para no volver a mostrarlo.
            authViewModel.limpiarMensaje()
            // Navega a la pantalla de inicio.
            navController.navigate("inicio") {
                // Limpia la pila de navegación hasta la pantalla de login (inclusive) para que el usuario no pueda "volver atrás".
                popUpTo("login") { inclusive = true }
            }
        }
    }

    // Muestra una alerta si hay un mensaje de error o informativo y el usuario no se ha autenticado todavía.
    if (authState.mensaje.isNotBlank() && !authState.isAuthenticated) {
        AlertaMensaje(
            mensaje = authState.mensaje,
            onConfirm = { authViewModel.limpiarMensaje() } // Al cerrar la alerta, se limpia el mensaje en el ViewModel.
        )
    }

    // `Column` es el contenedor principal que apila los elementos de la UI verticalmente.
    Column(
        modifier = Modifier
            .fillMaxSize() // Ocupa toda la pantalla.
            .padding(32.dp) // Añade un padding general.
            .verticalScroll(rememberScrollState()), // Permite el scroll si el contenido no cabe en la pantalla.
        horizontalAlignment = Alignment.CenterHorizontally // Centra todos los elementos horizontalmente.
    ) {
        Spacer(modifier = Modifier.height(40.dp)) // Espacio vertical.

        // Encabezado de la pantalla.
        Text(text = "🍕", fontSize = 80.sp) // Icono de pizza como elemento decorativo.
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Bienvenido de nuevo",
            style = MaterialTheme.typography.headlineMedium, // Estilo de texto grande.
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Inicia sesión para continuar",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Gray // Color gris para el subtítulo.
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Campo de texto para el correo electrónico.
        OutlinedTextField(
            value = authState.usuario.correo, // El valor del campo viene del estado del ViewModel.
            onValueChange = authViewModel::onCorreoChange, // Al cambiar el texto, se llama a la función del ViewModel.
            label = { Text("Correo electrónico") }, // Etiqueta del campo.
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) }, // Icono a la izquierda.
            isError = authState.errores.correo != null, // El campo se marca en rojo si hay un error.
            modifier = Modifier.fillMaxWidth(), // Ocupa todo el ancho.
            shape = MaterialTheme.shapes.large, // Usa bordes redondeados definidos en el tema.
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email) // Muestra el teclado de tipo email.
        )
        // Muestra el mensaje de error con una animación de visibilidad.
        AnimatedVisibility(visible = authState.errores.correo != null) {
            Text(
                text = authState.errores.correo ?: "", // El texto del error (o vacío si es nulo).
                color = MaterialTheme.colorScheme.error, // Color de error del tema.
                style = MaterialTheme.typography.bodySmall, // Estilo de texto pequeño.
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de texto para la contraseña.
        OutlinedTextField(
            value = authState.usuario.password,
            onValueChange = authViewModel::onPasswordChange,
            label = { Text("Contraseña") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            isError = authState.errores.password != null,
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            visualTransformation = PasswordVisualTransformation() // Oculta el texto de la contraseña con puntos.
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

        // Botón de inicio de sesión con estado de carga.
        AnimatedContent(targetState = authState.isLoading) { loading ->
            Button(
                onClick = { authViewModel.login() }, // Al pulsar, llama a la función de login del ViewModel.
                modifier = Modifier.fillMaxWidth().height(50.dp),
                enabled = !loading, // El botón se deshabilita mientras está cargando.
                shape = MaterialTheme.shapes.large
            ) {
                if (loading) {
                    // Si está cargando, muestra un indicador de progreso circular.
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                } else {
                    // Si no, muestra el texto normal.
                    Text("Iniciar Sesión", fontSize = 16.sp)
                }
            }
        }

        // Botón de texto para navegar a la pantalla de registro.
        TextButton(onClick = { navController.navigate("registro") }) {
            Text("¿No tienes cuenta? Regístrate")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Divisor visual con texto "o".
        Row(verticalAlignment = Alignment.CenterVertically) {
            Divider(modifier = Modifier.weight(1f)) // Una línea que ocupa el espacio sobrante.
            Text("o", modifier = Modifier.padding(horizontal = 8.dp), color = Color.Gray)
            Divider(modifier = Modifier.weight(1f)) // Otra línea.
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botones para inicio de sesión con redes sociales (funcionalidad no implementada).
        Button(onClick = { /* TODO */ }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black)) {
            Text("Continuar con Google")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { /* TODO */ }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1877F2))) {
            Text("Continuar con Facebook")
        }
    }
}
