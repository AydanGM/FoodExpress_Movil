package com.example.foodexpress.view

// Importa las clases y funciones necesarias de las librerías de Jetpack Compose, Material Design y Navegación.
import androidx.compose.foundation.Image // Para mostrar imágenes.
import androidx.compose.foundation.clickable // Para hacer que un Composable sea clicleable.
import androidx.compose.foundation.layout.* // Para componentes de layout como Column, Row, Spacer, etc.
import androidx.compose.foundation.lazy.LazyColumn // Para listas verticales eficientes.
import androidx.compose.foundation.shape.CircleShape // Para dar forma circular a los elementos.
import androidx.compose.material.icons.Icons // Para acceder a los iconos predefinidos de Material Design.
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight // Icono de flecha a la derecha.
import androidx.compose.material.icons.filled.ExitToApp // Icono para la acción de cerrar sesión.
import androidx.compose.material.icons.filled.LocationOn // Icono para la ubicación.
import androidx.compose.material.icons.filled.ReceiptLong // Icono para los pedidos.
import androidx.compose.material3.* // Componentes de Material Design 3 como Card, Text, Button.
import androidx.compose.runtime.Composable // Anotación que marca una función como un componente de UI de Compose.
import androidx.compose.runtime.collectAsState // Para convertir un StateFlow de un ViewModel en un State de Compose.
import androidx.compose.runtime.getValue // Para acceder al valor de un State de Compose.
import androidx.compose.ui.Alignment // Para alinear elementos dentro de un contenedor.
import androidx.compose.ui.Modifier // Para decorar o añadir comportamiento a los Composables.
import androidx.compose.ui.draw.clip // Para recortar la forma de un Composable.
import androidx.compose.ui.graphics.Color // Para definir colores.
import androidx.compose.ui.graphics.vector.ImageVector // Clase base para los iconos vectoriales.
import androidx.compose.ui.layout.ContentScale // Para definir cómo se escala una imagen.
import androidx.compose.ui.res.painterResource // Para cargar imágenes desde los recursos drawable.
import androidx.compose.ui.text.font.FontWeight // Para definir el grosor de la fuente (ej. negrita).
import androidx.compose.ui.text.style.TextAlign // Para alinear el texto (ej. centrado).
import androidx.compose.ui.tooling.preview.Preview // Para previsualizar Composables en el editor.
import androidx.compose.ui.unit.dp // Para definir dimensiones en píxeles independientes de la densidad.
import androidx.navigation.NavController // Para controlar la navegación entre pantallas.
import com.example.foodexpress.R // Clase generada que contiene los IDs de todos los recursos del proyecto.
import com.example.foodexpress.viewModel.AuthViewModel // ViewModel para la lógica de autenticación.
import com.example.foodexpress.viewModel.Usuario // Clase de datos que representa al usuario.
import com.example.foodexpress.viewModel.UsuarioViewModel // ViewModel para el estado del usuario.

/**
 * Composable principal de la pantalla de perfil. Decide qué vista mostrar (conectado o desconectado).
 */
@Composable // Anotación que define esta función como un componente de UI.
fun PantallaPerfil(
        usuarioViewModel: UsuarioViewModel, // Parámetro: El ViewModel que gestiona el estado del usuario.
        navController: NavController, // Parámetro: El controlador para la navegación.
        authViewModel: AuthViewModel // Parámetro: El ViewModel para la lógica de autenticación.
) {
    // Se suscribe al estado de la UI del `usuarioViewModel` y lo convierte en un State de Compose.
    val usuarioState by usuarioViewModel.uiState.collectAsState()
    // Obtiene el objeto del usuario actual desde el estado para un acceso más fácil.
    val usuarioActual = usuarioState.usuarioActual

    // Comprueba si hay un usuario actualmente conectado.
    if (usuarioActual != null) {
        // Si hay un usuario, muestra la pantalla de perfil para un usuario conectado.
        PantallaPerfilConectado(
            usuario = usuarioActual, // Pasa el objeto del usuario.
            onCerrarSesion = { // Define la acción a ejecutar cuando se pulsa "Cerrar Sesión".
                usuarioViewModel.cerrarSesion() // Limpia el estado del usuario en el `usuarioViewModel`.
                authViewModel.logout() // Cierra la sesión en el `authViewModel` (ej. en Firebase y SharedPreferences).
                navController.navigate("login") { // Navega a la pantalla de login.
                    popUpTo(0) // Limpia toda la pila de navegación para que el usuario no pueda volver atrás.
                }
            }
        )
    } else {
        // Si no hay un usuario, muestra la pantalla que invita a iniciar sesión.
        PantallaPerfilDesconectado(navController = navController)
    }
}

/**
 * Muestra la UI para un usuario que ha iniciado sesión.
 */
@Composable // Anotación de Composable.
fun PantallaPerfilConectado(usuario: Usuario, onCerrarSesion: () -> Unit) {
    // `LazyColumn` es una lista vertical eficiente que solo compone y dibuja los ítems visibles en pantalla.
    LazyColumn(
        modifier = Modifier.fillMaxSize(), // El modificador hace que la columna ocupe todo el espacio disponible.
        horizontalAlignment = Alignment.CenterHorizontally, // Centra a todos sus hijos horizontalmente.
        contentPadding = PaddingValues(vertical = 32.dp) // Añade un espacio vertical en la parte superior e inferior del contenido.
    ) {
        // `item` define un único elemento dentro de la lista `LazyColumn`.
        item {
            // Foto de perfil.
            Image(
                painter = painterResource(id = R.drawable.foto_perfil), // Carga la imagen desde los recursos drawable.
                contentDescription = "Foto de perfil", // Texto descriptivo para accesibilidad.
                modifier = Modifier // Modificador para estilizar la imagen.
                    .size(120.dp) // Establece un tamaño fijo de 120x120 dp.
                    .clip(CircleShape), // Recorta la imagen con una forma circular.
                contentScale = ContentScale.Crop // Escala la imagen para que llene el espacio, recortando el exceso.
            )
            // `Spacer` crea un espacio vertical vacío para separar elementos.
            Spacer(modifier = Modifier.height(16.dp))

            // Nombre y correo.
            Text(text = usuario.nombre, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = usuario.email, style = MaterialTheme.typography.bodyLarge)

            // Espacio más grande para separar la información del usuario de las opciones.
            Spacer(modifier = Modifier.height(32.dp))
        }

        // Otro `item` en la lista para las opciones del perfil.
        item {
            // Opciones del perfil.
            Column(modifier = Modifier.padding(horizontal = 16.dp)) { // Añade padding a los lados.
                // Llama al composable reutilizable `TarjetaOpcionPerfil` para mostrar cada opción del menú.
                TarjetaOpcionPerfil(icono = Icons.Default.ReceiptLong, texto = "Mis Pedidos", onClick = { /*TODO*/ })
                TarjetaOpcionPerfil(icono = Icons.Default.LocationOn, texto = "Mis Direcciones", onClick = { /*TODO*/ })
                TarjetaOpcionPerfil(icono = Icons.Default.ExitToApp, texto = "Cerrar Sesión", esDestructivo = true, onClick = onCerrarSesion)
            }
        }
    }
}

/**
 * Muestra la UI para un usuario que no ha iniciado sesión.
 */
@Composable // Anotación de Composable.
fun PantallaPerfilDesconectado(navController: NavController) {
    // `Column` para organizar los elementos verticalmente.
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp), // Ocupa toda la pantalla y añade un padding general.
        horizontalAlignment = Alignment.CenterHorizontally, // Centra los elementos horizontalmente.
        verticalArrangement = Arrangement.Center // Centra los elementos verticalmente.
    ) {
        // Texto que pide al usuario que inicie sesión.
        Text(
            text = "Inicia sesión para ver tu perfil",
            style = MaterialTheme.typography.headlineSmall, // Estilo de texto grande.
            textAlign = TextAlign.Center // Centra el texto si ocupa más de una línea.
        )
        Spacer(modifier = Modifier.height(16.dp))
        // Botón que, al ser pulsado, navega a la pantalla de login.
        Button(onClick = { navController.navigate(DestinosNavegacion.Login.ruta) }) {
            Text("Iniciar Sesión / Registrarse")
        }
    }
}

/**
 * Un componente reutilizable para mostrar una fila de opción en el menú de perfil.
 */
@Composable // Anotación de Composable.
fun TarjetaOpcionPerfil(icono: ImageVector, texto: String, esDestructivo: Boolean = false, onClick: () -> Unit) {
    // `Card` es un contenedor de Material Design que muestra contenido y acciones sobre un único tema.
    Card(
        modifier = Modifier
            .fillMaxWidth() // La tarjeta ocupa todo el ancho disponible.
            .padding(vertical = 8.dp) // Espacio vertical arriba y abajo de la tarjeta.
            .clickable(onClick = onClick) // Hace que toda la superficie de la tarjeta sea clicleable.
    ) {
        // `Row` para alinear los elementos (icono, texto, flecha) de forma horizontal.
        Row(
            modifier = Modifier.padding(16.dp), // Padding interno para los elementos de la fila.
            verticalAlignment = Alignment.CenterVertically // Alinea los elementos verticalmente al centro.
        ) {
            // Muestra el icono a la izquierda.
            Icon(
                imageVector = icono, // El icono a mostrar, pasado como parámetro.
                contentDescription = texto, // Texto para accesibilidad.
                // El color del icono es rojo si la acción es destructiva (ej. Cerrar Sesión), o el color primario si no.
                tint = if (esDestructivo) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(16.dp)) // Espacio entre el icono y el texto.
            // Muestra el texto de la opción.
            Text(
                text = texto,
                style = MaterialTheme.typography.bodyLarge,
                // El color del texto también cambia si la acción es destructiva.
                color = if (esDestructivo) MaterialTheme.colorScheme.error else Color.Unspecified
            )
            // `Spacer` con `weight(1f)` ocupa todo el espacio horizontal restante.
            // Esto empuja al siguiente elemento (la flecha) hasta el final de la fila.
            Spacer(modifier = Modifier.weight(1f))
            // Icono de flecha a la derecha para indicar que la fila es una acción navegable.
            Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null)
        }
    }
}

/**
 * Define una previsualización de Composable para `PantallaPerfilConectado`.
 * Esto permite ver el componente en el panel de diseño de Android Studio sin ejecutar la app.
 */
@Preview(showBackground = true) // Anotación para habilitar la previsualización.
@Composable // Anotación de Composable.
fun PreviewPantallaPerfilConectado() {
    // Llama al Composable que se quiere previsualizar, pasándole datos de ejemplo.
    PantallaPerfilConectado(usuario = Usuario("Mark Zuckerberg", "mark.z@example.com"), onCerrarSesion = {})
}
