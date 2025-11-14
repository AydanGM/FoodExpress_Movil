package com.example.foodexpress.viewModel

import com.example.foodexpress.model.AuthErrores

object Validaciones {

    // Expresiones regulares
    private val nombreRegex = Regex("^[A-Za-zÁÉÍÓÚáéíóúÑñÜü]+(?: [A-Za-zÁÉÍÓÚáéíóúÑñÜü]+)+$")
    private val correoRegex = Regex("^[^\\s@]+@[^\\s@]+\\.[^\\s@]{2,}$", RegexOption.IGNORE_CASE)
    private val passwordRegex = Regex("^(?=.*[A-Z])(?=.*\\d).{8,}$")

    fun validarNombre(nombre: String): String? {
        return if (!nombreRegex.matches(nombre.trim())) {
            "Por favor, ingresa tu nombre y apellido(s)"
        } else null
    }

    fun validarCorreo(correo: String): String? {
        return if (!correoRegex.matches(correo.trim())) {
            "Por favor, ingresa un correo electrónico válido."
        } else null
    }

    fun validarPassword(password: String): String? {
        return if (!passwordRegex.matches(password)) {
            "La contraseña debe tener al menos 8 caracteres, una mayúscula y un número."
        } else null
    }

    fun validarConfirmacion(password: String, confirmPassword: String): String? {
        return if (password != confirmPassword) {
            "Las contraseñas no coinciden."
        } else null
    }

    fun validarLogin(correo: String, password: String): AuthErrores {
        return AuthErrores(
            correo = if (correo.isBlank()) "El correo es obligatorio." else validarCorreo(correo),
            password = if (password.isBlank()) "La contraseña es obligatoria." else null
        )
    }
}
