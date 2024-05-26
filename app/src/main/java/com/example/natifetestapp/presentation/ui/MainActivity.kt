package com.example.natifetestapp.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.natifetestapp.presentation.navigation.AppNavGraph
import com.example.natifetestapp.presentation.ui.theme.NatifeTestAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NatifeTestAppTheme {
                val navController = rememberNavController()
                AppNavGraph(navHostController = navController)
            }
        }
    }
}