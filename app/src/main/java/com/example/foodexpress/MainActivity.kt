package com.example.foodexpress

// Importa las clases necesarias del framework de Android y de las librerías utilizadas.
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

/**
 * MainActivity es la única actividad de la aplicación y el punto de entrada principal.
 * Hereda de `ComponentActivity`, que está diseñada para funcionar con Jetpack Compose.
 */
class MainActivity : ComponentActivity() {
    /**
     * `onCreate` es el primer método que se llama cuando se crea la actividad.
     * Aquí es donde se debe realizar toda la inicialización.
     * @param savedInstanceState Si la actividad se está recreando, este Bundle contiene el estado guardado previamente.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        // Llama a la implementación del método `onCreate` de la clase padre. Es obligatorio hacerlo.
        super.onCreate(savedInstanceState)

        // --- INICIALIZACIÓN DE DEPENDENCIAS --- //

        // Obtiene una instancia única (Singleton) de la base de datos Room.
        val db = AppDatabase.getDatabase(applicationContext)
        // Crea una instancia del repositorio, pasándole la base de datos para que pueda acceder a los DAOs.
        val repository = UsuarioRepository(db)
        // Crea una instancia del gestor de sesión, que usará SharedPreferences para persistir datos simples.
        val sesionManager = SesionManager(applicationContext)
        // Crea el ViewModel de autenticación, inyectando el repositorio y el gestor de sesión.
        val authViewModel = AuthViewModel(repository, sesionManager)
        // Crea el ViewModel de usuario, que manejará el estado del usuario en la UI.
        val usuarioViewModel = UsuarioViewModel()

        // --- RESTAURACIÓN DE SESIÓN --- //

        // Lanza una corutina en el `lifecycleScope`. Esta corutina se cancelará automáticamente si la actividad se destruye.
        lifecycleScope.launch {
            // Intenta obtener el correo electrónico de la sesión guardada en SharedPreferences.
            val correoGuardado = sesionManager.obtenerSesion()
            // Si se encontró un correo guardado...
            if (correoGuardado != null) {
                // ...busca al usuario correspondiente en la base de datos Room de forma asíncrona.
                val usuario = db.usuarioDao().obtenerUsuarioPorCorreo(correoGuardado)
                // Si el usuario existe en la base de datos...
                if (usuario != null) {
                    // ...actualiza el estado en `UsuarioViewModel` para que la UI refleje que el usuario está conectado.
                    usuarioViewModel.iniciarSesion(usuario.nombre, usuario.correo)
                    // ...actualiza el estado en `AuthViewModel` para indicar que la autenticación es válida.
                    authViewModel.loginDirecto(usuario)
                }
            }
        }

        // `setContent` establece la interfaz de usuario de la actividad utilizando Jetpack Compose.
        setContent {
            // `FoodExpressTheme` aplica el tema visual (colores, fuentes, etc.) a toda la UI anidada.
            FoodExpressTheme {
                // `NavegacionPrincipal` es el Composable raíz que contiene toda la lógica de navegación y la estructura de la pantalla.
                // Se le pasan los ViewModels para que las diferentes pantallas puedan acceder a la lógica y al estado.
                NavegacionPrincipal(
                    authViewModel = authViewModel,
                    usuarioViewModel = usuarioViewModel
                )
            }
        }
    }
}
