package com.example.foodexpress.view

import androidx.compose.material3.Icon // Para mostrar iconos vectoriales.
import androidx.compose.material3.NavigationBar // El contenedor de la barra de navegación inferior.
import androidx.compose.material3.NavigationBarItem // Un único ítem (pestaña) dentro de la barra.
import androidx.compose.material3.Text // Para mostrar texto.
import androidx.compose.runtime.Composable // Anotación para marcar la función como un componente de UI.
import androidx.compose.runtime.getValue // Para acceder al valor de un State de Compose.
import androidx.navigation.NavController // El controlador para gestionar la navegación.
import androidx.navigation.compose.currentBackStackEntryAsState // Para obtener la ruta actual de la pila de navegación como un State.

/**
 * Composable que define la barra de navegación inferior de la aplicación.
 * @param navController El controlador de navegación para gestionar los clics en los ítems.
 */
@Composable
fun BottomNavigationBar(navController: NavController) {
    // Lista que define todos los destinos (pantallas) que se mostrarán en la barra de navegación.
    val items = listOf(
        DestinosNavegacion.Inicio,
        DestinosNavegacion.Menu,
        DestinosNavegacion.Restaurantes,
        DestinosNavegacion.Mapa,
        DestinosNavegacion.Perfil
    )

    // `NavigationBar` es el componente de Material 3 que actúa como contenedor para los ítems.
    NavigationBar {
        // `currentBackStackEntryAsState` se suscribe a los cambios en la pila de navegación.
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        // Obtiene la ruta de la pantalla que se está mostrando actualmente.
        val currentRoute = navBackStackEntry?.destination?.route

        // Itera sobre la lista de destinos para crear un `NavigationBarItem` para cada uno.
        items.forEach { item ->
            // `NavigationBarItem` representa un único destino clickeable en la barra.
            NavigationBarItem(
                // El `icon` es el elemento visual principal del ítem.
                icon = {
                    // Aquí usamos un `Text` con emojis como una forma sencilla de representar los iconos.
                    Text(
                        when (item) {
                            is DestinosNavegacion.Inicio -> "🏠"
                            is DestinosNavegacion.Menu -> "🍕"
                            is DestinosNavegacion.Restaurantes -> "🏪"
                            is DestinosNavegacion.Perfil -> "👤"
                            is DestinosNavegacion.Mapa -> "🗺️"
                            else -> "❓" // Un emoji de interrogación como caso por defecto.
                        }
                    )
                },
                // El `label` es el texto que aparece debajo del icono.
                label = { Text(item.titulo) },
                // `selected` es un booleano que determina si el ítem está actualmente seleccionado (resaltado).
                // Se activa si la ruta actual coincide con la ruta de este ítem.
                selected = currentRoute == item.ruta,
                // `onClick` define la acción que se ejecuta cuando el usuario pulsa el ítem.
                onClick = {
                    // Llama al `navigate` del controlador para ir a la ruta del ítem pulsado.
                    navController.navigate(item.ruta) {
                        // `launchSingleTop = true` evita que se creen múltiples copias de la misma pantalla
                        // si el usuario pulsa repetidamente sobre el mismo ítem.
                        launchSingleTop = true
                        // `restoreState = true` asegura que el estado de la pantalla se restaure
                        // al volver a ella (ej. la posición de scroll).
                        restoreState = true
                    }
                }
            )
        }
    }
}
