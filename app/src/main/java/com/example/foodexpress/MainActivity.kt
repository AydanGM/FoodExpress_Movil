package com.example.foodexpress

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.example.foodexpress.model.AppDatabase
import com.example.foodexpress.model.SesionManager
import com.example.foodexpress.repository.UsuarioRepository
import com.example.foodexpress.ui.theme.FoodExpressTheme
import com.example.foodexpress.view.NavegacionPrincipal
import com.example.foodexpress.viewModel.AuthViewModel
import com.example.foodexpress.viewModel.UsuarioViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = AppDatabase.getDatabase(applicationContext)
        val repository = UsuarioRepository(db)
        val sesionManager = SesionManager(applicationContext)
        val authViewModel = AuthViewModel(repository, sesionManager)
        val usuarioViewModel = UsuarioViewModel()

        // Restaurar sesión si existe
        lifecycleScope.launch {
            val correoGuardado = sesionManager.obtenerSesion()
            if (correoGuardado != null) {
                val usuario = db.usuarioDao().obtenerUsuarioPorCorreo(correoGuardado)
                if (usuario != null) {
                    usuarioViewModel.iniciarSesion(usuario.nombre, usuario.correo)
                    authViewModel.loginDirecto(usuario)
                }
            }
        }


        setContent {
            FoodExpressTheme {
                NavegacionPrincipal(
                    authViewModel = authViewModel,
                    usuarioViewModel = usuarioViewModel
                )
            }
        }
    }
}
