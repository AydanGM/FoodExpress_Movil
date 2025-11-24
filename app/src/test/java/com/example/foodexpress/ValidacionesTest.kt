package com.example.foodexpress

import com.example.foodexpress.viewModel.Validaciones
import org.junit.Assert.*
import org.junit.Test

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
}