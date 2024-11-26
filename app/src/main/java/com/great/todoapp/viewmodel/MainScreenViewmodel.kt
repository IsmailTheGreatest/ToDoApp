import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.great.todoapp.data.MainUiState
import com.great.todoapp.viewmodel.MainScreenAction
import com.great.todoapp.viewmodel.MainScreenEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class MainScreenViewmodel : ViewModel() {

    private val _uiState = MutableStateFlow<MainUiState>(MainUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<MainScreenEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private val repository = MyRepository

    init {
        viewModelScope.launch {
            delay(1000)
            repository.getList().collectLatest { items ->
                _uiState.update {
                    MainUiState.Content(items = items)
                }
            }
        }
    }

    fun onAction(action: MainScreenAction) {
        when (action) {
            is MainScreenAction.MakeItemDone -> {
                repository.makeItemDone(action.item)
            }
            is MainScreenAction.EditItem -> {
                repository.editItem(action.item)
                viewModelScope.launch {
                    _uiEvent.emit(MainScreenEvent.NavigateToEdit(item = action.item))
                }
            }
            is MainScreenAction.OnButtonClick -> {
                viewModelScope.launch {
                    _uiEvent.emit(MainScreenEvent.NavigateToCreate())
                }
            }
            is MainScreenAction.OnItemClick -> {
                viewModelScope.launch {
                    _uiEvent.emit(MainScreenEvent.NavigateToEdit(item = action.item))
                }
            }

            is MainScreenAction.AddItem -> {
                repository.addItem(action.item)
                viewModelScope.launch {
                    _uiEvent.emit(MainScreenEvent.NavigateToMain())
                }

            }

            is MainScreenAction.RemoveItem -> {
                repository.removeItem(action.item)
            }}}
}


