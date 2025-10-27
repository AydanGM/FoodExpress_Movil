package com.example.foodexpress.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey val correo: String = "",
    val nombre: String = "",
    val password: String = ""
)

