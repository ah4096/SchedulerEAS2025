package com.example.finalprojectscheduler

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.finalprojectscheduler.screens.*

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable("schedule") { ScheduleScreen(navController) }
        composable("add") { AddScheduleScreen(navController) }
        composable(
            "edit/{day}/{index}",
            arguments = listOf(
                navArgument("day") { type = NavType.StringType },
                navArgument("index") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val day = backStackEntry.arguments?.getString("day") ?: ""
            val index = backStackEntry.arguments?.getInt("index") ?: 0
            EditScheduleScreen(navController, day, index)
        }
    }
}
