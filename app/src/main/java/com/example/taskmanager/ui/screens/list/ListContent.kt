@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.taskmanager.ui.screens.list

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.taskmanager.R
import com.example.taskmanager.data.models.Priority
import com.example.taskmanager.data.models.TaskEntity
import com.example.taskmanager.ui.theme.*
import com.example.taskmanager.utils.Actions
import com.example.taskmanager.utils.RequestState
import com.example.taskmanager.utils.SearchAppBarState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ListContent(
    allTasks: RequestState<List<TaskEntity>>,
    lowPriorityTasks: List<TaskEntity>,
    highPriorityTasks: List<TaskEntity>,
    sortState: RequestState<Priority>,
    searchedTasks: RequestState<List<TaskEntity>>,
    onSwipeToDelete: (Actions, TaskEntity) -> Unit,
    navigateToTaskScreen: (taskId: Int) -> Unit,
    searchAppBarState: SearchAppBarState
) {
    if (sortState is RequestState.Success) {
        when {
            searchAppBarState == SearchAppBarState.TRIGGERED -> {
                if (searchedTasks is RequestState.Success) {
                    HandleListContent(
                        tasks = searchedTasks.data,
                        navigateToTaskScreen = navigateToTaskScreen,
                        onSwipeToDelete = onSwipeToDelete
                    )
                } else if (searchedTasks is RequestState.Loading) {
                    CircularProgressIndicator()
                }
            }

            sortState.data == Priority.NONE -> {
                if (allTasks is RequestState.Success) {
                    HandleListContent(
                        tasks = allTasks.data,
                        navigateToTaskScreen = navigateToTaskScreen,
                        onSwipeToDelete = onSwipeToDelete
                    )
                } else if (allTasks is RequestState.Loading) {
                    CircularProgressIndicator()
                }
            }

            sortState.data == Priority.HIGH -> {
                HandleListContent(
                    tasks = highPriorityTasks,
                    navigateToTaskScreen = navigateToTaskScreen,
                    onSwipeToDelete = onSwipeToDelete
                )
            }

            sortState.data == Priority.LOW -> {
                HandleListContent(
                    tasks = lowPriorityTasks,
                    navigateToTaskScreen = navigateToTaskScreen,
                    onSwipeToDelete = onSwipeToDelete
                )
            }
        }
    }

}

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HandleListContent(
    tasks: List<TaskEntity>,
    onSwipeToDelete: (Actions, TaskEntity) -> Unit,
    navigateToTaskScreen: (taskId: Int) -> Unit
) {
    if (tasks.isEmpty()) {
        EmptyContent()
    } else {
        LazyColumn {
            items(
                items = tasks,
                key = { item ->
                    item.id
                }
            ) { task ->
                val dismissState = rememberDismissState()
                val dismissDirection = dismissState.dismissDirection
                val isDismissed = dismissState.isDismissed(DismissDirection.EndToStart)
                if (isDismissed && dismissDirection == DismissDirection.EndToStart) {
                    val scope = rememberCoroutineScope()
                    scope.launch {
                        // reason for this delay is bc our animations are 300ms long
                        delay(300)
                        onSwipeToDelete(Actions.DELETE, task)
                    }
                }

                val degrees by animateFloatAsState(
                    if (dismissState.targetValue == DismissValue.Default) 0f
                    else -45f
                )
                var itemAppeared by remember{
                    mutableStateOf(false)
                }
                LaunchedEffect(key1 = true){
                    itemAppeared = true
                }

                AnimatedVisibility(
                    visible = itemAppeared && !isDismissed, // this parameter tells if the content should be visible
                    enter = expandVertically(
                        animationSpec = tween(300)
                    ),
                    exit = shrinkHorizontally (
                        animationSpec = tween(300)
                    )
                ) {
                    SwipeToDismiss(
                        state = dismissState,
                        directions = setOf(DismissDirection.EndToStart),
                        background = { RedBackground(degrees = degrees) },
                        dismissContent = {
                            TaskItem(
                                task = task,
                                navigateToTaskScreen = navigateToTaskScreen
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun RedBackground(degrees: Float) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(HighPriorityColor)
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Icon(
            modifier = Modifier.rotate(degrees),
            imageVector = Icons.Rounded.Delete,
            contentDescription = "Delete Icon",
            tint = Color.White
        )
    }
}

@Composable
fun TaskItem(
    task: TaskEntity,
    navigateToTaskScreen: (taskId: Int) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        color = MaterialTheme.colorScheme.taskItemBackGroundColor,
        shadowElevation = 2.dp,
        onClick = {
            navigateToTaskScreen(task.id)
        }
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
        ) {
            Row {
                Text(
                    modifier = Modifier.weight(9f),
                    text = task.title,
                    color = MaterialTheme.colorScheme.taskItemTextColor,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.TopEnd
                ) {
                    Canvas(
                        modifier = Modifier
                            .size(16.dp)
                    ) {
                        drawCircle(color = task.priority.color)
                    }
                }
            }
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                text = task.description,
                color = MaterialTheme.colorScheme.taskItemTextColor,
                style = MaterialTheme.typography.bodyMedium,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2
            )
        }
    }
}

@Preview
@Composable
fun TaskItemPreview() {
    TaskItem(task = TaskEntity(
        title = "task1",
        priority = Priority.NONE,
        description = "task 1 should be completed",
        id = 1
    ), navigateToTaskScreen = {})
}

@Composable
@Preview
private fun RedBackgroundPreview() {
    Column(modifier = Modifier.height(80.dp)) {
        RedBackground(degrees = 0f)
    }
}