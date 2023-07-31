package com.example.taskmanager.ui.screens.list

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.*
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.taskmanager.R
import com.example.taskmanager.ui.viewmodels.SharedViewmodel
import com.example.taskmanager.utils.Actions
import com.example.taskmanager.utils.SearchAppBarState
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ListScreen(
    navigateToTaskScreen: (taskId: Int) -> Unit,
    sharedViewmodel: SharedViewmodel
) {
    LaunchedEffect(key1 = true) {
        sharedViewmodel.getAllTasks()
        sharedViewmodel.readSortState()
    }
    val action by sharedViewmodel.action

    val sortState by sharedViewmodel.sortState.collectAsState()
    val lowPriorityTasks by sharedViewmodel.lowPriorityTasks.collectAsState()
    val highPriorityTasks by sharedViewmodel.highPriorityTasks.collectAsState()
    val allTasks by sharedViewmodel.allTasks.collectAsState()
    val searchedTasks by sharedViewmodel.searchedTasks.collectAsState()
    val searchAppBarState: SearchAppBarState by sharedViewmodel.searchAppBarState
    val searchTextState: String by sharedViewmodel.searchTextState

    val snackbarHostState = remember { SnackbarHostState() }

    DisplaySnackBar(
        snackbarHostState = snackbarHostState,
        handleDatabaseActions = { sharedViewmodel.handleDatabaseActions(action = action) },
        taskTitle = sharedViewmodel.title.value,
        onUndoClicked = {
            sharedViewmodel.action.value = it
        },
        action = sharedViewmodel.action.value
    )

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            ListAppBar(
                searchAppBarState = searchAppBarState,
                searchTextState = searchTextState,
                viewmodel = sharedViewmodel
            )
        },
        content = {
            Column(modifier = Modifier.padding(top = 60.dp)) {
                ListContent(
                    allTasks = allTasks,
                    navigateToTaskScreen = navigateToTaskScreen,
                    searchedTasks = searchedTasks,
                    searchAppBarState = searchAppBarState,
                    sortState = sortState,
                    lowPriorityTasks = lowPriorityTasks,
                    highPriorityTasks = highPriorityTasks,
                    onSwipeToDelete = { action, task ->
                        sharedViewmodel.action.value = action
                        sharedViewmodel.updateTaskFields(task)
                        snackbarHostState.currentSnackbarData?.dismiss()
                    }
                )
            }
        },
        floatingActionButton = {
            ListFab(onFabClicked = navigateToTaskScreen)
        }
    )
}

@Composable
fun ListFab(
    onFabClicked: (taskId: Int) -> Unit
) {
    FloatingActionButton(onClick = {
        onFabClicked(-1)
    }) {
        Icon(
            imageVector = Icons.Rounded.Add,
            contentDescription =
            stringResource(id = R.string.add_button)
        )
    }
}

@Composable
fun DisplaySnackBar(
    snackbarHostState: SnackbarHostState,
    handleDatabaseActions: () -> Unit,
    taskTitle: String,
    onUndoClicked: (Actions) -> Unit,
    action: Actions
) {

    handleDatabaseActions()

    val scope = rememberCoroutineScope()
    LaunchedEffect(key1 = action) {
        if (action != Actions.NO_ACTION) {
            scope.launch {
                val snackBarResult = snackbarHostState.showSnackbar(
                    message = setMessage(action = action,taskTitle = taskTitle),
                    actionLabel = setActionLabel(action),
                    duration = SnackbarDuration.Short
                )
                undoDeletedTask(
                    action = action,
                    snackbarResult = snackBarResult,
                    onUndoClicked = onUndoClicked
                )
            }
        }
    }
}

private fun setActionLabel(action: Actions): String {
    return if (action == Actions.DELETE) {
        "Undo"
    } else {
        "Ok"
    }
}

private fun setMessage(action: Actions, taskTitle: String): String {
    return when (action) {
        Actions.DELETE_ALL -> "All Tasks Removed."
        else -> "${action.name}: $taskTitle"
    }
}

private fun undoDeletedTask(
    action: Actions,
    snackbarResult: SnackbarResult,
    onUndoClicked: (Actions) -> Unit
) {
    if (snackbarResult == SnackbarResult.ActionPerformed && action == Actions.DELETE) {
        onUndoClicked(Actions.UNDO)
    }
}