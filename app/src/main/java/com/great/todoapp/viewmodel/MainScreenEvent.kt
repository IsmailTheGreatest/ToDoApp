package com.great.todoapp.viewmodel

import TodoItem

sealed interface MainScreenEvent {

    class NavigateToEdit(item: TodoItem) : MainScreenEvent{
        val item = item
    }
    class NavigateToCreate() : MainScreenEvent
    class NavigateToMain() : MainScreenEvent

}