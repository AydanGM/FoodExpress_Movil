package com.example.foodexpress.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.OnConflictStrategy

@Dao
interface UsuarioDao {
    @Query("SELECT * FROM usuarios WHERE correo = :correo AND password = :password")
    suspend fun validarUsuario(correo: String, password: String): Usuario?

    @Query("SELECT * FROM usuarios WHERE correo = :correo")
    suspend fun obtenerUsuarioPorCorreo(correo: String): Usuario?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(usuario: Usuario)

    @Query("SELECT COUNT(*) FROM usuarios WHERE correo = :correo")
    suspend fun existeUsuario(correo: String): Int
}