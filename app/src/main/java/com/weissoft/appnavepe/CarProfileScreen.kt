package com.weissoft.appnavepe.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.weissoft.appnavepe.R
import com.weissoft.appnavepe.room.CarProfile
import com.weissoft.appnavepe.room.CarProfileRepository
import kotlinx.coroutines.launch
import pl.droidsonroids.gif.GifImageView

@Composable
fun CarProfileScreen(navController: NavController, repository: CarProfileRepository) {
    val scope = rememberCoroutineScope()
    var carProfile by remember { mutableStateOf<CarProfile?>(null) }

    // Obtener el perfil desde la base de datos cuando la pantalla se carga
    LaunchedEffect(Unit) {
        carProfile = repository.getProfile()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()) // Habilitar desplazamiento
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

            if (carProfile != null) {
                // Nombre del auto en mayúsculas
                Text(
                    text = carProfile?.carNickname?.uppercase() ?: "",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.align(Alignment.Start)
                )

                // Cargar la imagen desde la URI guardada
                val painter = rememberAsyncImagePainter(model = carProfile?.imageUri)
                Image(
                    painter = painter,
                    contentDescription = "Imagen del Auto",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .align(Alignment.CenterHorizontally)
                )

                // Detalles del perfil en el formato visual deseado
                ProfileDetail(label = "NOMBRE DEL CONDUCTOR", detail = carProfile?.driverName?.uppercase())
                ProfileDetail(label = "MODELO DE AUTO", detail = carProfile?.carModel?.uppercase())
                ProfileDetail(label = "PLACA", detail = carProfile?.plate?.uppercase())
                ProfileDetail(label = "COLOR", detail = carProfile?.color?.uppercase())

                Spacer(modifier = Modifier.height(16.dp))

                // Botón para actualizar perfil
                Button(
                    onClick = { navController.navigate("createProfileScreen") },
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
                            repository.deleteProfile()
                            carProfile = null // Actualiza el estado después de eliminar
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
                // Mostrar opción para crear un perfil si no existe
                Text(
                    text = "¿AUN NO TIENES UN PERFIL?",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 8.dp)
                )

                // Mostrar GIF en lugar de imagen estática cuando no hay perfil
                AndroidView(
                    factory = { context ->
                        GifImageView(context).apply {
                            setImageResource(R.drawable.ic_carprofile) // Asegúrate de que el GIF esté en `res/drawable`
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Botón para crear un nuevo perfil
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
fun ProfileDetail(label: String, detail: String?) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Gray
        )
        Text(
            text = detail ?: "",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }
}
