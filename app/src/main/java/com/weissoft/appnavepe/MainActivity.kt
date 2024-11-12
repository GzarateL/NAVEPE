package com.weissoft.appnavepe

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.weissoft.appnavepe.room.CarProfileDatabase
import com.weissoft.appnavepe.room.CarProfileRepository
import com.weissoft.appnavepe.sensores.CameraScreen
import com.weissoft.appnavepe.sensores.MapScreen
import com.weissoft.appnavepe.ui.screens.CarProfileScreen
import com.weissoft.appnavepe.ui.screens.CreateProfileScreen
import com.weissoft.appnavepe.ui.theme.AppNavepeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Configura la base de datos
        val database = Room.databaseBuilder(
            applicationContext,
            CarProfileDatabase::class.java,
            "car_profile_database"
        ).build()

        val repository = CarProfileRepository(database.carProfileDao())

        setContent {
            AppNavepeTheme {
                val navController = rememberNavController()

                // Detecta la pantalla actual
                val backStackEntry = navController.currentBackStackEntryAsState()

                // Cambia la orientación en función de la pantalla actual
                DisposableEffect(backStackEntry.value) {
                    val route = backStackEntry.value?.destination?.route
                    if (route == "cameraScreen") {
                        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                    } else {
                        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    }
                    onDispose { }
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "dashboard",
                        Modifier.padding(innerPadding)
                    ) {
                        composable("dashboard") {
                            DashboardScreen(navController = navController)
                        }
                        composable("carProfile") {
                            CarProfileScreen(
                                navController = navController,
                                repository = repository
                            )
                        }
                        composable("createProfileScreen") {
                            CreateProfileScreen(
                                navController = navController,
                                repository = repository
                            )
                        }
                        composable("cameraScreen") {
                            CameraScreen(navController = navController)
                        }
                        composable("mapScreen") { // Pasa el repository y context a MapScreen
                            MapScreen(
                                navController = navController,
                                repository = repository,
                                context = this@MainActivity // Pasa el contexto de la actividad actual
                            )
                        } // Cierra el composable "mapScreen"
                    } // Cierra NavHost
                } // Cierra Scaffold
            }
        }
    }
}
