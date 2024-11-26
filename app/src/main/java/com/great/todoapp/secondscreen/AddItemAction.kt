package com.great.todoapp.viewmodel

import TodoItem


sealed interface AddItemAction {
    class AddItem(val item: TodoItem) : AddItemAction
    class EditItem(val item: TodoItem) : AddItemAction
    class RemoveItem(val item: TodoItem) : AddItemAction
    class OnButtonClick() : AddItemAction

}