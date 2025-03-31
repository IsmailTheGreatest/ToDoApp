package com.great.todoapp.viewmodel

import TodoItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun MainScreenEventHandler(
    uiEvent: SharedFlow<MainScreenEvent>,
    navigateToEditFeature: (item: TodoItem) -> Unit,
    navigateToAddFeature: ( ) -> Unit

) {

    LaunchedEffect(key1 = Unit) {
        uiEvent.collectLatest { event ->
            when (event) {
                is MainScreenEvent.NavigateToEdit -> navigateToEditFeature(event.item)
                is MainScreenEvent.NavigateToCreate -> navigateToAddFeature()
                is MainScreenEvent.NavigateToMain -> navigateToAddFeature()
            }
        }
    }
}