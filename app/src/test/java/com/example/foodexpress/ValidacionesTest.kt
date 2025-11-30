package com.example.foodexpress

import com.example.foodexpress.viewModel.Validaciones
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

//JUnit 5
class ValidacionesTest {

    @Test
    fun correoInvalidoDebeRetornarError() {
        val error = Validaciones.validarCorreo("correoSinArroba")
        assertNotNull(error)
    }

    @Test
    fun correoValidoDebePasar() {
        val error = Validaciones.validarCorreo("test@correo.com")
        assertNull(error)
    }

    @Test
    fun contrasenaInvalidaDebeRetornarError() {
        val error = Validaciones.validarPassword("abc") // demasiado corta
        assertNotNull(error)
    }

    @Test
    fun contrasenaValidaDebePasar() {
        val error = Validaciones.validarPassword("Abc12345")
        assertNull(error)
    }

    @Test
    fun confirmacionContrasenaIncorrectaDebeRetornarError() {
        val error = Validaciones.validarConfirmacion("Abc12345", "Abc54321")
        assertNotNull(error)
    }

    @Test
    fun confirmacionContrasenaCorrectaDebePasar() {
        val error = Validaciones.validarConfirmacion("Abc12345", "Abc12345")
        assertNull(error)
    }

    @Test
    fun nombreInvalidoDebeRetornarError() {
        val error = Validaciones.validarNombre("Juan") // solo un nombre
        assertEquals("Por favor, ingresa tu nombre y apellido(s)", error)
    }

    @Test
    fun nombreValidoDebePasar() {
        val error = Validaciones.validarNombre("Juan Pérez")
        assertNull(error)
    }

    @Test
    fun loginDebeRetornarErrorSiCorreoVacio() {
        val errores = Validaciones.validarLogin("", "Abc12345")
        assertEquals("El correo es obligatorio.", errores.correo)
        assertNull(errores.password)
    }

    @Test
    fun loginDebeRetornarErrorSiPasswordVacia() {
        val errores = Validaciones.validarLogin("test@correo.com", "")
        assertNull(errores.correo)
        assertEquals("La contraseña es obligatoria.", errores.password)
    }

    @Test
    fun loginDebePasarConCredencialesValidas() {
        val errores = Validaciones.validarLogin("test@correo.com", "Abc12345")
        assertNull(errores.correo)
        assertNull(errores.password)
    }
}