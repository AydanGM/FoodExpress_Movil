package com.example.foodexpress.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class UsuarioTest {

    @Test
    fun usuariosConMismosDatosSonIguales() {
        val u1 = Usuario(correo = "test@correo.com", nombre = "Test", password = "123")
        val u2 = Usuario(correo = "test@correo.com", nombre = "Test", password = "123")

        assertEquals(u1, u2)
    }

    @Test
    fun copyDebeActualizarNombre() {
        val u1 = Usuario(correo = "test@correo.com", nombre = "Test", password = "123")
        val u2 = u1.copy(nombre = "Nuevo")

        assertEquals("Nuevo", u2.nombre)
        assertNotEquals(u1, u2)
    }
}
