package com.example.foodexpress.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.foodexpress.R
import com.example.foodexpress.viewModel.AuthViewModel
import com.example.foodexpress.viewModel.Usuario
import com.example.foodexpress.viewModel.UsuarioViewModel

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
            onCerrarSesion = {
                usuarioViewModel.cerrarSesion()
                authViewModel.logout()
                navController.navigate("login") {
                    popUpTo(0)
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
            // Foto de perfil
            Image(
                painter = painterResource(id = R.drawable.foto_perfil), // Reemplazar con imagen real
                contentDescription = "Foto de perfil",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Nombre y correo
            Text(text = usuario.nombre, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = usuario.email, style = MaterialTheme.typography.bodyLarge)

            Spacer(modifier = Modifier.height(32.dp))
        }

        item {
            // Opciones del perfil
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