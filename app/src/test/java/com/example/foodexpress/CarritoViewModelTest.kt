package com.example.foodexpress

import com.example.foodexpress.view.Comida
import com.example.foodexpress.view.ItemCarrito
import com.example.foodexpress.viewModel.CarritoUiState
import com.example.foodexpress.viewModel.CarritoViewModel
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class CarritoViewModelTest {

    @Test
    fun agregarComidaNuevaDebeAnadirItem() = runTest {
        val vm = CarritoViewModel()
        val comida = Comida(id = 1, nombre = "Pizza", precio = 10.0, descripcion = "Test", imagen = 0)

        vm.agregarAlCarrito(comida)
        val state = vm.uiState.value

        assertEquals(1, state.items.size)
        assertEquals("Pizza", state.itemAgregado)
        assertEquals(1, state.items[0].cantidad)
    }

    @Test
    fun agregarComidaRepetidaDebeIncrementarCantidad() = runTest {
        val vm = CarritoViewModel()
        val comida = Comida(id = 1, nombre = "Pizza", precio = 10.0, descripcion = "Test", imagen = 0)

        vm.agregarAlCarrito(comida)
        vm.agregarAlCarrito(comida)
        val state = vm.uiState.value

        assertEquals(1, state.items.size)
        assertEquals(2, state.items[0].cantidad)
    }

    @Test
    fun eliminarItemDebeVaciarLista() = runTest {
        val vm = CarritoViewModel()
        val comida = Comida(id = 1, nombre = "Pizza", precio = 10.0, descripcion = "Test", imagen = 0)

        vm.agregarAlCarrito(comida)
        val item = vm.uiState.value.items[0]
        vm.eliminarDelCarrito(item)

        val state = vm.uiState.value
        assertTrue(state.items.isEmpty())
    }

    @Test
    fun notificacionMostradaDebeResetearItemAgregado() = runTest {
        val vm = CarritoViewModel()
        val comida = Comida(id = 1, nombre = "Pizza", precio = 10.0, descripcion = "Test", imagen = 0)

        vm.agregarAlCarrito(comida)
        vm.notificacionMostrada()

        val state = vm.uiState.value
        assertNull(state.itemAgregado)
    }

    @Test
    fun totalYNumeroDeItemsSeCalculanCorrectamente() {
        val items = listOf(
            ItemCarrito(1, "Pizza", 10.0, 2),
            ItemCarrito(2, "Hamburguesa", 5.0, 1)
        )
        val state = CarritoUiState(items = items)

        assertEquals(25.0, state.total) // 2*10 + 1*5
        assertEquals(3, state.numeroDeItems) // 2 + 1
    }
}
