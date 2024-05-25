package com.example.natifetestapp.ui.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.natifetestapp.ui.screens.mainGifs.MainGifsScreen

@Composable
fun AppNavGraph(
    navHostController: NavHostController,
    startDestination: String = "test"
) {
    NavHost(
        navController = navHostController,
        startDestination = startDestination
    ) {
        composable(route = "test") {
            Column {
                MainGifsScreen()
            }
        }
    }
}