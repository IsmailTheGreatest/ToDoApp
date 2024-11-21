package com.example.workshop_animations.some_feature

import TodoItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.great.todoapp.viewmodel.MainScreenEvent
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun MainScreenEventHandler(
    uiEvent: SharedFlow<MainScreenEvent>,
    navigateToAnotherFeature: (item: TodoItem) -> Unit,
    navigateToAddFeature: ( ) -> Unit

) {

    LaunchedEffect(key1 = Unit) {
        uiEvent.collectLatest { event ->
            when (event) {
                is MainScreenEvent.NavigateToEdit -> navigateToAnotherFeature(event.item)
                is MainScreenEvent.NavigateToCreate -> navigateToAddFeature()
                is MainScreenEvent.NavigateToMain -> navigateToAddFeature()
            }
        }
    }
}