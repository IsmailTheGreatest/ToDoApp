package com.great.todoapp.viewmodel

import TodoItem


sealed interface MainScreenAction {

    class OnItemClick(val item : TodoItem) : MainScreenAction
    class  OnButtonClick : MainScreenAction

    class AddItem(val item: TodoItem) : MainScreenAction
    class EditItem(val item: TodoItem) : MainScreenAction
    class RemoveItem(val item: TodoItem) : MainScreenAction
    class MakeItemDone(val item: TodoItem) : MainScreenAction
}