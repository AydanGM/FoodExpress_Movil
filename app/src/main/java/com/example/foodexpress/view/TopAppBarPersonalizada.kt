package com.example.foodexpress.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarPersonalizada(
    navController: NavController,
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onSearchSubmit: () -> Unit,
    onProfileClick: () -> Unit,
    onCartClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    CenterAlignedTopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Logo placeholder (luego agregarás imagen real)
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🍕", style = MaterialTheme.typography.titleMedium)
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    "Food Express",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color(0xFFDC2626) // Rojo similar a tu React
        ),
        actions = {
            // Barra de búsqueda
            OutlinedTextField(
                value = searchText,
                onValueChange = onSearchTextChange,
                placeholder = { Text("Buscar...", color = Color.White.copy(alpha = 0.7f)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        onSearchSubmit()
                        focusManager.clearFocus()
                    }
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = Color.White,
                    focusedBorderColor = Color.White.copy(alpha = 0.5f),
                    unfocusedBorderColor = Color.White.copy(alpha = 0.3f)
                ),
                modifier = Modifier
                    .width(200.dp)
                    .height(40.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Icono del carrito
            IconButton(onClick = onCartClick) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Carrito",
                    tint = Color.White
                )
            }

            // Menú de usuario
            var expanded by remember { mutableStateOf(false) }

            Box {
                IconButton(onClick = { expanded = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Menú de usuario",
                        tint = Color.White
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Iniciar sesión") },
                        onClick = {
                            expanded = false
                            navController.navigate(DestinosNavegacion.Login.ruta)
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Registro") },
                        onClick = {
                            expanded = false
                            navController.navigate(DestinosNavegacion.Registro.ruta)
                        }
                    )
                }
            }
        }
    )
}