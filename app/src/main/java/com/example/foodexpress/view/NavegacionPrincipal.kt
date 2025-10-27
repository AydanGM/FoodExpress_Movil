package com.example.foodexpress.view

import PantallaMapaOffline
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.foodexpress.viewModel.AuthViewModel
import com.example.foodexpress.viewModel.CarritoViewModel
import com.example.foodexpress.viewModel.UsuarioViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch

@Composable
fun NavegacionPrincipal(
    authViewModel: AuthViewModel,
    usuarioViewModel: UsuarioViewModel
) {
    val navController = rememberNavController()
    var searchText by remember { mutableStateOf("") }
    val carritoViewModel: CarritoViewModel = viewModel()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val carritoState by carritoViewModel.uiState.collectAsState()
    val usuarioState by usuarioViewModel.uiState.collectAsState()

    LaunchedEffect(carritoState.itemAgregado) {
        carritoState.itemAgregado?.let {
            scope.launch {
                snackbarHostState.showSnackbar("Se ha añadido $it al carrito")
                carritoViewModel.notificacionMostrada()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBarPersonalizada(
                navController = navController,
                searchText = searchText,
                onSearchTextChange = { searchText = it },
                onSearchSubmit = {
                    navController.navigate("menu?search=$searchText")
                },
                onProfileClick = {
                    navController.navigate(DestinosNavegacion.Perfil.ruta)
                },
                onCartClick = {
                    navController.navigate(DestinosNavegacion.Carrito.ruta)
                },
                onTitleClick = {
                    navController.navigate(DestinosNavegacion.Inicio.ruta) {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                numeroDeItems = carritoState.numeroDeItems,
                usuarioConectado = usuarioState.usuarioActual != null
            )
        },
        bottomBar = {
            BottomNavigationBar(navController = navController)
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) {
                Snackbar(
                    snackbarData = it,
                    shape = RoundedCornerShape(16.dp),
                    containerColor = Color.Black.copy(alpha = 0.7f),
                    contentColor = Color.White
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = DestinosNavegacion.Inicio.ruta,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(DestinosNavegacion.Inicio.ruta) {
                PantallaInicio()
            }
            composable(DestinosNavegacion.Menu.ruta) {
                PantallaMenu(onAgregarClick = { comida ->
                    if (usuarioState.usuarioActual != null) {
                        carritoViewModel.agregarAlCarrito(comida)
                    } else {
                        navController.navigate(DestinosNavegacion.Login.ruta)
                    }
                })
            }
            composable(DestinosNavegacion.Restaurantes.ruta) {
                PantallaRestaurantes()
            }
            composable(DestinosNavegacion.Perfil.ruta) {
                PantallaPerfil(
                    navController = navController,
                    usuarioViewModel = usuarioViewModel,
                    authViewModel = authViewModel
                )
            }
            composable(DestinosNavegacion.Login.ruta) {
                PantallaLogin(
                    navController = navController,
                    authViewModel = authViewModel,
                    usuarioViewModel = usuarioViewModel
                )
            }
            composable(DestinosNavegacion.Registro.ruta) {
                PantallaRegistro(
                    navController = navController,
                    authViewModel = authViewModel,
                    usuarioViewModel = usuarioViewModel
                )
            }
            composable(DestinosNavegacion.Carrito.ruta) {
                PantallaCarrito(carritoViewModel = carritoViewModel)
            }
            composable(DestinosNavegacion.Mapa.ruta) {
                PantallaMapaOffline()
            }
        }
    }
}
