package com.example.foodexpress

import com.example.foodexpress.model.Usuario
import com.example.foodexpress.model.SesionManager
import com.example.foodexpress.repository.UsuarioRepository
import com.example.foodexpress.viewModel.AuthViewModel
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest : StringSpec({

    "login exitoso debe actualizar authState a autenticado" {
        val mockRepo = mockk<UsuarioRepository>()
        val fakeSesionManager = mockk<SesionManager>(relaxed = true)

        // Simulamos que el repo devuelve un usuario válido
        coEvery { mockRepo.login("test@correo.com", "Abc12345") } returns Usuario(
            correo = "test@correo.com",
            nombre = "Test User",
            password = "Abc12345"
        )

        val viewModel = AuthViewModel(mockRepo, fakeSesionManager)

        runTest {
            // Simulamos que el usuario escribe correo y contraseña
            viewModel.onCorreoChange("test@correo.com")
            viewModel.onPasswordChange("Abc12345")

            viewModel.login()

            // Verificamos que el estado cambió a autenticado
            viewModel.authState.value.isAuthenticated shouldBe true
        }
    }

    "login fallido debe mantener authState en no autenticado" {
        val mockRepo = mockk<UsuarioRepository>()
        val fakeSesionManager = mockk<SesionManager>(relaxed = true)

        // Simulamos que el repo devuelve null (login incorrecto)
        coEvery { mockRepo.login("test@correo.com", "wrongpass") } returns null

        val viewModel = AuthViewModel(mockRepo, fakeSesionManager)

        runTest {
            viewModel.onCorreoChange("test@correo.com")
            viewModel.onPasswordChange("wrongpass")

            viewModel.login()

            // Verificamos que sigue sin autenticarse
            viewModel.authState.value.isAuthenticated shouldBe false
            viewModel.authState.value.mensaje shouldBe "Correo o contraseña incorrectos."
        }
    }

    "logout debe limpiar la sesión y actualizar mensaje" {
        val mockRepo = mockk<UsuarioRepository>()
        val fakeSesionManager = mockk<SesionManager>(relaxed = true)

        val viewModel = AuthViewModel(mockRepo, fakeSesionManager)

        runTest {
            viewModel.logout()
            viewModel.authState.value.isAuthenticated shouldBe false
            viewModel.authState.value.mensaje shouldBe "Has cerrado sesión exitosamente."
        }
    }
})