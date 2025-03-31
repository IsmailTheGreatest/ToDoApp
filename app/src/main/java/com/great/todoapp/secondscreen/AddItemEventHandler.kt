package  com.great.todoapp.secondscreen


import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AddItemEventHandler(
    uiEvent: SharedFlow<AddItemEvent>,

    navigateToMainScreen: ( ) -> Unit

) {

    LaunchedEffect(key1 = Unit) {
        uiEvent.collectLatest { event ->
            when (event) {

                is AddItemEvent.NavigateToMain -> navigateToMainScreen()


            }
        }
    }
}