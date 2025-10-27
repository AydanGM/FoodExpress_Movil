package com.example.foodexpress.viewModel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Clase de datos que representa a un usuario dentro de la capa de la UI.
 * Contiene solo la información necesaria para ser mostrada en pantalla.
 * @param nombre El nombre del usuario.
 * @param email El correo electrónico del usuario.
 */
data class Usuario(
    val nombre: String,
    val email: String
)

/**
 * Representa el estado de la UI relacionado con la sesión del usuario.
 * Es una única clase que contiene toda la información que la UI necesita saber sobre el usuario actual.
 * @property usuarioActual Un objeto `Usuario` con los datos del usuario conectado, o `null` si no hay sesión.
 * @property isAuthenticated Un booleano que es `true` si el usuario está autenticado, y `false` en caso contrario.
 */
data class UsuarioUiState(
    val usuarioActual: Usuario? = null,
    val isAuthenticated: Boolean = false
)

/**
 * ViewModel para gestionar el estado del usuario actual en toda la aplicación.
 * Su responsabilidad es mantener y exponer el estado del usuario (quién está conectado)
 * para que cualquier parte de la UI pueda reaccionar a los cambios de sesión.
 */
class UsuarioViewModel : ViewModel() {

    // `_uiState` es un flujo de estado mutable y privado. Contiene la única fuente de verdad para el estado del usuario.
    // Solo este ViewModel puede modificarlo, garantizando un flujo de datos unidireccional.
    private val _uiState = MutableStateFlow(UsuarioUiState())
    // `uiState` es la versión pública e inmutable de `_uiState`. La UI observa este StateFlow
    // para recomponerse automáticamente cuando el estado del usuario cambia.
    val uiState: StateFlow<UsuarioUiState> = _uiState.asStateFlow()

    /**
     * Actualiza el estado para reflejar que un usuario ha iniciado sesión.
     * @param nombre El nombre del usuario que ha iniciado sesión.
     * @param email El correo del usuario que ha iniciado sesión.
     */
    fun iniciarSesion(nombre: String, email: String) {
        // `update` es una operación atómica y segura para modificar el estado.
        _uiState.update {
            // Crea una copia del estado actual (`it`), modificando solo las propiedades necesarias.
            it.copy(
                usuarioActual = Usuario(nombre, email), // Crea un nuevo objeto Usuario con los datos.
                isAuthenticated = true // Marca la sesión como autenticada.
            )
        }
    }

    /**
     * Actualiza el estado para reflejar que el usuario ha cerrado sesión.
     */
    fun cerrarSesion() {
        _uiState.update {
            it.copy(
                usuarioActual = null, // Elimina los datos del usuario.
                isAuthenticated = false // Marca la sesión como no autenticada.
            )
        }
    }
}
