package com.example.foodexpress.repository

import com.example.foodexpress.model.AppDatabase
import com.example.foodexpress.model.Usuario

class UsuarioRepository(private val db: AppDatabase) {
    private val usuarioDao = db.usuarioDao()

    suspend fun insertarUsuario(usuario: Usuario) {
        usuarioDao.insertar(usuario)
    }

    suspend fun validarUsuario(correo: String, password: String): Usuario? {
        return usuarioDao.validarUsuario(correo, password)
    }

    suspend fun obtenerUsuarioPorCorreo(correo: String): Usuario? {
        return usuarioDao.obtenerUsuarioPorCorreo(correo)
    }

    suspend fun existeUsuario(correo: String): Boolean {
        return usuarioDao.existeUsuario(correo) > 0
    }
}