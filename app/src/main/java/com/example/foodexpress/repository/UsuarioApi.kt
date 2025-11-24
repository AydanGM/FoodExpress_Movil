package com.example.foodexpress.repository

import com.example.foodexpress.model.Usuario
import retrofit2.Response
import retrofit2.http.*
interface UsuarioApi {

    @POST("usuarios")
    suspend fun registrarUsuario(@Body usuario: Usuario): Usuario

    @POST("usuarios/login")
    suspend fun login(@Body request: LoginRequest): Response<Usuario>

    @GET("usuarios/correo/{correo}")
    suspend fun obtenerUsuario(@Path("correo") correo: String): Usuario?

    @GET("usuarios/existe/{correo}")
    suspend fun existeUsuario(@Path("correo") correo: String): Boolean
}