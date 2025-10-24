package com.example.foodexpress.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodexpress.model.AuthErrores
import com.example.foodexpress.model.AuthState
import com.example.foodexpress.model.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> = _authState

    // Datos simulados
    private val usuariosRegistrados = mutableListOf<Usuario>()

    fun onNombreChange(nombre: String) {
        _authState.update { it.copy(
            usuario = it.usuario.copy(nombre = nombre),
            errores = it.errores.copy(nombre = null)
        ) }
    }

    fun onCorreoChange(correo: String) {
        _authState.update { it.copy(
            usuario = it.usuario.copy(correo = correo),
            errores = it.errores.copy(correo = null)
        ) }
    }

    fun onPasswordChange(password: String) {
        _authState.update { it.copy (
            usuario = it.usuario.copy(password = password),
            errores = it.errores.copy(password = null, confirmPassword = null)

        ) }
    }

    fun onConfirmPasswordChange(confirmPassword: String) {
        _authState.update { it.copy(
                confirmPassword = confirmPassword,
                errores = it.errores.copy(confirmPassword = null)
        ) }
    }

    fun validarRegistro(): Boolean {
        val estado = _authState.value
        val errores = AuthErrores(
            nombre = Validaciones.validarNombre(estado.usuario.nombre),
            correo = Validaciones.validarCorreo(estado.usuario.correo),
            password = Validaciones.validarPassword(estado.usuario.password),
            confirmPassword = Validaciones.validarConfirmacion(
                estado.usuario.password,
                estado.confirmPassword
            )
        )

        _authState.update { it.copy(errores = errores) }

        return listOfNotNull(
            errores.nombre,
            errores.correo,
            errores.password,
            errores.confirmPassword
        ).isEmpty()
    }

    fun validarLogin(): Boolean {
        val estado = _authState.value
        val errores = Validaciones.validarLogin(estado.usuario.correo, estado.usuario.password)

        _authState.update { it.copy(errores = errores) }

        return listOfNotNull(
            errores.correo,
            errores.password
        ).isEmpty()
    }

    fun registrar() {
        viewModelScope.launch {
            _authState.update { it.copy(isLoading = true) }

            if (validarRegistro()) {
                val nuevoUsuario = _authState.value.usuario.copy(id = System.currentTimeMillis())
                usuariosRegistrados.add(nuevoUsuario)

                _authState.update {
                    it.copy(
                        isLoading = false,
                        mensaje = "¡Registro exitoso! Ahora puedes iniciar sesión.",
                        errores = AuthErrores()
                    )
                }
            } else {
                _authState.update { it.copy(
                    isLoading = false,
                    mensaje = "Por favor corrige los errores en el formulario"
                ) }
            }
        }
    }

    fun login() {
        viewModelScope.launch {
            _authState.update { it.copy(isLoading = true) }

            if (validarLogin()) {
                val usuarioEncontrado = usuariosRegistrados.find {
                    it.correo == _authState.value.usuario.correo &&
                            it.password == _authState.value.usuario.password
                }

                if (usuarioEncontrado != null) {
                    _authState.update { it.copy(
                        isLoading = false,
                        isAuthenticated = true,
                        mensaje = "¡Bienvenido de nuevo, ${usuarioEncontrado.nombre}!",
                        errores = AuthErrores()
                    ) }
                } else {
                    _authState.update { it.copy(
                        isLoading = false,
                        mensaje = "Correo o contraseña incorrectos.",
                        errores = AuthErrores(general = "Correo o contraseña incorrectos.")
                    ) }
                }
            } else {
                _authState.update { it.copy(
                    isLoading = false,
                    mensaje = "Por favor corrige los errores en el formulario."
                ) }
            }
        }
    }

    fun limpiarMensaje() {
        _authState.update { it.copy(mensaje = "") }
    }

    fun logout() {
        _authState.update {
            AuthState(
                mensaje = "Has cerrado sesión exitosamente."
            )
        }
    }
}