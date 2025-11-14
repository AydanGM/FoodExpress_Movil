package com.example.foodexpress


import android.os.Bundle // Para manejar el estado de la actividad (ej. al rotar la pantalla).
import androidx.activity.ComponentActivity // Clase base para actividades que usan Jetpack Compose.
import androidx.activity.compose.setContent // Función para establecer el contenido de la UI con Compose.
import androidx.lifecycle.lifecycleScope // Proporciona un alcance de corutinas ligado al ciclo de vida de la actividad.
import com.example.foodexpress.model.AppDatabase // Clase que representa la base de datos Room.
import com.example.foodexpress.model.SesionManager // Clase para gestionar la sesión del usuario (guardar/leer de SharedPreferences).
import com.example.foodexpress.repository.UsuarioRepository // Repositorio para abstraer el acceso a los datos del usuario.
import com.example.foodexpress.ui.theme.FoodExpressTheme // El tema personalizado de la aplicación (colores, tipografía).
import com.example.foodexpress.view.NavegacionPrincipal // El Composable que contiene la lógica de navegación principal.
import com.example.foodexpress.viewModel.AuthViewModel // ViewModel para la lógica de autenticación (Firebase).
import com.example.foodexpress.viewModel.UsuarioViewModel // ViewModel para la lógica de datos del usuario (Room).
import kotlinx.coroutines.launch // Función para iniciar una nueva corutina.

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = AppDatabase.getDatabase(applicationContext)
        val repository = UsuarioRepository(db)
        val sesionManager = SesionManager(applicationContext, repository)
        val authViewModel = AuthViewModel(repository, sesionManager)
        val usuarioViewModel = UsuarioViewModel()

        lifecycleScope.launch {
            val usuario = sesionManager.obtenerUsuarioSesion()
            if (usuario != null) {
                usuarioViewModel.iniciarSesion(usuario.nombre, usuario.correo)
                authViewModel.loginDirecto(usuario)
            }
        }

        setContent {
            FoodExpressTheme {
                NavegacionPrincipal(
                    authViewModel = authViewModel,
                    usuarioViewModel = usuarioViewModel
                )
            }
        }
    }
}
