package com.great.todoapp.viewmodel

import MainScreen
import MainScreenViewmodel
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController



@Composable
fun MainScreenEntry(navController: NavController) {

    val viewModel = viewModel(modelClass = MainScreenViewmodel::class)

    MainScreenEventHandler(
        uiEvent = viewModel.uiEvent,
        navigateToEditFeature = {
            navController.navigate("add_item_screen/${it.id}")

        },
        navigateToAddFeature = { navController.navigate("add_item_screen") }

    )
    MainScreen(
        uiState = viewModel.uiState.collectAsStateWithLifecycle().value,
        onAction = viewModel::onAction
    )
}