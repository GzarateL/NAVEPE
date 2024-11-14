package com.weissoft.appnavepe.sensores

import RetrofitClient
import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.weissoft.appnavepe.R
import com.weissoft.appnavepe.room.CarProfileRepository
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(navController: NavController, repository: CarProfileRepository, context: Context) {
    val initialPosition = LatLng(-12.0, -77.0)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(initialPosition, 15f)
    }

    val carNickname = remember { mutableStateOf("Tu auto está aquí") }
    var addressText by remember { mutableStateOf("Cargando dirección...") }
    val coroutineScope = rememberCoroutineScope()
    var lastLocation by remember { mutableStateOf<LatLng?>(null) }

    LaunchedEffect(Unit) {
        val profile = repository.getProfile()
        if (profile != null) {
            carNickname.value = "${profile.carNickname} está aquí"
        }
    }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                val gpsDataList = RetrofitClient.apiService.getAllSensorData()

                if (gpsDataList.isNotEmpty()) {
                    val lastData = gpsDataList.last()
                    val newLocation = LatLng(lastData.latitude, lastData.longitude)
                    lastLocation = newLocation
                    cameraPositionState.position = CameraPosition.fromLatLngZoom(newLocation, 15f)
                    fetchAddressFromLocation(context, newLocation) { address ->
                        addressText = address
                    }
                } else {
                    addressText = "No se encontraron datos de GPS"
                }
            } catch (e: Exception) {
                addressText = "Error obteniendo datos de GPS"
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("UBICACIÓN DEL AUTO", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(painter = painterResource(id = R.drawable.ic_back), contentDescription = "Back")
                    }
                }
            )
        },
        content = { paddingValues ->
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState
                ) {
                    // Agrega el marcador en la última ubicación obtenida con el ícono personalizado
                    lastLocation?.let { location ->
                        Marker(
                            state = MarkerState(position = location),
                            title = carNickname.value,
                            snippet = addressText,
                            icon = BitmapDescriptorFactory.fromResource(R.drawable.custom_marker) // Ícono personalizado
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                        .fillMaxWidth()
                        .background(Color.White, shape = RoundedCornerShape(12.dp))
                        .border(BorderStroke(2.dp, Color.Black), shape = RoundedCornerShape(12.dp))
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = carNickname.value,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = addressText,
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        IconButton(onClick = { navController.navigate("cameraScreen") }) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_cam),
                                contentDescription = "Videollamada"
                            )
                        }

                        IconButton(onClick = { makeCallToPolice(context) }) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_call),
                                contentDescription = "Llamada al 105"
                            )
                        }
                    }
                }
            }
        }
    )
}

// Función para obtener la dirección a partir de latitud y longitud
fun fetchAddressFromLocation(context: Context, location: LatLng, onResult: (String) -> Unit) {
    val geocoder = Geocoder(context, Locale.getDefault())
    try {
        val addressList = geocoder.getFromLocation(location.latitude, location.longitude, 1)
        if (addressList.isNullOrEmpty()) {
            onResult("Dirección no encontrada")
        } else {
            onResult(addressList[0].getAddressLine(0))
        }
    } catch (e: Exception) {
        onResult("Error al obtener dirección")
    }
}

// Función para realizar la llamada al 105
fun makeCallToPolice(context: Context) {
    val intent = Intent(Intent.ACTION_CALL)
    intent.data = Uri.parse("tel:105")
    context.startActivity(intent)
}
