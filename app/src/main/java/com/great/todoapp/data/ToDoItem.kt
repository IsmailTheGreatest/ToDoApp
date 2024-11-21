import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

 data class TodoItem(
    val id: String,
    val text: String,
    val importance: String,
    val deadline: Long?,
    val done: Boolean,
    val color: String?,
    val created_at: Long,
    val changed_at: Long,
    val last_updated_by: String
)

data class TodoListResponse(
    val status: String,
    val list: List<TodoItem>,
    val revision: Int
)

data class TodoItemResponse(
    val status: String,
    val element: TodoItem,
    val revision: Int
)


object MyRepository {

    private val list = MutableStateFlow(
        listOf(
            TodoItem(
                id = UUID.randomUUID().toString(),
                text = "one",
                done = false,
                importance = "Высокий",
                deadline = 1234567890,
                color = "red",

                created_at = 1234567890,
                changed_at = 1234567890,
                last_updated_by = "user"
            ),
            TodoItem(
                id = UUID.randomUUID().toString(),
                text = "two",
                done = false,
                importance = "Высокий",
                deadline = 1234567890,
                color = "red",
                created_at = 1234567890,
                changed_at = 1234567890,
                last_updated_by = "user"

            ), )
    )

    fun getList() = list.asStateFlow()
fun getItem(id: String): TodoItem {
        return list.value.first { it.id == id }
    }
    fun addItem(item: TodoItem) {
        list.update { it + item }
    }
    fun editItem(item: TodoItem) {
        val currentList = list.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == item.id }
        currentList[index] = item

        list.update { currentList }
    }

    fun removeItem(item: TodoItem) {
        val currentList = list.value.toMutableList()
        currentList.remove(item)

        list.update { currentList }
    }
    fun makeItemDone(item: TodoItem) {
        val currentList = list.value.toMutableList()
        val index = currentList.indexOf(item)
        currentList[index] = item.copy(done = !item.done)

        list.update { currentList }
    }
}




