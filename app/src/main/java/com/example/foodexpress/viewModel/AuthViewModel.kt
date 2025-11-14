package com.example.foodexpress.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodexpress.model.AuthErrores
import com.example.foodexpress.model.AuthState
import com.example.foodexpress.model.SesionManager
import com.example.foodexpress.model.Usuario
import com.example.foodexpress.repository.UsuarioRepository
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(
    private val usuarioRepository: UsuarioRepository,
    private val sesionManager: SesionManager,
) : ViewModel() {

    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    fun onNombreChange(nombre: String) {
        _authState.update {
            it.copy(
                usuario = it.usuario.copy(nombre = nombre),
                errores = it.errores.copy(nombre = null)
            )
        }
    }

    fun onCorreoChange(correo: String) {
        _authState.update {
            it.copy(
                usuario = it.usuario.copy(correo = correo),
                errores = it.errores.copy(correo = null)
            )
        }
    }

    fun onPasswordChange(password: String) {
        _authState.update {
            it.copy(
                usuario = it.usuario.copy(password = password),
                errores = it.errores.copy(password = null, confirmPassword = null)
            )
        }
    }

    fun onConfirmPasswordChange(confirmPassword: String) {
        _authState.update {
            it.copy(
                confirmPassword = confirmPassword,
                errores = it.errores.copy(confirmPassword = null)
            )
        }
    }

    private fun validarRegistro(): Boolean {
        val estado = _authState.value
        val errores = AuthErrores(
            nombre = Validaciones.validarNombre(estado.usuario.nombre),
            correo = Validaciones.validarCorreo(estado.usuario.correo),
            password = Validaciones.validarPassword(estado.usuario.password),
            confirmPassword = Validaciones.validarConfirmacion(estado.usuario.password, estado.confirmPassword)
        )

        _authState.update { it.copy(errores = errores) }

        return listOfNotNull(errores.nombre, errores.correo, errores.password, errores.confirmPassword).isEmpty()
    }

    private fun validarLogin(): Boolean {
        val estado = _authState.value
        val errores = Validaciones.validarLogin(estado.usuario.correo, estado.usuario.password)

        _authState.update { it.copy(errores = errores) }

        return listOfNotNull(errores.correo, errores.password).isEmpty()
    }

    fun registrar() {
        viewModelScope.launch {
            _authState.update { it.copy(isLoading = true) }
            if (validarRegistro()) {
                val correo = _authState.value.usuario.correo
                val existe = usuarioRepository.existeUsuario(correo)

                if (existe) {
                    _authState.update {
                        it.copy(
                            isLoading = false,
                            errores = it.errores.copy(general = "El correo ya está registrado.")
                        )
                    }
                } else {
                    val nuevoUsuario = Usuario(
                        nombre = _authState.value.usuario.nombre,
                        correo = _authState.value.usuario.correo,
                        password = _authState.value.usuario.password
                    )
                    usuarioRepository.insertarUsuario(nuevoUsuario)

                    delay(1500)

                    _authState.update {
                        it.copy(
                            isLoading = false,
                            isAuthenticated = false,
                            usuario = Usuario(),
                            confirmPassword = "",
                            isGoogleAuth = false,
                            mensaje = "Registro exitoso. Ahora inicia sesión."
                        )
                    }
                }
            } else {
                _authState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun login() {
        viewModelScope.launch {
            _authState.update { it.copy(isLoading = true) }

            if (validarLogin()) {
                val correo = _authState.value.usuario.correo
                val password = _authState.value.usuario.password

                val usuario = usuarioRepository.validarUsuario(correo, password)

                if (usuario != null) {
                    delay(1500)

                    sesionManager.guardarSesion(usuario.correo)
                    _authState.update {
                        it.copy(
                            isLoading = false,
                            isAuthenticated = true,
                            usuario = usuario,
                            mensaje = "¡Bienvenido de nuevo, ${usuario.nombre}!"
                        )
                    }
                } else {
                    delay(1000)

                    _authState.update {
                        it.copy(
                            isLoading = false,
                            errores = it.errores.copy(general = "Correo o contraseña incorrectos.")
                        )
                    }
                }
            } else {
                _authState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun limpiarMensaje() {
        _authState.update { it.copy(mensaje = "") }
    }

    fun limpiarEstado() {
        _authState.value = AuthState()
    }

    fun logout() {
        sesionManager.cerrarSesion()
        _authState.value = AuthState(mensaje = "Has cerrado sesión exitosamente.")
    }

    fun loginDirecto(usuario: Usuario) {
        _authState.update {
            it.copy(
                isAuthenticated = true,
                usuario = usuario,
                mensaje = "Sesión restaurada automáticamente."
            )
        }
    }

    fun loginConGoogle(nombre: String?, correo: String?) {
        viewModelScope.launch {
            _authState.update { it.copy(isLoading = true) }

            if (correo != null) {
                val existe = usuarioRepository.existeUsuario(correo)

                if (!existe) {
                    // Crear nuevo usuario con datos de Google para registro
                    val nuevoUsuario = Usuario(
                        correo = correo,
                        nombre = nombre ?: "",
                        password = "" // vacío porque viene de Google
                    )
                    usuarioRepository.insertarUsuario(nuevoUsuario)
                }

                sesionManager.guardarSesion(correo)
                _authState.update {
                    it.copy(
                        isLoading = false,
                        isAuthenticated = true,
                        usuario = Usuario(
                            correo = correo,
                            nombre = nombre ?: "",
                            password = ""),
                        mensaje = "¡Bienvenido con Google, ${nombre ?: "Usuario"}!",
                        isGoogleAuth = true
                    )
                }
            } else {
                _authState.update {
                    it.copy(
                        isLoading = false,
                        errores = it.errores.copy(general = "Error al iniciar sesión con Google.")
                    )
                }
            }
        }
    }


    fun actualizarErrorGeneral(mensaje: String) {
        _authState.update {
            it.copy(
                isLoading = false,
                errores = it.errores.copy(general = mensaje)
            )
        }
    }



}

