package com.weissoft.appnavepe.sensores

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.location.Geocoder
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
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
import com.weissoft.appnavepe.R
import com.weissoft.appnavepe.room.CarProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(navController: NavController, repository: CarProfileRepository, context: Context) {
    val defaultLocation = LatLng(-34.6037, -58.3816) // Ubicación de ejemplo (Buenos Aires)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultLocation, 15f)
    }

    // Estado para almacenar el apodo del vehículo
    val carNickname = remember { mutableStateOf("Tu auto está aquí") }

    // Estado para la dirección seleccionada
    var addressText by remember { mutableStateOf("Presiona en el mapa para obtener la dirección") }

    // Cargar el apodo desde la base de datos Room
    LaunchedEffect(Unit) {
        val profile = repository.getProfile() // Método para obtener el perfil
        if (profile != null) {
            carNickname.value = "${profile.carNickname} está aquí" // Actualiza con el apodo del perfil
        }
    }

    // Geocodificación inversa para obtener la dirección a partir de latitud y longitud
    val coroutineScope = rememberCoroutineScope()
    fun fetchAddressFromLocation(location: LatLng) {
        coroutineScope.launch {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addressList = withContext(Dispatchers.IO) {
                geocoder.getFromLocation(location.latitude, location.longitude, 1)
            }
            addressText = if (addressList.isNullOrEmpty()) {
                "Dirección no encontrada"
            } else {
                addressList[0].getAddressLine(0) // Obtiene la dirección completa
            }
        }
    }

    // Función para realizar la llamada al 105
    fun makeCallToPolice() {
        val intent = Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse("tel:105")
        context.startActivity(intent)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("UBICACIÓN DEL AUTO", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) { // Acción de retroceso
                        Icon(painter = painterResource(id = R.drawable.ic_back), contentDescription = "Back")
                    }
                }
            )
        },
        content = { paddingValues ->
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    onMapClick = { latLng ->
                        fetchAddressFromLocation(latLng) // Llama a la función de geocodificación inversa
                    }
                )

                // Cuadro de información en la parte inferior con borde negro
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                        .fillMaxWidth()
                        .background(Color.White, shape = RoundedCornerShape(12.dp))
                        .border(BorderStroke(2.dp, Color.Black), shape = RoundedCornerShape(12.dp)) // Borde negro
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
                                text = carNickname.value, // Apodo o mensaje predeterminado
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = addressText, // Muestra la dirección obtenida
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }

                        // Espacio entre el texto y los íconos
                        Spacer(modifier = Modifier.width(16.dp))

                        // Botón de videollamada que accede a la cámara
                        IconButton(onClick = { navController.navigate("cameraScreen") }) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_cam), // Icono de videollamada
                                contentDescription = "Videollamada"
                            )
                        }

                        // Botón de llamada al 105 (policía)
                        IconButton(onClick = { makeCallToPolice() }) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_call), // Icono de llamada
                                contentDescription = "Llamada al 105"
                            )
                        }
                    }
                }
            }
        }
    )
}
