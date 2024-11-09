// NavGraph.kt
package com.weissoft.appnavepe.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.weissoft.appnavepe.DashboardScreen
import com.weissoft.appnavepe.ui.screens.CarProfileData
import com.weissoft.appnavepe.ui.screens.CreateProfileScreen
import com.weissoft.appnavepe.room.CarProfileRepository

@Composable
fun NavGraph(navController: NavHostController, repository: CarProfileRepository) {
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
    }
}
