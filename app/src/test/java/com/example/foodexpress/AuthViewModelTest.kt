package com.example.foodexpress

import com.example.foodexpress.model.Usuario
import com.example.foodexpress.model.SesionManager
import com.example.foodexpress.repository.UsuarioRepository
import com.example.foodexpress.viewModel.AuthViewModel
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.*

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun loginExitosoDebeAutenticar() = runTest {
        val mockRepo = mockk<UsuarioRepository>()
        val fakeSesionManager = mockk<SesionManager>(relaxed = true)

        coEvery { mockRepo.login("test@correo.com", "Abc12345") } returns Usuario(
            correo = "test@correo.com",
            nombre = "Test User",
            password = "Abc12345"
        )

        val viewModel = AuthViewModel(mockRepo, fakeSesionManager)

        viewModel.onCorreoChange("test@correo.com")
        viewModel.onPasswordChange("Abc12345")
        viewModel.login()

        testDispatcher.scheduler.advanceUntilIdle()

        Assertions.assertTrue(viewModel.authState.value.isAuthenticated)
    }

    @Test
    fun loginFallidoDebeMantenerNoAutenticado() = runTest {
        val mockRepo = mockk<UsuarioRepository>()
        val fakeSesionManager = mockk<SesionManager>(relaxed = true)

        coEvery { mockRepo.login("test@correo.com", "wrongpass") } returns null

        val viewModel = AuthViewModel(mockRepo, fakeSesionManager)

        viewModel.onCorreoChange("test@correo.com")
        viewModel.onPasswordChange("wrongpass")
        viewModel.login()

        testDispatcher.scheduler.advanceUntilIdle()

        Assertions.assertFalse(viewModel.authState.value.isAuthenticated)
        Assertions.assertEquals("Correo o contraseña incorrectos.", viewModel.authState.value.mensaje)
    }

    @Test
    fun logoutDebeLimpiarSesionYMostrarMensaje() = runTest {
        val mockRepo = mockk<UsuarioRepository>()
        val fakeSesionManager = mockk<SesionManager>(relaxed = true)
        val viewModel = AuthViewModel(mockRepo, fakeSesionManager)

        viewModel.logout()
        testDispatcher.scheduler.advanceUntilIdle()

        Assertions.assertFalse(viewModel.authState.value.isAuthenticated)
        Assertions.assertTrue(viewModel.authState.value.mensaje.contains("Has cerrado sesión"))
    }

    @Test
    fun registrarDebeMostrarMensajeExitoso() = runTest {
        val mockRepo = mockk<UsuarioRepository>()
        val fakeSesionManager = mockk<SesionManager>(relaxed = true)
        val viewModel = AuthViewModel(mockRepo, fakeSesionManager)

        coEvery { mockRepo.existeUsuario(any()) } returns false
        coJustRun { mockRepo.registrarUsuario(any()) }

        viewModel.onNombreChange("Test User")
        viewModel.onCorreoChange("test@correo.com")
        viewModel.onPasswordChange("Abc12345")
        viewModel.onConfirmPasswordChange("Abc12345")

        viewModel.registrar()
        advanceUntilIdle()

        Assertions.assertTrue(viewModel.authState.value.mensaje.contains("Registro exitoso"))
    }

    @Test
    fun loginConGoogleDebeMostrarMensajeBienvenida() = runTest {
        val mockRepo = mockk<UsuarioRepository>()
        val fakeSesionManager = mockk<SesionManager>(relaxed = true)
        val viewModel = AuthViewModel(mockRepo, fakeSesionManager)

        coEvery { mockRepo.existeUsuario("google@correo.com") } returns false
        coJustRun { mockRepo.registrarUsuario(any()) }

        viewModel.loginConGoogle(nombre = "Google User", correo = "google@correo.com")
        advanceUntilIdle()

        Assertions.assertTrue(viewModel.authState.value.mensaje.contains("¡Bienvenido con Google"))
        Assertions.assertTrue(viewModel.authState.value.isGoogleAuth)
    }

    @Test
    fun actualizarErrorGeneralDebeSetearError() {
        val mockRepo = mockk<UsuarioRepository>()
        val fakeSesionManager = mockk<SesionManager>(relaxed = true)
        val viewModel = AuthViewModel(mockRepo, fakeSesionManager)

        viewModel.actualizarErrorGeneral("Error general")

        Assertions.assertEquals("Error general", viewModel.authState.value.errores.general)
    }

    @Test
    fun restaurarSesionDebeAutenticarUsuarioExistente() = runTest {
        val mockRepo = mockk<UsuarioRepository>()
        val fakeSesionManager = mockk<SesionManager>(relaxed = true)

        // Simulamos que hay un correo guardado en sesión
        every { fakeSesionManager.obtenerCorreoSesion() } returns "test@correo.com"

        // Simulamos que el repositorio devuelve un usuario válido
        coEvery { mockRepo.obtenerUsuario("test@correo.com") } returns Usuario(
            correo = "test@correo.com",
            nombre = "Test User",
            password = "Abc12345"
        )

        val viewModel = AuthViewModel(mockRepo, fakeSesionManager)

        viewModel.restaurarSesion()
        advanceUntilIdle()

        Assertions.assertTrue(viewModel.authState.value.isAuthenticated)
        Assertions.assertEquals("test@correo.com", viewModel.authState.value.usuario?.correo)
    }

    @Test
    fun restaurarSesionDebeMantenerNoAutenticadoSiNoHayUsuario() = runTest {
        val mockRepo = mockk<UsuarioRepository>()
        val fakeSesionManager = mockk<SesionManager>(relaxed = true)

        every { fakeSesionManager.obtenerCorreoSesion() } returns "test@correo.com"
        coEvery { mockRepo.obtenerUsuario("test@correo.com") } returns null

        val viewModel = AuthViewModel(mockRepo, fakeSesionManager)

        viewModel.restaurarSesion()
        advanceUntilIdle()

        Assertions.assertFalse(viewModel.authState.value.isAuthenticated)
    }

    @Test
    fun limpiarMensajeDebeVaciarElMensaje() {
        val mockRepo = mockk<UsuarioRepository>()
        val fakeSesionManager = mockk<SesionManager>(relaxed = true)
        val viewModel = AuthViewModel(mockRepo, fakeSesionManager)

        viewModel.actualizarErrorGeneral("Error general")
        viewModel.limpiarMensaje()

        Assertions.assertEquals("", viewModel.authState.value.mensaje)
    }

    @Test
    fun limpiarEstadoDebeResetearAuthState() {
        val mockRepo = mockk<UsuarioRepository>()
        val fakeSesionManager = mockk<SesionManager>(relaxed = true)
        val viewModel = AuthViewModel(mockRepo, fakeSesionManager)

        viewModel.onCorreoChange("test@correo.com")
        viewModel.limpiarEstado()

        Assertions.assertFalse(viewModel.authState.value.isAuthenticated)
        Assertions.assertEquals("", viewModel.authState.value.usuario.correo)
    }
}