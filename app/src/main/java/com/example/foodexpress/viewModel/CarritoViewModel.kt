package com.example.foodexpress.viewModel

import androidx.lifecycle.ViewModel
import com.example.foodexpress.view.Comida
import com.example.foodexpress.view.ItemCarrito
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CarritoViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CarritoUiState())
    val uiState: StateFlow<CarritoUiState> = _uiState.asStateFlow()

    fun agregarAlCarrito(comida: Comida) {
        _uiState.update { currentState ->
            val items = currentState.items.toMutableList()
            val itemExistente = items.find { it.id == comida.id }

            if (itemExistente != null) {
                itemExistente.cantidad++
            } else {
                items.add(ItemCarrito(
                    id = comida.id,
                    nombre = comida.nombre,
                    precio = comida.precio,
                    cantidad = 1
                ))
            }
            currentState.copy(items = items, itemAgregado = comida.nombre)
        }
    }

    fun eliminarDelCarrito(item: ItemCarrito) {
        _uiState.update { currentState ->
            val items = currentState.items.toMutableList()
            items.remove(item)
            currentState.copy(items = items)
        }
    }

    fun notificacionMostrada() {
        _uiState.update { it.copy(itemAgregado = null) }
    }
}

data class CarritoUiState(
    val items: List<ItemCarrito> = emptyList(),
    val itemAgregado: String? = null
) {
    val total: Double
        get() = items.sumOf { it.precio * it.cantidad }

    val numeroDeItems: Int
        get() = items.sumOf { it.cantidad }
}