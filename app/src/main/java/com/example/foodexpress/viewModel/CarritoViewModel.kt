package com.example.foodexpress.viewModel

import androidx.lifecycle.ViewModel
import com.example.foodexpress.view.Comida
import com.example.foodexpress.view.ItemCarrito
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * ViewModel para gestionar el estado y la lógica del carrito de compras.
 * Se encarga de añadir, eliminar y calcular los totales de los productos en el carrito.
 */
class CarritoViewModel : ViewModel() {

    // `_uiState` es un flujo de estado mutable y privado que contiene el estado actual del carrito.
    // Solo este ViewModel puede modificarlo.
    private val _uiState = MutableStateFlow(CarritoUiState())
    // `uiState` es la versión pública e inmutable de `_uiState`. La UI observa este flujo
    // para reaccionar a cualquier cambio en el carrito (ej. añadir un ítem, cambiar cantidad, etc.).
    val uiState: StateFlow<CarritoUiState> = _uiState.asStateFlow()

    /**
     * Añade un producto (`Comida`) al carrito.
     * Si el producto ya existe en el carrito, incrementa su cantidad. Si no, lo añade como un nuevo ítem.
     * @param comida El objeto `Comida` que se va to agregar.
     */
    fun agregarAlCarrito(comida: Comida) {
        // `update` garantiza que la modificación del estado sea atómica y segura.
        _uiState.update { currentState ->
            // Crea una copia mutable de la lista actual de ítems para poder modificarla.
            val items = currentState.items.toMutableList()
            // Busca si ya existe un ítem en el carrito con el mismo ID que la comida que se está agregando.
            val itemExistente = items.find { it.id == comida.id }

            if (itemExistente != null) {
                // Si el ítem ya existe, simplemente incrementa su cantidad.
                itemExistente.cantidad++
            } else {
                // Si no existe, crea un nuevo `ItemCarrito` y lo añade a la lista.
                items.add(ItemCarrito(
                    id = comida.id,
                    nombre = comida.nombre,
                    precio = comida.precio,
                    cantidad = 1 // La cantidad inicial siempre es 1.
                ))
            }
            // Crea una copia del estado actual, reemplazando la lista de ítems con la nueva lista actualizada
            // y estableciendo `itemAgregado` con el nombre del producto para mostrar una notificación.
            currentState.copy(items = items, itemAgregado = comida.nombre)
        }
    }

    /**
     * Elimina un ítem completo del carrito.
     * @param item El `ItemCarrito` que se va a eliminar.
     */
    fun eliminarDelCarrito(item: ItemCarrito) {
        _uiState.update { currentState ->
            val items = currentState.items.toMutableList()
            items.remove(item) // Elimina el ítem de la lista.
            currentState.copy(items = items) // Actualiza el estado con la nueva lista.
        }
    }

    /**
     * Resetea el estado `itemAgregado` a nulo.
     * Se llama desde la UI después de que la notificación (Snackbar) de "ítem añadido" ha sido mostrada,
     * para evitar que se muestre de nuevo si hay una recomposición.
     */
    fun notificacionMostrada() {
        _uiState.update { it.copy(itemAgregado = null) }
    }
}

/**
 * Clase de datos que representa el estado completo de la UI para el carrito de compras.
 * @property items La lista de `ItemCarrito` que están actualmente en el carrito.
 * @property itemAgregado El nombre del último ítem que se añadió. Se usa para mostrar notificaciones.
 *                       Es `nullable` porque la mayor parte del tiempo no se está añadiendo nada.
 */
data class CarritoUiState(
    val items: List<ItemCarrito> = emptyList(),
    val itemAgregado: String? = null
) {
    /**
     * Propiedad computada que calcula el precio total del carrito.
     * Se recalcula automáticamente cada vez que se accede a `total` y el estado ha cambiado.
     * @return La suma de (precio * cantidad) para cada ítem en el carrito.
     */
    val total: Double
        get() = items.sumOf { it.precio * it.cantidad }

    /**
     * Propiedad computada que calcula el número total de productos en el carrito.
     * A diferencia de `items.size`, esta suma las cantidades de todos los ítems.
     * @return El número total de unidades en el carrito.
     */
    val numeroDeItems: Int
        get() = items.sumOf { it.cantidad }
}