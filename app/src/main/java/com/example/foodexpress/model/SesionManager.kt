package com.example.foodexpress.model

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.foodexpress.repository.UsuarioRepository

class SesionManager(
    context: Context,
    private val usuarioRepository: UsuarioRepository
) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("sesion_usuario", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_CORREO = "correo"
    }

    fun guardarSesion(correo: String) {
        prefs.edit { putString(KEY_CORREO, correo) }
    }

    fun cerrarSesion() {
        prefs.edit { remove(KEY_CORREO) }
    }

    suspend fun obtenerUsuarioSesion(): Usuario? {
        val correo = prefs.getString(KEY_CORREO, null)
        return correo?.let { usuarioRepository.obtenerUsuarioPorCorreo(it) }
    }
}
