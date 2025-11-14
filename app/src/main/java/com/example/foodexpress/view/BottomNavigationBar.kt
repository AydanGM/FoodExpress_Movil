package com.example.foodexpress.view

import androidx.compose.material3.Icon // Para mostrar iconos vectoriales.
import androidx.compose.material3.NavigationBar // El contenedor de la barra de navegación inferior.
import androidx.compose.material3.NavigationBarItem // Un único ítem (pestaña) dentro de la barra.
import androidx.compose.material3.Text // Para mostrar texto.
import androidx.compose.runtime.Composable // Anotación para marcar la función como un componente de UI.
import androidx.compose.runtime.getValue // Para acceder al valor de un State de Compose.
import androidx.navigation.NavController // El controlador para gestionar la navegación.
import androidx.navigation.compose.currentBackStackEntryAsState // Para obtener la ruta actual de la pila de navegación como un State.

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
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}
