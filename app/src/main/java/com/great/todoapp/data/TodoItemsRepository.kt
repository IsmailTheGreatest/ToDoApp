package com.great.todoapp.data

import java.util.Date

class TodoItemsRepository {

    // Hardcoded list of 20 TodoItems
    private val todoItems = mutableListOf<TodoItem>(
        // Sample data creation for testing
        TodoItem("1", "Buy groceries", Importance.NO, null, false, Date(), Date()),
        TodoItem("2", "Prepare presentation", Importance.HIGH, Date(), false, Date()),
        TodoItem("4", "Prepare presentation2", Importance.LOW, Date(), false, Date()),
        TodoItem("5", "Prepare presentation2", Importance.LOW, Date(), false, Date()),
        TodoItem("6", "Prepare presentation2dvn fjvhfjdvfdvhfduvhdfhvudfhv udfhvufdhvufduhvfudhvufd hvufdhvufdvuhfdvufdhvufdhvfdvhudfvu", Importance.LOW, Date(), false, Date()),



        // Add more items as needed...
    )

    // Method to get the list of tasks
    fun getTodoItems(): List<TodoItem> = todoItems

    // Method to add a new task
    fun addTodoItem(item: TodoItem) {
        todoItems.add(item)
    }

    // Method to remove a task by its ID
    fun removeTodoItem(id: String) {
        todoItems.removeIf { it.id == id }
    }
    fun countCompleted(): Int {
        return todoItems.count { it.isCompleted }
    }
    fun updateTodoItemCompletion(id: String, isCompleted: Boolean) {
        val item = todoItems.find { it.id == id }
        item?.let {
            val index = todoItems.indexOf(it)
            todoItems[index] = it.copy(isCompleted = isCompleted)
        }
    }
    fun updateTodoItem(item: TodoItem) {
        val index = todoItems.indexOfFirst { it.id == item.id }
        if (index != -1) {
            todoItems[index] = item
        }
    }

}
