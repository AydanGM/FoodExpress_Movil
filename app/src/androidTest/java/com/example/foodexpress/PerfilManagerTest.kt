package com.example.foodexpress.model

import android.content.Context
import android.graphics.Bitmap
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class PerfilManagerTest {

    private lateinit var context: Context

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        PerfilManager.borrarFotoPerfil(context) // limpiar antes de cada test
    }

    @Test
    fun guardarYCargarFotoPerfilDebeRetornarMismaImagen() {
        val bitmap = Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_8888)
        PerfilManager.guardarFotoPerfil(context, bitmap)

        val cargada = PerfilManager.cargarFotoPerfil(context)
        assertNotNull(cargada)
        assertEquals(bitmap.width, cargada!!.width)
        assertEquals(bitmap.height, cargada.height)
    }

    @Test
    fun borrarFotoPerfilDebeEliminarImagen() {
        val bitmap = Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_8888)
        PerfilManager.guardarFotoPerfil(context, bitmap)

        PerfilManager.borrarFotoPerfil(context)
        val cargada = PerfilManager.cargarFotoPerfil(context)
        assertNull(cargada)
    }
}
