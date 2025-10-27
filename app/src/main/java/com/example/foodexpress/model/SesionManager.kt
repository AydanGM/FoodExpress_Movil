package com.example.foodexpress.model

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

/**
 * `SesionManager` es una clase de utilidad para gestionar la persistencia de la sesión del usuario.
 * Utiliza `SharedPreferences`, que es el mecanismo de Android para guardar pequeñas cantidades de datos primitivos (clave-valor).
 * Su propósito es recordar quién es el usuario entre diferentes lanzamientos de la aplicación.
 * @param context El contexto de la aplicación, necesario para acceder a SharedPreferences.
 */
class SesionManager(context: Context) {

    /**
     * Obtiene una instancia de SharedPreferences. Los datos guardados aquí son privados para la aplicación.
     * "sesion_usuario" es el nombre del archivo XML donde se guardarán los datos.
     */
    private val prefs: SharedPreferences =
        context.getSharedPreferences("sesion_usuario", Context.MODE_PRIVATE)

    /**
     * `companion object` se usa para definir constantes y métodos que son estáticos para la clase.
     */
    companion object {
        // Define la clave que se usará para guardar y recuperar el correo del usuario.
        // Usar una constante evita errores de tipeo y facilita el mantenimiento.
        private const val KEY_CORREO = "correo"
    }

    /**
     * Guarda el correo electrónico del usuario en SharedPreferences.
     * Esto "inicia" la sesión a nivel local en el dispositivo.
     * @param correo El correo del usuario que ha iniciado sesión.
     */
    fun guardarSesion(correo: String) {
        // `prefs.edit { ... }` es una función de extensión de KTX que simplifica la escritura en SharedPreferences.
        // Automáticamente maneja la apertura, la edición y el guardado (`apply()`).
        prefs.edit { putString(KEY_CORREO, correo) }
    }

    /**
     * Obtiene el correo electrónico del usuario guardado en SharedPreferences.
     * @return El correo del usuario si existe una sesión guardada, o `null` si no hay ninguna.
     */
    fun obtenerSesion(): String? {
        // Lee el valor de tipo String asociado a `KEY_CORREO`.
        // El segundo parámetro, `null`, es el valor que se devuelve si la clave no se encuentra.
        return prefs.getString(KEY_CORREO, null)
    }

    /**
     * Elimina el correo del usuario de SharedPreferences.
     * Esto "cierra" la sesión a nivel local en el dispositivo.
     */
    fun cerrarSesion() {
        // Usa la misma función de extensión `edit` para eliminar una clave.
        prefs.edit { remove(KEY_CORREO) }
    }
}
