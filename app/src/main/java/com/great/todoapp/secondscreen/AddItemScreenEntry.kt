package com.great.todoapp.secondscreen




import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController



@Composable
fun AddItemScreenEntry(navController: NavController, item: String?) {

    val viewModel = viewModel(modelClass = AddItemViewModel::class)

    AddItemEventHandler(
        uiEvent = viewModel.uiEvent,

        navigateToMainScreen = { navController.navigate("main_screen") }
    )
    if (item == null)
    {
        AddItemScreen(
            item = null,
            uiState = viewModel.uiState.collectAsStateWithLifecycle().value,
            onAction = viewModel::onAction
        )}
    else{
        viewModel.itemId=item;
        AddItemScreen(
        item = item?.let { viewModel.onEditItem(it) },
        uiState = viewModel.uiState.collectAsStateWithLifecycle().value,
        onAction = viewModel::onAction

    )}
}