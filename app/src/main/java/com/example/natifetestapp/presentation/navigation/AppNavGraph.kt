package com.example.natifetestapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.natifetestapp.presentation.ui.screens.detailsGifs.DetailsGifsScreen
import com.example.natifetestapp.presentation.ui.screens.mainGifs.MainGifsScreen

@Composable
fun AppNavGraph(
    navHostController: NavHostController,
    startDestination: String = "main"
) {
    NavHost(
        navController = navHostController,
        startDestination = startDestination
    ) {
        composable(route = "main") {
            MainGifsScreen(
                onGifPressed = { initialIndex ->
                    navHostController.navigate("details/$initialIndex")
                }
            )
        }

        composable(
            route = "details/{initialIndex}",
            arguments = listOf(navArgument("initialIndex") { type = NavType.IntType })
        ) {
            DetailsGifsScreen()
        }
    }
}