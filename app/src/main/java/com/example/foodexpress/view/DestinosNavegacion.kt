package com.example.foodexpress.view

/**
 * `sealed class` que define todas las rutas de navegación de la aplicación.
 * Usar una clase sellada para las rutas ofrece varias ventajas:
 * 1. Autocompletado y seguridad de tipos: El compilador conoce todos los destinos posibles.
 * 2. Centralización: Todas las rutas están en un solo lugar, facilitando su mantenimiento.
 * 3. Evita errores de tipeo al escribir las rutas como strings en diferentes partes del código.
 * @property ruta La cadena de texto (String) que se usa en el `NavHost` para identificar la pantalla.
 * @property titulo Un título legible para el ser humano, útil para usar en la UI (ej. en la barra de navegación inferior).
 */
sealed class DestinosNavegacion(val ruta: String, val titulo: String) {
    // Objeto que representa la pantalla de Inicio.
    object Inicio : DestinosNavegacion("inicio", "Inicio")
    // Objeto que representa la pantalla de Menú.
    object Menu : DestinosNavegacion("menu", "Menú")
    // Objeto que representa la pantalla de Restaurantes.
    object Restaurantes : DestinosNavegacion("restaurantes", "Restaurantes")
    // Objeto que representa la pantalla de Perfil.
    object Perfil : DestinosNavegacion("perfil", "Perfil")
    // Objeto que representa la pantalla de Login.
    object Login : DestinosNavegacion("login", "Login")
    // Objeto que representa la pantalla de Registro.
    object Registro : DestinosNavegacion("registro", "Registro")
    // Objeto que representa la pantalla del Carrito.
    object Carrito : DestinosNavegacion("carrito", "Carrito")
    // Objeto que representa la pantalla del Mapa.
    object Mapa : DestinosNavegacion("mapa", "Mapa")

    /**
     * Función de utilidad para construir una ruta con un parámetro de búsqueda.
     * Esto es útil para la navegación con argumentos.
     * Ejemplo de uso: `DestinosNavegacion.Menu.withSearch("pizza")` devolvería `"menu?search=pizza"`.
     * @param query El término de búsqueda que se pasará como argumento en la URL.
     * @return La ruta completa con el parámetro de consulta.
     */
    fun withSearch(query: String): String {
        // Concatena la ruta base (ej. "menu") con el parámetro de consulta (ej. "?search=pizza").
        return "$ruta?search=$query"
    }
}