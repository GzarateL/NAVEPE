package com.weissoft.appnavepe.sensores

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.weissoft.appnavepe.R

@Composable
fun CameraScreen(modifier: Modifier = Modifier, navController: NavHostController) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        // Botón de retroceso en la esquina superior izquierda
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier.align(Alignment.Start)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_back), // Reemplaza con tu recurso de flecha
                contentDescription = "Back",
                tint = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Fila para centrar y alinear la vista de la cámara a la derecha
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End // Alinea todo el contenido a la derecha
        ) {
            Box(
                modifier = Modifier
                    .width(500.dp) // Ancho específico de la vista de la cámara
                    .height(1000.dp) // Altura específica de la vista de la cámara
                    .padding(8.dp)
            ) {
                ESP32CameraView(modifier = Modifier.fillMaxSize()) // Muestra la transmisión de la cámara
            }
        }
    }
}
