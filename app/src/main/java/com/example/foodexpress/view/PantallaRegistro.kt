package com.example.foodexpress.view

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.foodexpress.view.componentes.CampoTextoValidado
import com.example.foodexpress.viewModel.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.delay
import com.example.foodexpress.R

@Composable
fun PantallaRegistro(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val authState by authViewModel.authState.collectAsState()
    val scrollState = rememberScrollState()

    // Registro con Google
    val context = LocalContext.current

    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .requestIdToken(context.getString(R.string.default_web_client_id))
        .build()

    val googleSignInClient = GoogleSignIn.getClient(context, gso)

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            val nombre = account.displayName
            val correo = account.email

            authViewModel.loginConGoogle(nombre, correo)
        } catch (e: ApiException) {
            authViewModel.actualizarErrorGeneral("Error en Google Sign-In: ${e.statusCode}")
        }
    }

    LaunchedEffect(Unit) {
        authViewModel.limpiarEstado()
    }

    // Caso de registro manual
    LaunchedEffect(authState.mensaje) {
        if (authState.mensaje == "Registro exitoso. Ahora inicia sesión.") {
            delay(1500)
            navController.navigate("login"){
                popUpTo("registro") { inclusive = true }
            }
        }
    }

    // Caso de registro con google
    LaunchedEffect(authState.isAuthenticated) {
        if (authState.isAuthenticated && authState.isGoogleAuth) {
            delay(1500)
            navController.navigate("inicio") {
                popUpTo("registro") { inclusive = true }
            }
        }
    }

    // Animacion de desplazamiento
    LaunchedEffect(Unit) {
        delay(500)
        scrollState.animateScrollTo(scrollState.maxValue)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Text(text = "🥗", fontSize = 80.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Crea tu cuenta",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Regístrate para comenzar",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(40.dp))

        CampoTextoValidado(
            valor = authState.usuario.nombre,
            onValorChange = authViewModel::onNombreChange,
            label = "Nombre completo",
            esError = authState.errores.nombre != null,
            mensajeError = authState.errores.nombre,
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        CampoTextoValidado(
            valor = authState.usuario.correo,
            onValorChange = authViewModel::onCorreoChange,
            label = "Correo electrónico",
            esError = authState.errores.correo != null,
            mensajeError = authState.errores.correo,
            keyboardType = KeyboardType.Email,
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        CampoTextoValidado(
            valor = authState.usuario.password,
            onValorChange = authViewModel::onPasswordChange,
            label = "Contraseña",
            esError = authState.errores.password != null,
            mensajeError = authState.errores.password,
            esPassword = true,
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        CampoTextoValidado(
            valor = authState.confirmPassword,
            onValorChange = authViewModel::onConfirmPasswordChange,
            label = "Confirmar contraseña",
            esError = authState.errores.confirmPassword != null,
            mensajeError = authState.errores.confirmPassword,
            esPassword = true,
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) }
        )

        Spacer(modifier = Modifier.height(24.dp))

        AnimatedContent(targetState = authState.isLoading) { loading ->
            Button(
                onClick = { authViewModel.registrar() },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                enabled = !loading,
                shape = MaterialTheme.shapes.large
            ) {
                if (loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White
                    )
                } else {
                    Text(
                        "Registrarse",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }

        AnimatedVisibility(visible = authState.mensaje.isNotBlank()) {
            Text(
                text = authState.mensaje,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { navController.navigate("login") }) {
            Text("¿Ya tienes una cuenta? Inicia sesión")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                thickness = DividerDefaults.Thickness,
                color = DividerDefaults.color
            )
            Text("o", modifier = Modifier.padding(horizontal = 8.dp), color = Color.Gray)
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                thickness = DividerDefaults.Thickness,
                color = DividerDefaults.color
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val signInIntent = googleSignInClient.signInIntent
                launcher.launch(signInIntent)
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            )
        ) {
            Text("Acceder con Google")
        }
    }
}