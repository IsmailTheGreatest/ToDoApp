package com.great.todoapp.secondscreen

import MyRepository
import TodoItem
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.great.todoapp.viewmodel.AddItemAction
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class AddItemViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<AddItemUiState>(AddItemUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<AddItemEvent>()
    val uiEvent = _uiEvent.asSharedFlow()
    val item: TodoItem? = null
    var itemId: String? = null
    private val repository = MyRepository

    init {
        viewModelScope.launch {
            delay(200)
            if(itemId == null){

                _uiState.value = AddItemUiState.AddItemContent()
            }else{
            _uiState.value = AddItemUiState.EditItemContent(item = onEditItem(itemId!!))
    }}}

    fun onEditItem(item: String): TodoItem? {
        return repository.getItem(item)
    }
    fun onAction(action: AddItemAction) {
        when (action) {
            is AddItemAction.EditItem -> {
                repository.editItem(action.item)
                viewModelScope.launch {
                    _uiEvent.emit(AddItemEvent.NavigateToMain())
                }
            }
            is AddItemAction.AddItem -> {
                repository.addItem(action.item)
                viewModelScope.launch {
                    _uiEvent.emit(AddItemEvent.NavigateToMain())
                }

            }
            is AddItemAction.RemoveItem -> {
                viewModelScope.launch {
                    _uiEvent.emit(AddItemEvent.NavigateToMain())
                    delay(100)
                    repository.removeItem(action.item)



                }






            }
            is AddItemAction.OnButtonClick -> {
                viewModelScope.launch {
                    _uiEvent.emit(AddItemEvent.NavigateToMain())
                }
            }

            else -> {}
        }}
}


