package com.example.foodexpress

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.foodexpress.model.SesionManager
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

// JUnit 4
class SesionManagerTest {

    private lateinit var sesionManager: SesionManager
    private lateinit var context: Context

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        sesionManager = SesionManager(context)
        sesionManager.cerrarSesion() // limpiamos antes de cada test
    }

    @Test
    fun guardarSesionDebeAlmacenarCorreo() {
        sesionManager.guardarSesion("test@correo.com")

        val correoGuardado = sesionManager.obtenerCorreoSesion()
        assertNotNull(correoGuardado)
        assertEquals("test@correo.com", correoGuardado)
    }

    @Test
    fun cerrarSesionDebeEliminarCorreo() {
        sesionManager.guardarSesion("test@correo.com")

        sesionManager.cerrarSesion()
        val correoGuardado = sesionManager.obtenerCorreoSesion()
        assertNull(correoGuardado)
    }

    @Test
    fun obtenerCorreoSesionSinSesionDebeRetornarNull() {
        val correoGuardado = sesionManager.obtenerCorreoSesion()
        assertNull(correoGuardado)
    }
}
