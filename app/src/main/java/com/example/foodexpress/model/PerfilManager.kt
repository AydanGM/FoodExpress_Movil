package com.example.foodexpress.model

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream
import androidx.core.content.edit

object PerfilManager {
    private const val PREFS_NAME = "perfil_usuario"
    private const val KEY_FOTO = "foto_perfil"

    fun guardarFotoPerfil(context: Context, bitmap: Bitmap) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val imagenBase64 = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT)
        prefs.edit { putString(KEY_FOTO, imagenBase64) }
    }

    fun cargarFotoPerfil(context: Context): Bitmap? {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val imagenBase64 = prefs.getString(KEY_FOTO, null)
        return imagenBase64?.let {
            val bytes = Base64.decode(it, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        }
    }

    fun borrarFotoPerfil(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit { remove(KEY_FOTO) }
    }
}
