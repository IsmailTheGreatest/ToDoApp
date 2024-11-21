package com.great.todoapp.data

import TodoItem


sealed interface MainUiState {

    data object Loading : MainUiState

    data class Content(val items: List<TodoItem>) : MainUiState
}