package com.example.workshop_animations.some_feature

import AddItemScreen
import MainScreen
import MainScreenViewmodel
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.navArgument


@Composable
fun MainScreenEntry(navController: NavController) {

    val viewModel = viewModel(modelClass = MainScreenViewmodel::class)

    MainScreenEventHandler(
        uiEvent = viewModel.uiEvent,
        navigateToAnotherFeature = {
            navController.navigate("add_item_screen")

        },
        navigateToAddFeature = { navController.navigate("add_item_screen") }

    )
    MainScreen(
        uiState = viewModel.uiState.collectAsStateWithLifecycle().value,
        onAction = viewModel::onAction
    )
}