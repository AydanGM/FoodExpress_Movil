package com.example.foodexpress.model

data class AuthState (
    val usuario: Usuario = Usuario(),
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val isAuthenticated: Boolean = false,
    val mensaje: String = "",
    val errores: AuthErrores = AuthErrores(),
    val isGoogleAuth: Boolean = false // Variable para diferenciar LaunchedEffect en pantallaRegistro
)

data class AuthErrores (
    val nombre: String? = null,
    val correo: String? = null,
    val password: String? = null,
    val confirmPassword: String? = null,
    val general: String? = null
)