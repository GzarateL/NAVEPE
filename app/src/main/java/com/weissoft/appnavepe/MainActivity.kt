package com.weissoft.appnavepe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.weissoft.appnavepe.ui.theme.AppNavepeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppNavepeTheme {
                // Crear el NavController
                val navController = rememberNavController()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Configurar el NavHost con el navController y definir las rutas
                    NavHost(
                        navController = navController,
                        startDestination = "dashboard",
                        Modifier.padding(innerPadding)
                    ) {
                        // Ruta para el DashboardScreen
                        composable("dashboard") {
                            DashboardScreen(
                                navController = navController // Pasar el navController a DashboardScreen
                            )
                        }
                        // Ruta para el CarProfileScreen
                        composable("carProfile") {
                            CarProfileScreen(
                                navController = navController // Pasar el navController a CarProfileScreen
                            )
                        }
                    }
                }
            }
        }
    }
}
