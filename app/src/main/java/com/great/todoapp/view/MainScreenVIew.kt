import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.sharp.Star
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


import com.great.todoapp.data.MainUiState
import com.great.todoapp.viewmodel.MainScreenAction
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.reflect.KFunction1






@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(uiState: MainUiState, onAction: KFunction1<MainScreenAction, Unit>) {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) {
        when (uiState) {
            is MainUiState.Content -> {
                MainScreenContent(Modifier.padding(top = it.calculateTopPadding()), uiState, onAction)
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
fun MainScreenContent(
    modifier: Modifier,
    uiState: MainUiState.Content,
    onAction: (MainScreenAction) -> Unit
) {
    val lazyListState = rememberLazyListState()


    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val isCollapsed = scrollBehavior.state.collapsedFraction > 0.5f
    var showCompleted by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
    ) {
        LargeTopAppBar(
            title = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Text("Мои дела", fontSize = 30.sp, fontWeight = FontWeight.Bold)
                    if (!isCollapsed) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            "Выполненные - ${uiState.items.size}", color = Color.Gray,
                            fontSize = 13.sp
                        )
                    }
                }
            },
            actions = {
                IconButton(onClick = { showCompleted = !showCompleted }) {
                    Icon(
                        imageVector = if (showCompleted) Icons.Default.Done else Icons.Default.Clear,
                        contentDescription = if (showCompleted) "Hide completed tasks" else "Show completed tasks"
                    )
                }
            },
            scrollBehavior = scrollBehavior
        )
        Spacer(modifier = Modifier.height(10.dp))
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .shadow(10.dp, RoundedCornerShape(10.dp))
                .background(color = Color(0xFFFFFFFF), shape = RoundedCornerShape(10.dp))
        ) {
            LazyColumn(contentPadding = PaddingValues(10.dp)) {
                items(uiState.items.filter { !it.done || showCompleted }) { item ->
                    TodoItemView(item, onAction)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
            TextButton(onClick = { onAction(MainScreenAction.OnButtonClick()) }) {
                Text(
                    modifier = Modifier.padding(horizontal = 60.dp),
                    text = "Новое дело",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
    FloatingActionButton(
        containerColor = Color(0xFF007aff),
        onClick = { onAction(MainScreenAction.OnButtonClick()) },
        modifier = Modifier
            .absolutePadding(top = 700.dp, left = 300.dp)
            .padding(10.dp)
            .shadow(10.dp, CircleShape)
            .background(color = Color(0xFF000000), shape = CircleShape)
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add",
            tint = Color.White
        )
    }



}

@Composable
fun TodoItemView(item: TodoItem, onAction: (MainScreenAction) -> Unit) {
    var isChecked by remember { mutableStateOf(item.done) }
    val priorityIcon = when (item.importance) {
        "Низкий" -> Icons.Default.KeyboardArrowDown
        "Нет" -> null
        "Высокий" -> Icons.Sharp.Star
        else -> {
            null
        }
    }
    val priorityIconTint = if (item.importance == "Высокий") Color.Red else Color.Gray

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.Start
            ) {
                Checkbox(
                    modifier = Modifier.padding(horizontal = 0.dp),
                    checked = isChecked,
                    onCheckedChange = { isCheckedChange ->
                        isChecked = isCheckedChange
                        onAction(MainScreenAction.MakeItemDone(item))
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color.Green,
                        uncheckedColor = Color.Gray
                    )
                )
                Spacer(modifier = Modifier.width(6.dp))
                if (priorityIcon != null) {
                    Icon(
                        imageVector = priorityIcon,
                        contentDescription = null,
                        tint = priorityIconTint,
                        modifier = Modifier.size(30.dp)
                    )
                } else {
                    Spacer(modifier = Modifier.width(30.dp))
                }
                Spacer(modifier = Modifier.width(4.dp))
                Column(modifier = Modifier.fillMaxWidth(fraction = 0.8f)) {
                    Text(
                        text = item.text,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            textDecoration = if (isChecked) TextDecoration.LineThrough else TextDecoration.None
                        ),
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (item.deadline != null) {
                        val dateFormat = SimpleDateFormat("dd-MMM", Locale("ru"))
                        Text(
                            text = dateFormat.format(item.deadline),
                            color = Color.Gray,
                            fontSize = 13.sp
                        )
                    }
                }
            }
            IconButton(onClick = { onAction(MainScreenAction.OnItemClick(item)) }, modifier = Modifier.size(24.dp)) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Info",
                    tint = Color.Gray
                )
            }
        }
        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color.LightGray))
    }
}