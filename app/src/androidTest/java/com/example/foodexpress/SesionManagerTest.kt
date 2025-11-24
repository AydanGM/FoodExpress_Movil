package com.example.foodexpress

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.foodexpress.model.SesionManager
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SesionManagerTest {

    @Test
    fun guardarSesion_debeAlmacenarCorreo() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val sesionManager = SesionManager(context)

        sesionManager.guardarSesion("test@correo.com")
        assertEquals("test@correo.com", sesionManager.obtenerCorreoSesion())
    }

    @Test
    fun cerrarSesion_debeEliminarCorreo() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val sesionManager = SesionManager(context)

        sesionManager.guardarSesion("test@correo.com")
        sesionManager.cerrarSesion()

        assertNull(sesionManager.obtenerCorreoSesion())
    }
}
