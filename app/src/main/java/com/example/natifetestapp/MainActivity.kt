package com.example.natifetestapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.natifetestapp.ui.navigation.AppNavGraph
import com.example.natifetestapp.ui.theme.NatifeTestAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

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