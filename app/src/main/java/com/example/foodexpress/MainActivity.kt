package com.example.foodexpress

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.foodexpress.ui.theme.FoodExpressTheme
import com.example.foodexpress.view.NavegacionPrincipal

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FoodExpressTheme {
                NavegacionPrincipal() // Cambia esto
            }
        }
    }
}