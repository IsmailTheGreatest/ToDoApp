package com.great.todoapp.secondscreen

import AddItemScreen
import MainScreen
import MainScreenViewmodel
import TodoItem
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.workshop_animations.some_feature.MainScreenEventHandler


@Composable
fun AddItemScreenEntry(navController: NavController, item: TodoItem?) {

    val viewModel = viewModel(modelClass = MainScreenViewmodel::class)

    MainScreenEventHandler(
        uiEvent = viewModel.uiEvent,
        navigateToAnotherFeature = { navController.navigate("main_screen") },
        navigateToAddFeature = { navController.navigate("main_screen") }
    )
    AddItemScreen(
        item = item,
        uiState = viewModel.uiState.collectAsStateWithLifecycle().value,
        onAction = viewModel::onAction

    )
}