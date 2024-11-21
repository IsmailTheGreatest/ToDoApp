package com.great.todoapp

import AddItemScreen
import TodoItem
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.sharp.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController



import androidx.compose.foundation.border
import androidx.compose.foundation.clickable

import androidx.compose.foundation.text.BasicTextField
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.*
import androidx.compose.material.icons.filled.Clear

import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext


import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration

import com.example.workshop_animations.some_feature.MainScreenEntry
import com.great.todoapp.secondscreen.AddItemScreenEntry


import java.text.SimpleDateFormat
import java.util.*

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
            composable("add_item_screen") {
                AddItemScreenEntry(navController, null)
            }

        }
    }
}
















