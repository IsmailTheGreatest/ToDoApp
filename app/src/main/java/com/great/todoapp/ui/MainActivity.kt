package com.great.todoapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.sharp.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.FontScaling
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.great.todoapp.data.Importance
import com.great.todoapp.data.TodoItem
import com.great.todoapp.data.TodoItemsRepository
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*

import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.platform.LocalContext


import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration


import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TodoApp()
        }
    }
}
@Composable
fun TodoApp() {
    Surface(color = Color(0xFFF7F6F2)) { // Set your desired background color here
        val todoItemsRepository = TodoItemsRepository()
        val navController = rememberNavController()
        NavHost(navController, startDestination = "main_screen") {
            composable("main_screen") {
                MainScreen(todoItemsRepository, navController)
            }
            composable("add_item_screen") {
                AddItemScreen(todoItemsRepository, navController)
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(repository: TodoItemsRepository, navController: NavController) {
    val todoItems = repository.getTodoItems()
    val numberOfCompleted by remember { derivedStateOf { repository.countCompleted() } }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val isCollapsed = scrollBehavior.state.collapsedFraction > 0.5f

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
                            "Выполненные - $numberOfCompleted", color = Color.Gray,
                            fontSize = 13.sp
                        )
                    }
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
                items(todoItems) { item ->
                    TodoItemView(item, repository)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
            TextButton(onClick = { navController.navigate("add_item_screen") }) {
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
        onClick = { navController.navigate("add_item_screen") },
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
//@Composable
//fun SwipeToActionRow(
//    item: TodoItem,
//    onComplete: () -> Unit,
//    onDelete: () -> Unit,
//    content: @Composable () -> Unit
//) {
//    var offsetX by remember { mutableStateOf(0f) }
//    val dismissThreshold = 200f // Adjust as needed
//
//    // Animate the offset for smooth swiping
//    val animatedOffsetX by animateFloatAsState(targetValue = offsetX)
//
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .background(Color.White)
//            .pointerInput(Unit) {
//                detectHorizontalDragGestures(
//                    onDragEnd = {
//                        when {
//                            offsetX > dismissThreshold -> onComplete()
//                            offsetX < -dismissThreshold -> onDelete()
//                            else -> offsetX = 0f
//                        }
//                    },
//                    onHorizontalDrag = { _, dragAmount ->
//                        offsetX += dragAmount
//                    }
//                )
//            }
//    ) {
//        // Background for complete action
//        if (offsetX > 0) {
//            Box(
//                modifier = Modifier
//                    .fillMaxHeight()
//                    .width(60.dp)
//                    .background(Color.Green),
//                contentAlignment = Alignment.CenterStart
//            ) {
//                Icon(
//                    Icons.Default.Check,
//                    contentDescription = "Complete",
//                    tint = Color.White,
//                    modifier = Modifier.padding(start = 16.dp)
//                )
//            }
//        }
//
//        // Main row content
//        Box(
//            modifier = Modifier
//                .fillMaxHeight()
//                .offset { IntOffset(animatedOffsetX.roundToInt(), 0) }
//                .padding(vertical = 8.dp)
//        ) {
//            content()
//        }
//
//        // Background for delete action
//        if (offsetX < 0) {
//            Box(
//                modifier = Modifier
//                    .fillMaxHeight()
//                    .width(60.dp)
//                    .background(Color.Red),
//                contentAlignment = Alignment.CenterEnd
//            ) {
//                Icon(
//                    Icons.Default.Delete,
//                    contentDescription = "Delete",
//                    tint = Color.White,
//                    modifier = Modifier.padding(end = 16.dp)
//                )
//            }
//        }
//    }
//}

@Composable
fun TodoItemView(item: TodoItem, repository: TodoItemsRepository) {
    var isChecked by remember { mutableStateOf(item.isCompleted) }
    val priorityIcon = when (item.importance) {
        Importance.LOW -> Icons.Default.KeyboardArrowDown
        Importance.NO -> null
        Importance.HIGH -> Icons.Sharp.Star
    }
    val priorityIconTint = if (item.importance == Importance.HIGH) Color.Red else Color.Gray

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
                    repository.updateTodoItemCompletion(item.id, isChecked)
                }
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
        IconButton(onClick = {}, modifier = Modifier.size(24.dp)) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "Info",
                tint = Color.Gray
            )
        }
    }
}

//@Composable
//fun TodoItemView(item: TodoItem, repository: TodoItemsRepository) {
//    var isChecked by remember { mutableStateOf(item.isCompleted) }
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 5.dp),
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.SpaceBetween
//    ) {
//        Row(
//
//            horizontalArrangement = Arrangement.Start
//        ) {
//            Checkbox(
//                modifier = Modifier.padding(horizontal = 0.dp),
//                checked = isChecked,
//                onCheckedChange = { isCheckedChange ->
//                    isChecked= isCheckedChange
//                    repository.updateTodoItemCompletion(item.id, isChecked)
//                }
//            )
//            Spacer(modifier = Modifier.width(6.dp))
//            Column(modifier = Modifier.fillMaxWidth(fraction = 0.8f)) {
//                Text(
//                    text = item.text,
//                    style = MaterialTheme.typography.bodyLarge,
//                    maxLines = 3,
//                    overflow = TextOverflow.Ellipsis
//                )
//                if (item.deadline != null) {
//                    Text("Дата")
//                }
//            }
//        }
//        IconButton(onClick = {}, modifier = Modifier.size(24.dp)) {
//            Icon(
//                imageVector = Icons.Default.Info,
//                contentDescription = "Info",
//                tint = Color.Gray
//            )
//        }
//    }
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemScreen(
    repository: TodoItemsRepository,
    navController: NavController,
    item: TodoItem? = null // Optional parameter for editing
) {
    val priorityOptions = listOf("Нет", "Низкий", "Высокий")
    var taskText by remember { mutableStateOf(item?.text ?: "") }
    var priority by remember { mutableStateOf(
        when (item?.importance) {
            Importance.NO -> priorityOptions[0]
            Importance.LOW -> priorityOptions[1]
            Importance.HIGH -> priorityOptions[2]
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
                dueDate = calendar.time
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
                    IconButton(onClick = { navController.popBackStack() }) {
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
                                    "Нет"-> Importance.NO
                                    "Низкий" -> Importance.LOW
                                    "Высокий" -> Importance.HIGH
                                    else -> Importance.NO
                                },
                                deadline = if (isDateEnabled) dueDate else null,
                                isCompleted = item?.isCompleted ?: false,
                                createdAt = item?.createdAt ?: Date(),
                                modifiedAt = Date()
                            )
                            if (item == null) {
                                repository.addTodoItem(newItem)
                            } else {
                                repository.updateTodoItem(newItem)
                            }
                            navController.popBackStack()
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












