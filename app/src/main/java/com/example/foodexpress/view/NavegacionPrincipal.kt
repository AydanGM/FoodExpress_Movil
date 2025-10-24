package com.example.foodexpress.view

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun NavegacionPrincipal() {
    val navController = rememberNavController()
    var searchText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBarPersonalizada(
                navController = navController,
                searchText = searchText,
                onSearchTextChange = { searchText = it },
                onSearchSubmit = {
                    // Navegar a menú con término de búsqueda
                    navController.navigate("menu?search=$searchText")
                },
                onProfileClick = {
                    navController.navigate(DestinosNavegacion.Perfil.ruta)
                },
                onCartClick = {
                    // TODO: Navegar a carrito
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = DestinosNavegacion.Inicio.ruta,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(DestinosNavegacion.Inicio.ruta) {
                PantallaInicio()
            }
            composable(DestinosNavegacion.Menu.ruta) {
                PantallaMenu()
            }
            composable(DestinosNavegacion.Restaurantes.ruta) {
                PantallaRestaurantes()
            }
            composable(DestinosNavegacion.Perfil.ruta) {
                PantallaPerfil()
            }
            composable(DestinosNavegacion.Login.ruta) {
                PantallaLogin(navController)
            }
            composable(DestinosNavegacion.Registro.ruta){
                PantallaRegistro(navController)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewNavegacionPrincipal() {
    NavegacionPrincipal()
}