package com.example.foodexpress.viewModel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class Usuario(
    val nombre: String,
    val email: String
)

data class UsuarioUiState(
    val usuarioActual: Usuario? = null,
    val isAuthenticated: Boolean = false
)

class UsuarioViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(UsuarioUiState())
    val uiState: StateFlow<UsuarioUiState> = _uiState.asStateFlow()

    fun iniciarSesion(nombre: String, email: String) {
        _uiState.update {
            it.copy(
                usuarioActual = Usuario(nombre, email),
                isAuthenticated = true
            )
        }
    }

    fun cerrarSesion() {
        _uiState.update {
            it.copy(
                usuarioActual = null,
                isAuthenticated = false
            )
        }
    }
}
