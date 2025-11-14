package com.example.foodexpress.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.android.gms.location.*

@Composable
fun PantallaMapa() {
    val context = LocalContext.current
    var ubicacion by remember { mutableStateOf<LatLng?>(null) }
    val cameraPositionState = rememberCameraPositionState()

    // Pedir permiso de ubicación
    val permisoUbicacion = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { concedido ->
            if (concedido) {
                // Iniciar ubicación en tiempo real
                iniciarUbicacion(context) { nuevaUbicacion ->
                    ubicacion = nuevaUbicacion
                    cameraPositionState.position =
                        CameraPosition.fromLatLngZoom(nuevaUbicacion, 15f)
                }
            } else {
                Toast.makeText(
                    context,
                    "Permiso de ubicación denegado. No se puede mostrar tu posición.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    )

    // Lanzar el permiso al abrir la pantalla
    LaunchedEffect(Unit) {
        permisoUbicacion.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Mapa",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )
        HorizontalDivider(
            Modifier, DividerDefaults.Thickness,
                DividerDefaults.color)
        // Mostrar el mapa
        GoogleMap(
            modifier = androidx.compose.ui.Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            ubicacion?.let {
                Marker(
                    state = MarkerState(position = it),
                    title = "Tu ubicación",
                    snippet = "Aquí estás ahora"
                )
            }
        }
    }
}

// Función que obtiene la ubicación en tiempo real usando FusedLocationProviderClient
@SuppressLint("MissingPermission")
fun iniciarUbicacion(context: Context, onLocationUpdate: (LatLng) -> Unit) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    val request = LocationRequest.Builder(
        Priority.PRIORITY_HIGH_ACCURACY, 5000 // Cada 5 segundos
    ).build()

    fusedLocationClient.requestLocationUpdates(
        request,
        object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let { location ->
                    val latLng = LatLng(location.latitude, location.longitude)
                    onLocationUpdate(latLng)
                }
            }
        },
        Looper.getMainLooper()
    )
}