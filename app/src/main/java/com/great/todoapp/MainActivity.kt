package com.great.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController




import androidx.navigation.navArgument

import com.great.todoapp.secondscreen.AddItemScreenEntry
import com.great.todoapp.viewmodel.MainScreenEntry


class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TodoApp()
        }
    }
}
@Composable
fun TodoApp() {
    Surface(color = Color(0xFFF7F6F2)) {

        val navController = rememberNavController()
        NavHost(navController, startDestination = "main_screen") {
            composable("main_screen") {
                MainScreenEntry(navController)
            }
            composable("add_item_screen/{itemId}", arguments = listOf(navArgument("itemId") {type=
                NavType.StringType})) {
                backStackEntry ->
                val itemId = backStackEntry.arguments?.getString("itemId")
                AddItemScreenEntry(navController, itemId)
            }
            composable("add_item_screen") {
                AddItemScreenEntry(navController, null)
            }

        }
    }
}
















