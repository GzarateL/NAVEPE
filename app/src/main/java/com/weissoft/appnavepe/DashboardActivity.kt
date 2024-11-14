package com.weissoft.appnavepe

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.ImageView
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
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import pl.droidsonroids.gif.GifImageView
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DashboardScreen(modifier: Modifier = Modifier, navController: NavHostController) {
    val context = LocalContext.current
    val callPermissionGranted = remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    var isSystemActive by remember { mutableStateOf(false) }

    var currentTime by remember { mutableStateOf(getCurrentTime()) }
    LaunchedEffect(Unit) {
        while (true) {
            currentTime = getCurrentTime()
            kotlinx.coroutines.delay(1000L)
        }
    }

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
                .verticalScroll(scrollState)
        ) {
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

                Text(
                    text = "HORA ACTUAL: $currentTime",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black)
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                CircularIconButton(
                    iconResource = R.drawable.ic_callpolice,
                    backgroundColor = Color(0xFF00FF00),
                    onClick = {
                        if (callPermissionGranted.value) {
                            makePhoneCall(context, "105")
                        } else {
                            launcher.launch(android.Manifest.permission.CALL_PHONE)
                        }
                    }
                )

                CircularIconButton(
                    iconResource = R.drawable.ic_map,
                    backgroundColor = Color(0xFFFFFF00),
                    onClick = { navController.navigate("mapScreen") }
                )

                CircularIconButton(
                    iconResource = if (isSystemActive) R.drawable.ic_system_active else R.drawable.ic_system_inactive,
                    backgroundColor = if (isSystemActive) Color(0xFFADD8E6) else Color(0xFFFF0000),
                    onClick = { isSystemActive = !isSystemActive }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "CÁMARA EN DIRECTO",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(start = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Box con el GIF animado
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(120.dp)
                    .border(BorderStroke(2.dp, Color.Black), shape = RoundedCornerShape(8.dp))
                    .background(if (isPressed) Color.Gray else Color.White, shape = RoundedCornerShape(8.dp))
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = {
                                navController.navigate("cameraScreen")
                            }
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                // Mostrar el GIF y ajustarlo dentro de los bordes redondeados
                AndroidView(
                    factory = { context ->
                        GifImageView(context).apply {
                            setImageResource(R.drawable.camera_icon) // Asegúrate de que el GIF esté en `res/drawable`
                            scaleType = ImageView.ScaleType.FIT_CENTER
                        }
                    },
                    modifier = Modifier
                        .fillMaxSize() // Ajusta para que ocupe todo el espacio disponible sin sobresalir
                        .padding(4.dp) // Ajuste adicional para evitar que el GIF se pegue a los bordes
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "ÚLTIMAS NOTIFICACIONES",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(start = 16.dp)
            )

            Spacer(modifier = Modifier.weight(1f))
        }

        BottomNavigationBar(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            onProfileClick = { navController.navigate("carProfile") }
        )
    }
}

@Composable
fun CircularIconButton(iconResource: Int, backgroundColor: Color, onClick: () -> Unit = {}) {
    Button(
        onClick = onClick,
        modifier = Modifier.size(100.dp),
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor)
    ) {
        Image(
            painter = painterResource(id = iconResource),
            contentDescription = null,
            modifier = Modifier.size(40.dp)
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
        NavigationItem(iconResource = R.drawable.ic_galery, label = "GALERÍA") {
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
