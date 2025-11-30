package com.example.foodexpress

import com.example.foodexpress.repository.RetrofitInstance
import com.example.foodexpress.repository.UsuarioApi
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class RetrofitInstanceTest {

    @Test
    fun apiDebeInicializarseCorrectamente() {
        val api = RetrofitInstance.api
        assertNotNull(api)
        assertTrue(api is UsuarioApi)
    }
}
