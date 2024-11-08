package com.weissoft.appnavepe

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

class SplashScreenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SplashScreen()
        }
    }
}

@Composable
fun SplashScreen() {
    val context = LocalContext.current

    // Efecto para retrasar la navegación y luego iniciar el DashboardActivity
    LaunchedEffect(true) {
        delay(2000) // Espera de 2 segundos
        context.startActivity(Intent(context, MainActivity::class.java))
    }

    // Diseño de la pantalla de carga
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_navepe), // Asegúrate de que esta sea la imagen correcta
            contentDescription = "Logo de Navepe",
            modifier = Modifier.size(200.dp) // Ajusta el tamaño según prefieras
        )
    }
}
