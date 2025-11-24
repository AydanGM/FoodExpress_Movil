package com.example.foodexpress

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.foodexpress.model.SesionManager
import com.example.foodexpress.repository.UsuarioRepository
import com.example.foodexpress.ui.theme.FoodExpressTheme
import com.example.foodexpress.view.NavegacionPrincipal
import com.example.foodexpress.viewModel.AuthViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repository = UsuarioRepository(com.example.foodexpress.repository.RetrofitInstance.api)
        val sesionManager = SesionManager(applicationContext)
        val authViewModel = AuthViewModel(repository, sesionManager)

        authViewModel.restaurarSesion()
        setContent {
            FoodExpressTheme {
                NavegacionPrincipal(
                    authViewModel = authViewModel
                )
            }
        }
    }
}
