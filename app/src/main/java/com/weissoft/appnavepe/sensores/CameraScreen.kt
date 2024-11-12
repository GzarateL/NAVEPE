package com.weissoft.appnavepe.sensores

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.weissoft.appnavepe.R

@Composable
fun CameraScreen(modifier: Modifier = Modifier, navController: NavHostController) {
    // Estado para manejar la conexión a la cámara
    var isCameraInitialized by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Flecha de retroceso en la esquina superior izquierda
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = "Back",
                tint = Color.Black
            )
        }

        // Caja para la transmisión de la cámara, centrada en la pantalla
        Box(
            modifier = Modifier
                .align(Alignment.Center) // Centra la vista de la cámara en la pantalla
                .width(500.dp) // Ancho específico de la vista de la cámara
                .height(800.dp) // Altura específica de la vista de la cámara
                .padding(8.dp)
        ) {
            if (isCameraInitialized) {
                ESP32CameraView(modifier = Modifier.fillMaxSize()) // Muestra la transmisión de la cámara
            }
        }
    }

    // Efecto de ciclo de vida para inicializar y liberar la cámara
    DisposableEffect(Unit) {
        // Código para inicializar la cámara al entrar en la pantalla
        isCameraInitialized = true // Cambia a `true` para mostrar el stream

        onDispose {
            // Código para liberar recursos de la cámara al salir de la pantalla
            isCameraInitialized = false // Cambia a `false` para ocultar el stream
        }
    }
}
