package com.example.taskmanager.view


import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.taskmanager.R
import com.example.taskmanager.model.dataClass.Task
import com.example.taskmanager.utils.Utilities.performVibration
import com.example.taskmanager.viewModel.TaskViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController, viewModel: TaskViewModel = hiltViewModel()) {

    val tasks by viewModel.allTasks.collectAsState()

    val filteredAndSortedTasks = viewModel.getFilteredAndSortedTasks(tasks)

    val taskList = remember { mutableStateOf(filteredAndSortedTasks) }

    LaunchedEffect(filteredAndSortedTasks) {
        taskList.value = filteredAndSortedTasks
    }

    val isLoading by viewModel.isLoading.collectAsState(initial = true)

    var isClicked by remember { mutableStateOf(false) }
    val scale = remember { Animatable(1f) }


    var showSortMenu by remember { mutableStateOf(false) }
    var showFilterMenu by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val dragState = rememberReorderableLazyListState(onMove = { from, to ->
        context.performVibration(100)
        taskList.value = taskList.value.toMutableList().apply {
            add(to.index, removeAt(from.index))
        }
    })


    val snackBarHostState = remember { SnackbarHostState() }



    val scope = rememberCoroutineScope()

    val completedTaskPercentage = remember(tasks) {
        if (tasks.isEmpty()) 0f
        else tasks.count { it.isCompleted }.toFloat() / tasks.size
    }


    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Task Manager") },
                actions = {
                    CircularProgressBar(
                        progress = completedTaskPercentage,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                    IconButton(
                        onClick = { showFilterMenu = true },
                        modifier = Modifier.semantics { contentDescription = "Filter Tasks" }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_filter_list_alt_24),
                            contentDescription = "Filter Tasks"
                        )
                    }
                    DropdownMenu(
                        expanded = showFilterMenu,
                        onDismissRequest = { showFilterMenu = false }
                    ) {
                        DropdownMenuItem(
                            modifier = Modifier.semantics { contentDescription = "Filter All" },
                            text = { Text("All") },
                            onClick = {
                                viewModel.updateFilterOption(FilterOption.ALL)
                                showFilterMenu = false
                            }
                        )
                        DropdownMenuItem(
                            modifier = Modifier.semantics { contentDescription = "Filter Completed" },
                            text = { Text("Completed") },
                            onClick = {
                                viewModel.updateFilterOption(FilterOption.COMPLETED)
                                showFilterMenu = false
                            }
                        )
                        DropdownMenuItem(
                            modifier = Modifier.semantics { contentDescription = "Filter Pending" },
                            text = { Text("Pending") },
                            onClick = {
                                viewModel.updateFilterOption(FilterOption.PENDING)
                                showFilterMenu = false
                            }
                        )
                    }
                    IconButton(onClick = { showSortMenu = true },
                        modifier = Modifier.semantics { contentDescription = "Sort Tasks" }) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_sort_24),
                            contentDescription = "Sort Tasks"
                        )
                    }
                    DropdownMenu(
                        expanded = showSortMenu,
                        onDismissRequest = { showSortMenu = false }
                    ) {
                        DropdownMenuItem(
                            modifier = Modifier.semantics { contentDescription = "Sort By Priority" },
                            text = { Text("By Priority") },
                            onClick = {
                                viewModel.updateSortOption(SortOption.PRIORITY)
                                showSortMenu = false
                            }
                        )
                        DropdownMenuItem(
                            modifier = Modifier.semantics { contentDescription = "Sort By Due Date" },
                            text = { Text("By Due Date") },
                            onClick = {
                                viewModel.updateSortOption(SortOption.DUE_DATE)
                                showSortMenu = false
                            }
                        )
                        DropdownMenuItem(
                            modifier = Modifier.semantics { contentDescription = "Sort By Title" },
                            text = { Text("By Title") },
                            onClick = {
                                viewModel.updateSortOption(SortOption.ALPHABETICAL)
                                showSortMenu = false
                            }
                        )
                    }
                    IconButton(onClick = { navController.navigate("settingsScreen") },
                        modifier = Modifier.semantics { contentDescription = "Settings" }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    scope.launch {
                        isClicked = true
                        scale.animateTo(
                            targetValue = 1.2f,
                            animationSpec = tween(durationMillis = 100, easing = LinearEasing)
                        )
                        scale.animateTo(
                            targetValue = 1f,
                            animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                        )
                        delay(50)
                        navController.navigate("taskCreation")
                        isClicked = false
                    }
                },
                modifier = Modifier
                    .semantics { contentDescription = "Add Task" }
                    .scale(scale.value),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
            }
        }
    ) { paddingValues ->
        if (isLoading) {
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                items(5) {
                    ShimmerEffect(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .padding(bottom = 8.dp)
                    )
                }
            }
        } else if (filteredAndSortedTasks.isEmpty()) {
            EmptyStateUI(modifier = Modifier.padding(paddingValues))
        } else {
            LazyColumn(
                state = dragState.listState,
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .padding(16.dp)
                    .reorderable(dragState)
                    .detectReorderAfterLongPress(dragState)
                    .semantics { contentDescription = "Task List" }
            ) {
                itemsIndexed(items = taskList.value, key = { _, listItem ->
                    listItem.hashCode()
                }) { _, item ->


                    var isDeleted by remember { mutableStateOf(false) }
                    var isAdded by remember { mutableStateOf(false) }
                    val state = rememberSwipeToDismissBoxState(
                        confirmValueChange = {
                            snackBarHostState.currentSnackbarData?.dismiss()
                            when (it) {
                                SwipeToDismissBoxValue.EndToStart -> {
                                    isDeleted = true
                                    viewModel.deleteTask(item)
                                    showUndoSnackBar(
                                        scope, snackBarHostState, "Task Deleted"
                                    ) {
                                        isAdded = true
                                        viewModel.insert(item)
                                    }
                                }

                                SwipeToDismissBoxValue.StartToEnd -> {
                                    if (!item.isCompleted) {
                                        viewModel.markTaskCompletion(item, true)
                                        showUndoSnackBar(
                                            scope,
                                            snackBarHostState, "Task Completed"
                                        ) {
                                            viewModel.markTaskCompletion(item, false)
                                        }
                                    }
                                }

                                SwipeToDismissBoxValue.Settled -> {}
                            }
                            true
                        }
                    )
                    LaunchedEffect(state.currentValue) {
                        if (state.currentValue != SwipeToDismissBoxValue.Settled) {
                            state.reset()
                        }
                    }
                    ReorderableItem(dragState, key = item) { isDragging ->
                        val elevation = animateDpAsState(if (isDragging) 8.dp else 0.dp)
                        SwipeToDismissBox(
                            modifier = Modifier.shadow(elevation.value),
                            state = state,
                            backgroundContent = {
                                val color = when (state.dismissDirection) {
                                    SwipeToDismissBoxValue.EndToStart -> Color(0xFFD34E4E)
                                    SwipeToDismissBoxValue.StartToEnd -> Color(0xFF4CAF50)
                                    SwipeToDismissBoxValue.Settled -> Color.Transparent
                                }
                                Card(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = 16.dp, vertical = 8.dp),
                                    shape = MaterialTheme.shapes.medium,
                                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .background(color)
                                            .fillMaxSize()
                                    ) {
                                        if (state.dismissDirection == SwipeToDismissBoxValue.EndToStart) {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = "Delete",
                                                modifier = Modifier
                                                    .padding(end = 8.dp)
                                                    .align(Alignment.CenterEnd)
                                                    .semantics {
                                                        contentDescription = "Delete Task"
                                                    }
                                            )
                                        } else {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = "Mark as completed",
                                                modifier = Modifier
                                                    .padding(start = 8.dp)
                                                    .align(Alignment.CenterStart)
                                                    .semantics {
                                                        contentDescription = "Mark as completed"
                                                    }
                                            )
                                        }
                                    }
                                }
                            },
                            content = {
                                TaskItem(task = item, onClick = {
                                    navController.currentBackStackEntry?.savedStateHandle?.set(
                                        "task",
                                        item
                                    )
                                    navController.navigate("taskDetails")
                                })
                            }
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun TaskItem(
    task: Task,
    onClick: () -> Unit,
) {
    val completionStatusColor = when (task.isCompleted) {
        false -> Color(0xFFFF6B6B)
        true -> Color(0xFF4CAF50)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick() },
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                    shape = MaterialTheme.shapes.medium
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(completionStatusColor, CircleShape)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = task.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Priority: ${task.priority}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Due Date: ${task.dueDate}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

fun showUndoSnackBar(
    scope: CoroutineScope,
    snackBarHostState: SnackbarHostState,
    message: String,
    onUndo: () -> Unit
) {
    scope.launch {
        val result = snackBarHostState.showSnackbar(
            message = message,
            actionLabel = "Undo",
            duration = SnackbarDuration.Short
        )
        if (result == SnackbarResult.ActionPerformed) {
            onUndo()
        }
    }
}

@Composable
fun EmptyStateUI(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.empty),
            contentDescription = "Empty State Illustration",
            modifier = Modifier
                .size(200.dp)
                .padding(16.dp)
        )
        Text(
            text = "No tasks yet!",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            text = "Start by adding a task and stay organized.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp),
            textAlign = TextAlign.Center

        )
    }
}


@Composable
fun CircularProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    strokeWidth: Dp = 8.dp,
    backgroundColor: Color = Color.LightGray,
    progressColor: Color = MaterialTheme.colorScheme.primary,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(40.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val size = size.minDimension
            val radius = size / 2

            drawCircle(
                color = backgroundColor,
                radius = radius - strokeWidth.toPx() / 2,
                style = Stroke(width = strokeWidth.toPx())
            )

            drawArc(
                color = progressColor,
                startAngle = -90f,
                sweepAngle = 360 * progress,
                useCenter = false,
                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            )
        }

        Text(
            text = "${(progress * 100).toInt()}%",
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

@Composable
fun ShimmerEffect(
    modifier: Modifier = Modifier,
) {
    val transition = rememberInfiniteTransition()
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Reverse
        )
    )

    val brush = Brush.linearGradient(
        colors = listOf(
            Color.LightGray.copy(alpha = 0.6f),
            Color.LightGray.copy(alpha = 0.2f),
            Color.LightGray.copy(alpha = 0.6f)
        ),
        start = Offset(translateAnim.value, translateAnim.value),
        end = Offset(translateAnim.value + 500f, translateAnim.value + 500f)
    )

    Box(
        modifier = modifier
            .background(brush)
            .fillMaxWidth()
            .height(100.dp)
    )
}

enum class SortOption {
    PRIORITY, DUE_DATE, ALPHABETICAL
}

enum class FilterOption {
    ALL, COMPLETED, PENDING
}