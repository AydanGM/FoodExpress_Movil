package com.example.foodexpress.view

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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

@Composable
fun PantallaPerfil(
        usuarioViewModel: UsuarioViewModel,
        navController: NavController,
        authViewModel: AuthViewModel
) {
    val usuarioState by usuarioViewModel.uiState.collectAsState()
    val usuarioActual = usuarioState.usuarioActual

    if (usuarioActual != null) {
        PantallaPerfilConectado(
            usuario = usuarioActual,
            // Cierra la sesión del usuario (incluye signout de google) y navega a la pantalla de inicio de sesión.
            onCerrarSesion = {
                usuarioViewModel.cerrarSesion()
                authViewModel.logout()

                val context = navController.context
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(context.getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build()
                val googleSignInClient = GoogleSignIn.getClient(context, gso)

                googleSignInClient.signOut().addOnCompleteListener {
                    navController.navigate("login") {
                        popUpTo(0)
                    }
                }
            }
        )
    } else {
        PantallaPerfilDesconectado(navController = navController)
    }
}

@Composable
fun PantallaPerfilConectado(usuario: Usuario, onCerrarSesion: () -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(vertical = 32.dp)
    ) {
        item {
            Image(
                painter = painterResource(id = R.drawable.foto_perfil),
                contentDescription = "Foto de perfil",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(text = usuario.nombre, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = usuario.email, style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center)

            Spacer(modifier = Modifier.height(32.dp))
        }

        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                TarjetaOpcionPerfil(icono = Icons.Default.ReceiptLong, texto = "Mis Pedidos", onClick = { /*TODO*/ })
                TarjetaOpcionPerfil(icono = Icons.Default.LocationOn, texto = "Mis Direcciones", onClick = { /*TODO*/ })
                TarjetaOpcionPerfil(icono = Icons.Default.ExitToApp, texto = "Cerrar Sesión", esDestructivo = true, onClick = onCerrarSesion)
            }
        }
    }
}

@Composable
fun PantallaPerfilDesconectado(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Inicia sesión para ver tu perfil",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate(DestinosNavegacion.Login.ruta) }) {
            Text("Iniciar Sesión / Registrarse")
        }
    }
}

@Composable
fun TarjetaOpcionPerfil(icono: ImageVector, texto: String, esDestructivo: Boolean = false, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icono,
                contentDescription = texto,
                tint = if (esDestructivo) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = texto,
                style = MaterialTheme.typography.bodyLarge,
                color = if (esDestructivo) MaterialTheme.colorScheme.error else Color.Unspecified
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPantallaPerfilConectado() {
    PantallaPerfilConectado(usuario = Usuario("Mark Zuckerberg", "mark.z@example.com"), onCerrarSesion = {})
}