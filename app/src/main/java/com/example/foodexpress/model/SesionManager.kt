package com.example.foodexpress.model

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class SesionManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("sesion_usuario", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_CORREO = "correo"
    }

    fun guardarSesion(correo: String) {
        prefs.edit { putString(KEY_CORREO, correo) }
    }

    fun obtenerSesion(): String? {
        return prefs.getString(KEY_CORREO, null)
    }

    fun cerrarSesion() {
        prefs.edit { remove(KEY_CORREO) }
    }
}
