package com.great.todoapp.secondscreen


sealed interface AddItemEvent {
    class NavigateToMain() : AddItemEvent

}