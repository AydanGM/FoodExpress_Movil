package com.example.foodexpress.repository

import com.example.foodexpress.model.Usuario

class UsuarioRepository(private val api: UsuarioApi) {

    suspend fun registrarUsuario(usuario: Usuario): Usuario {
        return api.registrarUsuario(usuario)
    }

    suspend fun login(correo: String, password: String): Usuario? {
        val response = api.login(LoginRequest(correo, password))
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }

    suspend fun obtenerUsuario(correo: String): Usuario? {
        return api.obtenerUsuario(correo)
    }

    suspend fun existeUsuario(correo: String): Boolean {
        return api.existeUsuario(correo)
    }
}