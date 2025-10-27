package com.example.foodexpress.view

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        DestinosNavegacion.Inicio,
        DestinosNavegacion.Menu,
        DestinosNavegacion.Restaurantes,
        DestinosNavegacion.Mapa,
        DestinosNavegacion.Perfil
    )

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Text(
                        when (item) {
                            is DestinosNavegacion.Inicio -> "🏠"
                            is DestinosNavegacion.Menu -> "🍕"
                            is DestinosNavegacion.Restaurantes -> "🏪"
                            is DestinosNavegacion.Perfil -> "👤"
                            is DestinosNavegacion.Mapa -> "🗺️"
                            else -> "❓"
                        }
                    )
                },
                label = { Text(item.titulo) },
                selected = currentRoute == item.ruta,
                onClick = {
                    navController.navigate(item.ruta) {
                        // Configuración para evitar múltiples copias de la misma pantalla
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}