package com.example.foodexpress.repository

import com.example.foodexpress.model.Usuario
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import retrofit2.Response

class UsuarioRepositoryTest {

    private val mockApi = mockk<UsuarioApi>()
    private val repository = UsuarioRepository(mockApi)

    @Test
    fun registrarUsuarioDebeRetornarUsuario() = runTest {
        val usuario = Usuario(correo = "test@correo.com", nombre = "Test User", password = "Abc12345")

        coEvery { mockApi.registrarUsuario(usuario) } returns usuario

        val result = repository.registrarUsuario(usuario)

        assertEquals(usuario, result)
    }

    @Test
    fun loginExitosoDebeRetornarUsuario() = runTest {
        val usuario = Usuario(correo = "test@correo.com", nombre = "Test User", password = "Abc12345")

        coEvery { mockApi.login(LoginRequest("test@correo.com", "Abc12345")) } returns Response.success(usuario)

        val result = repository.login("test@correo.com", "Abc12345")

        assertNotNull(result)
        assertEquals(usuario, result)
    }

    @Test
    fun loginFallidoDebeRetornarNull() = runTest {
        coEvery { mockApi.login(LoginRequest("test@correo.com", "wrongpass")) } returns Response.error(
            401,
            okhttp3.ResponseBody.create(null, "Unauthorized")
        )

        val result = repository.login("test@correo.com", "wrongpass")

        assertNull(result)
    }

    @Test
    fun obtenerUsuarioDebeRetornarUsuario() = runTest {
        val usuario = Usuario(correo = "test@correo.com", nombre = "Test User", password = "Abc12345")

        coEvery { mockApi.obtenerUsuario("test@correo.com") } returns usuario

        val result = repository.obtenerUsuario("test@correo.com")

        assertEquals(usuario, result)
    }

    @Test
    fun existeUsuarioDebeRetornarTrue() = runTest {
        coEvery { mockApi.existeUsuario("test@correo.com") } returns true

        val result = repository.existeUsuario("test@correo.com")

        assertTrue(result)
    }

    @Test
    fun existeUsuarioDebeRetornarFalse() = runTest {
        coEvery { mockApi.existeUsuario("test@correo.com") } returns false

        val result = repository.existeUsuario("test@correo.com")

        assertFalse(result)
    }
}