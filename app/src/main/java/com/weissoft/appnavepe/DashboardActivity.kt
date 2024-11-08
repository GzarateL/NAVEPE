package com.weissoft.appnavepe

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DashboardScreen(modifier: Modifier = Modifier, navController: NavHostController) {
    val context = LocalContext.current
    val callPermissionGranted = remember { mutableStateOf(false) }
    val scrollState = rememberScrollState() // Estado de desplazamiento

    // Estado para almacenar la hora actual
    var currentTime by remember { mutableStateOf(getCurrentTime()) }

    // Actualiza la hora cada segundo
    LaunchedEffect(Unit) {
        while (true) {
            currentTime = getCurrentTime()
            kotlinx.coroutines.delay(1000L) // 1 segundo
        }
    }

    // Launcher para solicitar el permiso de llamada
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        callPermissionGranted.value = isGranted
        if (isGranted) {
            makePhoneCall(context, "105")
        } else {
            Toast.makeText(context, "Permiso de llamada denegado", Toast.LENGTH_SHORT).show()
        }
    }

    var isPressed by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState) // Habilita desplazamiento vertical
        ) {
            // Row for Car Logo and Current Time
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.car_image),
                    contentDescription = "Car Logo",
                    modifier = Modifier.size(60.dp)
                )

                // Mostrar hora actual en la misma línea que el logo
                Text(
                    text = "HORA ACTUAL: $currentTime",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Rectángulo negro que ocupa todo el ancho de la pantalla
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black)
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                CircularButton(
                    text = "LLAMAR POLICÍA",
                    backgroundColor = Color(0xFF00FF00),
                    onClick = {
                        if (callPermissionGranted.value) {
                            makePhoneCall(context, "105")
                        } else {
                            launcher.launch(android.Manifest.permission.CALL_PHONE)
                        }
                    }
                )
                CircularButton(text = "VER MAPA", backgroundColor = Color(0xFFFFFF00))
                CircularButton(text = "DESACTIVAR SISTEMA", backgroundColor = Color(0xFFFF0000))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Camera Section
            Text(
                text = "¿QUE ESTA PASANDO AHORA?",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(start = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Botón de la cámara con efecto de presionado
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(120.dp)
                    .border(
                        BorderStroke(2.dp, Color.Black),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .background(if (isPressed) Color.Gray else Color.White, shape = RoundedCornerShape(8.dp))
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = {
                                isPressed = true
                                tryAwaitRelease()
                                isPressed = false
                            }
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_eye),
                    contentDescription = "Camera Icon",
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Last Movements Section
            Text(
                text = "ULTIMAS NOTIFICACIONES",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(start = 16.dp)
            )

            Spacer(modifier = Modifier.weight(1f)) // Espaciador para empujar la barra de navegación hacia abajo
        }

        // Bottom Navigation Bar aligned to the bottom of the screen
        BottomNavigationBar(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            onProfileClick = { navController.navigate("carProfile") }
        )
    }
}

@Composable
fun CircularButton(text: String, backgroundColor: Color, onClick: () -> Unit = {}) {
    Button(
        onClick = onClick,
        modifier = Modifier.size(100.dp),
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor)
    ) {
        Text(
            text = text,
            color = Color.Black,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun BottomNavigationBar(
    modifier: Modifier = Modifier,
    onProfileClick: () -> Unit
) {
    Row(
        modifier = modifier
            .background(Color.Black)
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        NavigationItem(iconResource = R.drawable.ic_settings, label = "AJUSTES") {
            // Acción para ir a ajustes
        }
        NavigationItem(iconResource = R.drawable.ic_galery, label = "GALERIA") {
            // Acción para ir a galería
        }
        NavigationItem(iconResource = R.drawable.ic_profile, label = "PERFIL") {
            onProfileClick()
        }
    }
}

@Composable
fun NavigationItem(iconResource: Int, label: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(16.dp)
            .pointerInput(Unit) {
                detectTapGestures(onTap = { onClick() })
            }
    ) {
        Image(
            painter = painterResource(id = iconResource),
            contentDescription = label,
            modifier = Modifier.size(32.dp)
        )
        Text(text = label, color = Color.White)
    }
}

// Función para obtener la hora actual
fun getCurrentTime(): String {
    val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    return sdf.format(Date())
}

// Función para realizar la llamada telefónica
fun makePhoneCall(context: Context, phoneNumber: String) {
    val intent = Intent(Intent.ACTION_CALL)
    intent.data = Uri.parse("tel:$phoneNumber")
    context.startActivity(intent)
}
