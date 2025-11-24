package com.example.foodexpress.view

import android.graphics.Bitmap
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.foodexpress.R
import com.example.foodexpress.model.PerfilManager
import com.example.foodexpress.model.Usuario
import com.example.foodexpress.viewModel.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

@Composable
fun PantallaPerfil(
        navController: NavController,
        authViewModel: AuthViewModel
) {
    val authState by authViewModel.authState.collectAsState()

    if (authState.isAuthenticated && authState.usuario.correo.isNotEmpty()) {
        PantallaPerfilConectado(
            usuario = authState.usuario,
            // Cierra la sesión del usuario (incluye signout de google) y navega a la pantalla de inicio de sesión.
            onCerrarSesion = {
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
    val context = LocalContext.current
    val imagenPerfil = remember { mutableStateOf<ImageBitmap?>(null) }

    // Al iniciar la pantalla, cargar foto guardada si existe
    LaunchedEffect(Unit) {
        PerfilManager.cargarFotoPerfil(context)?.let {
            imagenPerfil.value = it.asImageBitmap()
        }
    }

    // Launcher de camara
    val launcherCamara = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview(),
        onResult = { bitmap: Bitmap? ->
            if (bitmap != null) {
                imagenPerfil.value = bitmap.asImageBitmap()
                PerfilManager.guardarFotoPerfil(context, bitmap)
            }
        }
    )

    // Launcher de galeria
    val launcherGaleria = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, it)
                if(bitmap != null){
                    imagenPerfil.value = bitmap.asImageBitmap()
                    PerfilManager.guardarFotoPerfil(context, bitmap)
                }
            }
        }
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(vertical = 32.dp)
    ) {
        item {
            // Para hacer la imagen de perfil dinamica
            imagenPerfil.value?.let{
                Image(
                    bitmap = it,
                    contentDescription = "Foto de perfil",
                    modifier = Modifier.size(120.dp).clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } ?: Image(
                painter = painterResource(id = R.drawable.foto_perfil),
                contentDescription = "Foto de perfil",
                modifier = Modifier.size(120.dp).clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { launcherCamara.launch(null) }) {
                    Text("Tomar Foto")
                }
                Button(onClick = { launcherGaleria.launch("image/*") }) {
                    Text("Seleccionar Foto")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = usuario.nombre,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = usuario.correo,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))
        }

        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                TarjetaOpcionPerfil(icono = Icons.Default.ReceiptLong, texto = "Mis Pedidos", onClick = { /*TODO*/ })
                TarjetaOpcionPerfil(icono = Icons.Default.LocationOn, texto = "Mis Direcciones", onClick = { /*TODO*/ })
                TarjetaOpcionPerfil(icono = Icons.Default.ExitToApp, texto = "Cerrar Sesión", esDestructivo = true, onClick = {
                    PerfilManager.borrarFotoPerfil(context)
                    onCerrarSesion()
                })
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