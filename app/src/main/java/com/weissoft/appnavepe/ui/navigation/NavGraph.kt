// NavGraph.kt
package com.weissoft.appnavepe.ui.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.weissoft.appnavepe.DashboardScreen
import com.weissoft.appnavepe.NewsScreen
import com.weissoft.appnavepe.ui.screens.CarProfileData
import com.weissoft.appnavepe.ui.screens.CreateProfileScreen
import com.weissoft.appnavepe.sensores.CameraScreen
import com.weissoft.appnavepe.room.CarProfileRepository
import com.weissoft.appnavepe.sensores.MapScreen

@Composable
fun NavGraph(navController: NavHostController, repository: CarProfileRepository, context: Context) {
    NavHost(navController = navController, startDestination = "dashboard") {
        // Pantalla de inicio: Dashboard
        composable("dashboard") {
            DashboardScreen(navController = navController)
        }

        // Pantalla del perfil de auto
        composable("carProfileScreen") {
            CarProfileData(navController = navController, repository = repository)
        }

        // Pantalla para crear o actualizar el perfil de auto
        composable("createProfileScreen") {
            CreateProfileScreen(navController = navController, repository = repository)
        }

        // Pantalla de la cámara (CameraScreen)
        composable("cameraScreen") {
            CameraScreen(navController = navController)
        }

        // Pantalla del mapa (MapScreen)
        composable("mapScreen") {
            MapScreen(
                navController = navController,
                repository = repository,
                context = context // Pasa el contexto desde el parámetro
            )
        }
        composable("newsScreen") {
            NewsScreen(navController)
        }

    }
}
