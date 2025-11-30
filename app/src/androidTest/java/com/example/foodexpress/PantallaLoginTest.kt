package com.example.foodexpress

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.rememberNavController
import com.example.foodexpress.model.Usuario
import com.example.foodexpress.model.SesionManager
import com.example.foodexpress.repository.UsuarioRepository
import com.example.foodexpress.view.PantallaLogin
import com.example.foodexpress.viewModel.AuthViewModel
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

class PantallaLoginTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loginFallidoDebeMostrarMensajeError() {
        val mockRepo = mockk<UsuarioRepository>()
        val fakeSesionManager = mockk<SesionManager>(relaxed = true)
        val viewModel = AuthViewModel(mockRepo, fakeSesionManager)

        coEvery { mockRepo.login("test@correo.com", "Abc12345") } returns null

        composeTestRule.setContent {
            PantallaLogin(navController = rememberNavController(), authViewModel = viewModel)
        }

        composeTestRule.onNodeWithText("Correo electrónico").performTextInput("test@correo.com")
        composeTestRule.onNodeWithText("Contraseña").performTextInput("Abc12345")
        composeTestRule.onNodeWithText("Iniciar Sesión").performClick()

        // Esperamos a que aparezca el mensaje
        composeTestRule.waitUntil(timeoutMillis = 10_000) {
            composeTestRule.onAllNodesWithText("Correo o contraseña incorrectos.").fetchSemanticsNodes().isNotEmpty()
        }

        // Verificamos que al menos un nodo con el mensaje existe
        composeTestRule.onAllNodesWithText("Correo o contraseña incorrectos.", substring = true)
    }


    @Test
    fun loginExitosoDebeMostrarMensajeBienvenida() {
        val mockRepo = mockk<UsuarioRepository>()
        val fakeSesionManager = mockk<SesionManager>(relaxed = true)
        val viewModel = AuthViewModel(mockRepo, fakeSesionManager)

        // Simulamos login exitoso
        coEvery { mockRepo.login("test@correo.com", "Abc12345") } returns Usuario(
            correo = "test@correo.com",
            nombre = "Test User",
            password = "Abc12345"
        )

        composeTestRule.setContent {
            PantallaLogin(navController = rememberNavController(), authViewModel = viewModel)
        }

        // Ingresamos datos correctos
        composeTestRule.onNodeWithText("Correo electrónico").performTextInput("test@correo.com")
        composeTestRule.onNodeWithText("Contraseña").performTextInput("Abc12345")
        composeTestRule.onNodeWithText("Iniciar Sesión").performClick()

        // Esperamos a que aparezca el mensaje de bienvenida
        composeTestRule.waitUntil(timeoutMillis = 10_000) {
            composeTestRule.onAllNodesWithText("¡Bienvenido de nuevo, Test User!").fetchSemanticsNodes().isNotEmpty()
        }

        // Verificamos que aparece el mensaje de bienvenida
        composeTestRule.onNodeWithText("¡Bienvenido de nuevo, Test User!", substring = true).assertExists()
    }
}
