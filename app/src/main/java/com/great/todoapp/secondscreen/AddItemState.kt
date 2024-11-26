package com.great.todoapp.secondscreen

import TodoItem


sealed interface AddItemUiState {

    data object Loading : AddItemUiState

    data class EditItemContent(val item: TodoItem) : AddItemUiState
        class AddItemContent(): AddItemUiState

}