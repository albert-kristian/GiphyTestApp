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
    startDestination: String = Routes.MainScreenRoute.route()
) {
    NavHost(
        navController = navHostController,
        startDestination = startDestination
    ) {
        composable(route = Routes.MainScreenRoute.route()) {
            MainGifsScreen(
                onGifPressed = { initialIndex ->
                    navHostController.navigate(
                        with(Routes.DetailsScreenRoute) {
                            buildRoute(
                                arguments = mapOf(
                                    keyInitialItemIndex to initialIndex
                                )
                            )
                        }
                    )
                }
            )
        }
        composable(
            route = Routes.DetailsScreenRoute.route(),
            arguments = listOf(
                navArgument(
                    Routes.DetailsScreenRoute.keyInitialItemIndex
                ) {
                    type = NavType.IntType
                }
            )
        ) {
            DetailsGifsScreen()
        }
    }
}