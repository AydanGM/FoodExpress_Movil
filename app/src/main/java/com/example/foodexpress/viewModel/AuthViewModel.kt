package com.example.foodexpress.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodexpress.model.AuthErrores
import com.example.foodexpress.model.AuthState
import com.example.foodexpress.model.SesionManager
import com.example.foodexpress.model.Usuario
import com.example.foodexpress.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(
    private val usuarioRepository: UsuarioRepository,
    private val sesionManager: SesionManager
) : ViewModel() {

    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> = _authState

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
                val correo = _authState.value.usuario.correo
                val existe = usuarioRepository.existeUsuario(correo)

                if (existe) {
                    _authState.update {
                        it.copy(
                            isLoading = false,
                            mensaje = "El correo ya está registrado.",
                            errores = AuthErrores(general = "El correo ya está registrado.")
                        )
                    }
                } else {
                    val nuevoUsuario = Usuario(
                        nombre = _authState.value.usuario.nombre,
                        correo = _authState.value.usuario.correo,
                        password = _authState.value.usuario.password
                    )
                    usuarioRepository.insertarUsuario(nuevoUsuario)

                    _authState.update {
                        it.copy(
                            isLoading = false,
                            mensaje = "¡Registro exitoso! Ahora puedes iniciar sesión.",
                            errores = AuthErrores()
                        )
                    }
                }
            } else {
                _authState.update {
                    it.copy(
                        isLoading = false,
                        mensaje = "Por favor corrige los errores en el formulario"
                    )
                }
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
                    sesionManager.guardarSesion(usuario.correo)
                    _authState.update {
                        it.copy(
                            isLoading = false,
                            isAuthenticated = true,
                            usuario = usuario,
                            mensaje = "¡Bienvenido de nuevo, ${usuario.nombre}!",
                            errores = AuthErrores()
                        )
                    }
                } else {
                    _authState.update {
                        it.copy(
                            isLoading = false,
                            mensaje = "Correo o contraseña incorrectos.",
                            errores = AuthErrores(general = "Correo o contraseña incorrectos.")
                        )
                    }
                }
            } else {
                _authState.update {
                    it.copy(
                        isLoading = false,
                        mensaje = "Por favor corrige los errores en el formulario."
                    )
                }
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

        _authState.update {
            AuthState(
                mensaje = "Has cerrado sesión exitosamente."
            )
        }
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

}
