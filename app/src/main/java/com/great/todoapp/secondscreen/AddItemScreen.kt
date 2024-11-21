import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.great.todoapp.data.MainUiState
import com.great.todoapp.viewmodel.MainScreenAction
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.UUID
import kotlin.reflect.KFunction1

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemScreen(item: TodoItem?,
    uiState: MainUiState, onAction: KFunction1<MainScreenAction, Unit>)
    // Optional parameter for editing
     {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) {
        when (uiState) {
            is MainUiState.Content -> {


                    AddItemScreenContent(Modifier.padding(top = it.calculateTopPadding()), onAction, item = item)


            }

            MainUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }

}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemScreenContent(
   modifier: Modifier,
    onAction: (MainScreenAction) -> Unit,
    item: TodoItem?=null
) {
    val priorityOptions = listOf("Нет", "Низкий", "Высокий")
    var taskText by remember { mutableStateOf(item?.text ?: "") }
    var priority by remember { mutableStateOf(
        when (item?.importance) {
            "Нет" -> priorityOptions[0]
            "Низкий"-> priorityOptions[1]
            "Высокий" -> priorityOptions[2]
            else -> priorityOptions[0]
        }
    ) }
    var isDateEnabled by remember { mutableStateOf(item?.deadline != null) }
    var dueDate by remember { mutableStateOf(item?.deadline) }
    var showDatePicker by remember { mutableStateOf(false) }

    if (showDatePicker) {
        val context = LocalContext.current
        val calendar = Calendar.getInstance()
        val datePickerDialog = android.app.DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                dueDate = calendar.time.time
                showDatePicker = false
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.setOnDismissListener {
            if (dueDate == null) {
                isDateEnabled = false
            }
            showDatePicker = false
        }
        datePickerDialog.show()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = { onAction(MainScreenAction.OnButtonClick()) }) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                },
                actions = {
                    TextButton(onClick = {
                        if (taskText.isNotEmpty()) {
                            val newItem = TodoItem(
                                id = item?.id ?: UUID.randomUUID().toString(),
                                text = taskText,
                                importance = when (priority) {
                                    "Нет"-> "Нет"
                                    "Низкий" -> "Низкий"
                                    "Высокий" ->  "Высокий"
                                    else -> "Нет"
                                },
                                deadline = if (isDateEnabled) dueDate else null,
                                done = item?.done ?: false,
                                created_at = item?.created_at ?: Date().time,
                                changed_at = Date().time,
                                color = "",
                                last_updated_by = "user"
                            )
                            if (item == null) {
                                onAction(MainScreenAction.AddItem(newItem))
                            } else {
                                onAction(MainScreenAction.EditItem(newItem))
                            }

                        }
                    }) {
                        Text("СОХРАНИТЬ", color = MaterialTheme.colorScheme.primary)
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .background(Color(0xFFF5F5F5))
                .padding(16.dp)
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                BasicTextField(
                    minLines = 5,
                    maxLines = 20,
                    value = taskText,
                    onValueChange = { taskText = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(0.5.dp, Color.LightGray, MaterialTheme.shapes.small)
                        .background(Color.White, shape = MaterialTheme.shapes.small)
                        .padding(16.dp),
                    textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
                    decorationBox = { innerTextField ->
                        if (taskText.isEmpty()) {
                            Text("Что надо сделать...", color = Color.Gray)
                        }
                        innerTextField()
                    }
                )

                HorizontalDivider(thickness = 1.dp, color = Color.LightGray, modifier = Modifier.padding(vertical = 8.dp))
                Row (modifier = Modifier
                    .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
                ){
                    Text("Важность", fontSize = 16.sp, color = Color.Black)
                    Spacer(modifier = Modifier.fillMaxWidth(0.4f))
                    Row(
                        modifier = Modifier
                            .border(0.5.dp, Color.LightGray, MaterialTheme.shapes.small)
                            .fillMaxWidth(1f)
                            .background(Color(0xfff0f0f0), shape = MaterialTheme.shapes.small)
                            .padding(2.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        priorityOptions.forEach { option ->
                            val backgroundColor by animateColorAsState(
                                targetValue = if (priority == option) Color.White else Color(0xfff0f0f0)
                            )
                            val elevation by animateDpAsState(
                                targetValue = if (priority == option) 3.dp else 0.dp
                            )
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .shadow(elevation, MaterialTheme.shapes.small)
                                    .padding(vertical = 0.dp)
                                    .background(backgroundColor, shape = RoundedCornerShape(8.dp))
                                    .clickable { priority = option }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    fontSize = 10.sp,
                                    text = option,
                                    color = if (priority == option) Color.Black else Color.Gray,
                                    fontWeight = if (priority == option) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }
                }

                HorizontalDivider(thickness = 1.dp, color = Color.LightGray, modifier = Modifier.padding(vertical = 8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Сделать до", fontSize = 16.sp, color = Color.Black)
                    Spacer(modifier = Modifier.weight(1f))
                    Switch(checked = isDateEnabled, onCheckedChange = {
                        isDateEnabled = it
                        if (it) {
                            showDatePicker = true
                        } else {
                            dueDate = null
                        }
                    })
                }

                if (isDateEnabled && dueDate != null) {
                    Text(
                        SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(dueDate!!),
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.fillMaxWidth(0.2f))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Удалить", color = Color.Gray)
                }
            }
        }
    }
}