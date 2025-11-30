package com.example.foodexpress.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class AuthStateTest {

    @Test
    fun estadoInicialDebeSerNoAutenticado() {
        val estado = AuthState()
        assertFalse(estado.isAuthenticated)
        assertEquals("", estado.usuario.correo)
        assertEquals("", estado.usuario.nombre)
        assertEquals("", estado.usuario.password)
        assertEquals("", estado.confirmPassword)
        assertEquals("", estado.mensaje)
        assertFalse(estado.isGoogleAuth)
    }

    @Test
    fun copyDebeActualizarMensaje() {
        val estado = AuthState()
        val nuevoEstado = estado.copy(mensaje = "Hola")
        assertEquals("Hola", nuevoEstado.mensaje)
        assertNotEquals(estado, nuevoEstado)
    }
}

class AuthErroresTest {

    @Test
    fun erroresInicialesSonNulos() {
        val errores = AuthErrores()
        assertNull(errores.nombre)
        assertNull(errores.correo)
        assertNull(errores.password)
        assertNull(errores.confirmPassword)
        assertNull(errores.general)
    }

    @Test
    fun copyDebeActualizarCorreo() {
        val errores = AuthErrores()
        val nuevosErrores = errores.copy(correo = "Correo inválido")
        assertEquals("Correo inválido", nuevosErrores.correo)
        assertNotEquals(errores, nuevosErrores)
    }
}
