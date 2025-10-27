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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel para gestionar la lógica y el estado de la autenticación (Registro e Inicio de Sesión).
 * Hereda de `ViewModel` para sobrevivir a cambios de configuración (como rotaciones de pantalla).
 * @param usuarioRepository El repositorio para interactuar con los datos de usuario (en este caso, la base de datos Room).
 * @param sesionManager El gestor para guardar y recuperar la sesión del usuario en el dispositivo.
 */
class AuthViewModel(
    private val usuarioRepository: UsuarioRepository,
    private val sesionManager: SesionManager
) : ViewModel() {

    // `_authState` es un flujo de estado mutable y privado. Contiene el estado completo de la UI de autenticación.
    // Solo el ViewModel puede modificarlo.
    private val _authState = MutableStateFlow(AuthState())
    // `authState` es la versión pública e inmutable de `_authState`. La UI observa este flujo para reaccionar a los cambios de estado.
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    // --- MÉTODOS PARA ACTUALIZAR EL ESTADO DESDE LA UI --- //

    /** Se llama cuando el usuario escribe en el campo de nombre. */
    fun onNombreChange(nombre: String) {
        // `update` es una forma segura de actualizar el estado. Crea una copia del estado actual (`it`),
        // modifica solo lo necesario y emite el nuevo estado.
        _authState.update {
            it.copy(
                usuario = it.usuario.copy(nombre = nombre), // Actualiza el nombre dentro del objeto usuario.
                errores = it.errores.copy(nombre = null) // Limpia el error de este campo al empezar a escribir.
            )
        }
    }

    /** Se llama cuando el usuario escribe en el campo de correo. */
    fun onCorreoChange(correo: String) {
        _authState.update {
            it.copy(
                usuario = it.usuario.copy(correo = correo),
                errores = it.errores.copy(correo = null)
            )
        }
    }

    /** Se llama cuando el usuario escribe en el campo de contraseña. */
    fun onPasswordChange(password: String) {
        _authState.update {
            it.copy(
                usuario = it.usuario.copy(password = password),
                // Limpia el error tanto de la contraseña como de la confirmación, ya que ambas dependen de este campo.
                errores = it.errores.copy(password = null, confirmPassword = null)
            )
        }
    }

    /** Se llama cuando el usuario escribe en el campo de confirmar contraseña. */
    fun onConfirmPasswordChange(confirmPassword: String) {
        _authState.update {
            it.copy(
                confirmPassword = confirmPassword,
                errores = it.errores.copy(confirmPassword = null)
            )
        }
    }

    // --- MÉTODOS DE VALIDACIÓN --- //

    /** Valida los campos del formulario de registro. */
    private fun validarRegistro(): Boolean {
        val estado = _authState.value // Obtiene el valor actual del estado.
        // Llama a las funciones de validación para cada campo y crea un nuevo objeto de errores.
        val errores = AuthErrores(
            nombre = Validaciones.validarNombre(estado.usuario.nombre),
            correo = Validaciones.validarCorreo(estado.usuario.correo),
            password = Validaciones.validarPassword(estado.usuario.password),
            confirmPassword = Validaciones.validarConfirmacion(estado.usuario.password, estado.confirmPassword)
        )

        // Actualiza el estado de la UI con los nuevos errores para que se muestren en pantalla.
        _authState.update { it.copy(errores = errores) }

        // Devuelve `true` si la lista de errores (ignorando los nulos) está vacía, `false` en caso contrario.
        return listOfNotNull(errores.nombre, errores.correo, errores.password, errores.confirmPassword).isEmpty()
    }

    /** Valida los campos del formulario de inicio de sesión. */
    private fun validarLogin(): Boolean {
        val estado = _authState.value
        val errores = Validaciones.validarLogin(estado.usuario.correo, estado.usuario.password)

        _authState.update { it.copy(errores = errores) }

        return listOfNotNull(errores.correo, errores.password).isEmpty()
    }

    // --- MÉTODOS DE LÓGICA DE NEGOCIO --- //

    /** Procesa el flujo completo de registro de un nuevo usuario. */
    fun registrar() {
        // Lanza una corutina en el `viewModelScope`. Se cancelará automáticamente si el ViewModel se destruye.
        viewModelScope.launch {
            _authState.update { it.copy(isLoading = true) } // Pone la UI en estado de carga.

            // Si las validaciones del formulario son exitosas...
            if (validarRegistro()) {
                val correo = _authState.value.usuario.correo
                // Verifica si el usuario ya existe en la base de datos.
                val existe = usuarioRepository.existeUsuario(correo)

                if (existe) {
                    // Si el correo ya existe, actualiza el estado con un mensaje de error.
                    _authState.update {
                        it.copy(
                            isLoading = false,
                            errores = it.errores.copy(general = "El correo ya está registrado.")
                        )
                    }
                } else {
                    // Si no existe, crea el nuevo usuario.
                    val nuevoUsuario = Usuario(
                        nombre = _authState.value.usuario.nombre,
                        correo = _authState.value.usuario.correo,
                        password = _authState.value.usuario.password // En una app real, la contraseña debería estar hasheada.
                    )
                    // Inserta el nuevo usuario en la base de datos a través del repositorio.
                    usuarioRepository.insertarUsuario(nuevoUsuario)
                    // Actualiza el estado para reflejar el éxito del registro.
                    _authState.update {
                        it.copy(
                            isLoading = false,
                            mensaje = "¡Registro exitoso! Ahora puedes iniciar sesión."
                        )
                    }
                }
            } else {
                // Si las validaciones fallan, solo desactiva el estado de carga.
                _authState.update { it.copy(isLoading = false) }
            }
        }
    }

    /** Procesa el flujo completo de inicio de sesión. */
    fun login() {
        viewModelScope.launch {
            _authState.update { it.copy(isLoading = true) }

            if (validarLogin()) {
                val correo = _authState.value.usuario.correo
                val password = _authState.value.usuario.password

                // Intenta validar las credenciales del usuario contra la base de datos.
                val usuario = usuarioRepository.validarUsuario(correo, password)

                if (usuario != null) {
                    // Si las credenciales son correctas, guarda la sesión en el dispositivo.
                    sesionManager.guardarSesion(usuario.correo)
                    // Actualiza el estado para indicar que la autenticación fue exitosa.
                    _authState.update {
                        it.copy(
                            isLoading = false,
                            isAuthenticated = true, // Esto disparará la navegación en la UI.
                            usuario = usuario, // Guarda los datos del usuario autenticado.
                            mensaje = "¡Bienvenido de nuevo, ${usuario.nombre}!"
                        )
                    }
                } else {
                    // Si las credenciales son incorrectas, muestra un error general.
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

    /** Limpia el mensaje de estado (usado después de mostrar un Snackbar, por ejemplo). */
    fun limpiarMensaje() {
        _authState.update { it.copy(mensaje = "") }
    }

    /** Resetea todo el estado de autenticación a sus valores iniciales. */
    fun limpiarEstado() {
        _authState.value = AuthState()
    }

    /** Cierra la sesión del usuario. */
    fun logout() {
        // Elimina la sesión guardada en el dispositivo.
        sesionManager.cerrarSesion()
        // Resetea el estado a uno limpio, pero con un mensaje de cierre de sesión.
        _authState.value = AuthState(mensaje = "Has cerrado sesión exitosamente.")
    }

    /**
     * Inicia sesión directamente sin validaciones. Se usa al restaurar una sesión guardada
     * cuando el usuario abre la aplicación.
     */
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
