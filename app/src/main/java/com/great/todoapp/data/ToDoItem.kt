package com.great.todoapp.data

import java.util.Date

data class TodoItem(
    val id: String, // unique identifier
    val text: String, // task description
    val importance: Importance, // task priority
    val deadline: Date? = null, // optional deadline
    val isCompleted: Boolean, // completion flag
    val createdAt: Date, // creation date
    val modifiedAt: Date? = null // optional modification date
)

enum class Importance {
    NO, LOW, HIGH
}
