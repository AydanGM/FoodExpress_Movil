package com.example.foodexpress.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodexpress.model.AuthErrores
import com.example.foodexpress.model.AuthState
import com.example.foodexpress.model.SesionManager
import com.example.foodexpress.model.Usuario
import com.example.foodexpress.repository.UsuarioRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(
    private val usuarioRepository: UsuarioRepository,
    private val sesionManager: SesionManager
) : ViewModel() {

    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    // Actualizacion de campos
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

    // Validaciones para dar feedback inmediato
    private fun validarRegistro(): Boolean {
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

    private fun validarLogin(): Boolean {
        val estado = _authState.value
        val errores = Validaciones.validarLogin(
            estado.usuario.correo,
            estado.usuario.password
        )
        _authState.update { it.copy(errores = errores) }
        return listOfNotNull(
            errores.correo,
                        errores.password
        ).isEmpty()
    }

    fun registrar() {
        viewModelScope.launch {
            _authState.update { it.copy(isLoading = true) }

            try {
                val estado = _authState.value
                if(!validarRegistro()) {
                    _authState.update { it.copy(isLoading = false) }
                    return@launch
                }

                val existe = usuarioRepository.existeUsuario(estado.usuario.correo)

                if(existe) {
                    _authState.update {
                        it.copy(
                            isLoading = false,
                            mensaje = "El correo ya está registrado."
                        )
                    }
                    return@launch
                }
                usuarioRepository.registrarUsuario(estado.usuario)
                delay(1500)

                _authState.update {
                    it.copy(
                        isLoading = false,
                        mensaje = "Registro exitoso. Ahora inicia sesión.",
                        usuario = Usuario(),
                        confirmPassword = ""
                    )
                }
            } catch (e: Exception) {
                _authState.update {
                    it.copy(
                        isLoading = false,
                        mensaje = "Error en el registro: ${e.localizedMessage}"
                    )
                }
            }
        }
    }

    fun login() {
        viewModelScope.launch {
            _authState.update { it.copy(isLoading = true) }

            try {
                val estado = _authState.value
                if (!validarLogin()) {
                    _authState.update { it.copy(isLoading = false) }
                    return@launch
                }

                val usuario = usuarioRepository.login(
                    estado.usuario.correo,
                    estado.usuario.password
                )

                if (usuario != null) {
                    sesionManager.guardarSesion(usuario.correo)
                    delay(1500)
                    loginDirecto(usuario)
                } else {
                    delay(1000)
                    _authState.update {
                        it.copy(
                            isLoading = false,
                            mensaje = "Correo o contraseña incorrectos."
                        )
                    }
                }
            } catch (e: Exception) {
                _authState.update {
                    it.copy(
                        isLoading = false,
                        mensaje = "Error en el login: ${e.localizedMessage}"
                    )
                }
            }
        }
    }

    fun restaurarSesion() {
        viewModelScope.launch {
            val correo = sesionManager.obtenerCorreoSesion()
            if(correo != null) {
                try {
                    val usuario = usuarioRepository.obtenerUsuario(correo)
                    if(usuario != null) {
                        loginDirecto(usuario)
                    }
                } catch (e: Exception) {
                    _authState.update { it.copy(
                        mensaje = "Error al restaurar sesión: ${e.localizedMessage}"
                    ) }
                }
            }
        }
    }

    fun logout() {
        sesionManager.cerrarSesion()
        _authState.value = AuthState(mensaje = "Has cerrado sesión exitosamente.")
    }

    fun loginDirecto(usuario: Usuario) {
        _authState.update {
            it.copy(
                isLoading = false,
                isAuthenticated = true,
                usuario = usuario,
                mensaje = "¡Bienvenido de nuevo, ${usuario.nombre}!"
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
                    usuarioRepository.registrarUsuario(
                        Usuario(
                            correo = correo,
                            nombre = nombre ?: "",
                            password = "" // Vacio porque viene de google
                        )
                    )
                }
                sesionManager.guardarSesion(correo)
                loginDirecto(Usuario(correo = correo, nombre = nombre ?: "", password = ""))
                _authState.update {
                    it.copy(
                        mensaje = "¡Bienvenido con Google, ${nombre ?: "Usuario"}!",
                        isGoogleAuth = true
                    )
                }
            } else {
                _authState.update {
                    it.copy(
                        isLoading = false,
                        errores = it.errores.copy(general = "Error al iniciar sesión con Google")
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

    fun limpiarMensaje() {
        _authState.update { it.copy(mensaje = "") }
    }

    fun limpiarEstado() {
        _authState.value = AuthState()
    }

}