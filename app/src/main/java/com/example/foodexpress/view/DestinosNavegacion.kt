package com.example.foodexpress.view

sealed class DestinosNavegacion(val ruta: String, val titulo: String) {
    object Inicio : DestinosNavegacion("inicio", "Inicio")
    object Menu : DestinosNavegacion("menu", "Menú")
    object Restaurantes : DestinosNavegacion("restaurantes", "Restaurantes")
    object Perfil : DestinosNavegacion("perfil", "Perfil")
    object Login : DestinosNavegacion("login", "Login")
    object Registro : DestinosNavegacion("registro", "Registro")

    // Para búsqueda con parámetros
    fun withSearch(query: String): String {
        return "$ruta?search=$query"
    }
}