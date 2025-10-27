package com.example.foodexpress.model

/**
 * `AuthState` y `AuthErrores` son clases de datos que representan el estado completo de la interfaz de usuario
 * para las pantallas de autenticación (Login y Registro). Esto sigue el patrón de diseño UDF (Unidirectional Data Flow).
 */

/**
 * Representa el estado completo de la UI para una pantalla de autenticación.
 * Es inmutable; para cambiar el estado, se debe crear una nueva instancia con los valores actualizados.
 * @property usuario Los datos del usuario (nombre, correo, contraseña) que se están introduciendo en los campos de texto.
 * @property confirmPassword El valor del campo de texto "Confirmar Contraseña", usado para validación en el registro.
 * @property isLoading `true` si una operación asíncrona (ej. una llamada de red) está en curso. Se usa para mostrar un indicador de carga.
 * @property isAuthenticated `true` si el usuario se ha autenticado correctamente. Se usa para disparar la navegación a la pantalla principal.
 * @property mensaje Un mensaje de éxito para mostrar al usuario (ej. "Registro exitoso").
 * @property errores Un objeto que contiene todos los posibles mensajes de error de validación para los campos.
 */
data class AuthState (
    val usuario: Usuario = Usuario(),
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val isAuthenticated: Boolean = false,
    val mensaje: String = "",
    val errores: AuthErrores = AuthErrores()
)

/**
 * Contiene los mensajes de error de validación para cada campo de entrada del formulario de autenticación.
 * Los campos son `nullable` (pueden ser nulos). Si un campo es nulo, significa que no hay error para ese campo de entrada.
 * @property nombre El mensaje de error para el campo de nombre.
 * @property correo El mensaje de error para el campo de correo electrónico.
 * @property password El mensaje de error para el campo de contraseña.
 * @property confirmPassword El mensaje de error para el campo de confirmación de contraseña.
 * @property general Un mensaje de error general que no está asociado a ningún campo específico (ej. "Error de red").
 */
data class AuthErrores (
    val nombre: String? = null,
    val correo: String? = null,
    val password: String? = null,
    val confirmPassword: String? = null,
    val general: String? = null
)