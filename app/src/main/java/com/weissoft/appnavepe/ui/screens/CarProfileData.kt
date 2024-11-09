package com.weissoft.appnavepe.ui.screens

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.weissoft.appnavepe.R
import com.weissoft.appnavepe.room.CarProfile
import com.weissoft.appnavepe.room.CarProfileRepository
import kotlinx.coroutines.launch

@Composable
fun CarProfileData(navController: NavController, repository: CarProfileRepository) {
    val scope = rememberCoroutineScope()
    var carProfile by remember { mutableStateOf<CarProfile?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            carProfile = repository.getProfile()
        } catch (e: Exception) {
            errorMessage = "Error al cargar el perfil"
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Flecha de retroceso y título en la parte superior
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = "Volver",
                        tint = Color.Black
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "PERFIL DEL AUTO",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            errorMessage?.let { error ->
                Text(text = error, color = Color.Red)
            }

            if (carProfile != null) {
                // Mostrar el apodo del auto
                Text(
                    text = carProfile?.carNickname ?: "",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                // Mostrar la imagen subida o una imagen por defecto
                val imageUri = carProfile?.imageUri
                Image(
                    painter = if (imageUri != null) rememberAsyncImagePainter(model = Uri.parse(imageUri)) else painterResource(R.drawable.car_profile),
                    contentDescription = "Imagen del Auto",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(vertical = 16.dp)
                        .background(Color.LightGray, RoundedCornerShape(8.dp))
                )

                // Detalles del perfil en estilo de etiquetas
                Column(horizontalAlignment = Alignment.Start) {
                    ProfileDetailItem("Nombre del Conductor", carProfile?.driverName)
                    ProfileDetailItem("Modelo de Auto", carProfile?.carModel)
                    ProfileDetailItem("Placa", carProfile?.plate)
                    ProfileDetailItem("Color", carProfile?.color)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Botón para actualizar perfil con borde blanco
                Button(
                    onClick = { navController.navigate("createProfileScreen") }, // Navega a la pantalla de actualización
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                        .border(1.dp, Color.Black, RoundedCornerShape(16.dp))
                ) {
                    Text("ACTUALIZAR PERFIL", color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Botón para eliminar perfil
                Button(
                    onClick = {
                        scope.launch {
                            try {
                                repository.deleteProfile()
                                carProfile = null // Actualizar estado tras eliminación
                                navController.popBackStack()
                            } catch (e: Exception) {
                                errorMessage = "Error al eliminar el perfil"
                            }
                        }
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                ) {
                    Text("ELIMINAR", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            } else {
                // Mostrar mensaje y botón para crear perfil si no hay datos guardados
                Text(
                    text = "¿AUN NO TIENES UN PERFIL?",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Image(
                    painter = painterResource(id = R.drawable.car_profile),
                    contentDescription = "Imagen del Auto",
                    modifier = Modifier.size(200.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { navController.navigate("createProfileScreen") },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                ) {
                    Text(
                        text = "¡CREA TU PERFIL!",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileDetailItem(label: String, detail: String?) {
    Text(
        text = "$label:",
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color.Gray
    )
    Text(
        text = detail ?: "",
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Black,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}
