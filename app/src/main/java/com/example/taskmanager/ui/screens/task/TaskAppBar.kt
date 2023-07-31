package com.example.taskmanager.ui.screens.task

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.example.taskmanager.R
import com.example.taskmanager.components.DisplayAlertDialog
import com.example.taskmanager.data.models.Priority
import com.example.taskmanager.data.models.TaskEntity
import com.example.taskmanager.ui.theme.taskItemTextColor
import com.example.taskmanager.ui.theme.topAppBarBackgroundColor
import com.example.taskmanager.ui.theme.topAppBarContentColor
import com.example.taskmanager.utils.Actions

@Composable
fun TaskAppBar(
    selectedTask: TaskEntity?,
    navigateToListScreen: (Actions) -> Unit
) {
    if (selectedTask == null) {
        NewTaskAppBar(navigateToListScreen = navigateToListScreen)
    } else {
        ExistingTaskAppBar(
            selectedTask = selectedTask,
            navigateToListScreen = navigateToListScreen
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewTaskAppBar(
    navigateToListScreen: (Actions) -> Unit
) {
    TopAppBar(
        navigationIcon = {
            BackAction(onBackClicked = navigateToListScreen)
        },
        title = {
            Text(
                text = "Add Task",
                color = MaterialTheme.colorScheme.topAppBarContentColor
            )
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme
                .colorScheme
                .topAppBarBackgroundColor
        ),
        actions = {
            AddAction(onAddClicked = navigateToListScreen)
        }

    )
}


@Composable
fun BackAction(
    onBackClicked: (Actions) -> Unit
) {
    IconButton(onClick = { onBackClicked(Actions.NO_ACTION) }) {
        Icon(
            imageVector = Icons.Rounded.ArrowBack,
            contentDescription = "Back Action",
            tint = MaterialTheme.colorScheme.topAppBarContentColor
        )
    }
}

@Composable
fun AddAction(
    onAddClicked: (Actions) -> Unit
) {
    IconButton(onClick = { onAddClicked(Actions.ADD) }) {
        Icon(
            imageVector = Icons.Rounded.Check,
            contentDescription = "Add Task",
            tint = MaterialTheme.colorScheme.topAppBarContentColor
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExistingTaskAppBar(
    selectedTask: TaskEntity,
    navigateToListScreen: (Actions) -> Unit
) {
    TopAppBar(
        navigationIcon = {
            CloseAction(onCloseClicked = navigateToListScreen)
        },
        title = {
            Text(
                text = selectedTask.title,
                color = MaterialTheme.colorScheme.topAppBarContentColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme
                .colorScheme
                .topAppBarBackgroundColor
        ),
        actions = {
            ExistingAppBarActions(selectedTask = selectedTask,
                navigateToListScreen = navigateToListScreen )
        }

    )
}

@Composable
fun CloseAction(
    onCloseClicked: (Actions) -> Unit
) {
    IconButton(onClick = { onCloseClicked(Actions.NO_ACTION) }) {
        Icon(
            imageVector = Icons.Rounded.Close,
            contentDescription = "Close Action",
            tint = MaterialTheme.colorScheme.topAppBarContentColor
        )

    }
}

@Composable
fun ExistingAppBarActions(
    selectedTask: TaskEntity,
    navigateToListScreen: (Actions) -> Unit,

    ) {
    var openDialog by remember { mutableStateOf(false) }
    DisplayAlertDialog(
        title = stringResource(
            id = R.string.delete_task,
            selectedTask.title
        ),
        message = stringResource(
            id = R.string.delete_task_confirmation,
            selectedTask.title
        ),
        openDialog = openDialog,
        closeDialog = { openDialog = false },
        onYesSelected = {navigateToListScreen(Actions.DELETE)}
    )

    DeleteAction(onDeleteClicked = {
        openDialog = true
    })
    UpdateAction(onUpdateClicked = navigateToListScreen)
}

@Composable
fun DeleteAction(
    onDeleteClicked: () -> Unit
) {
    IconButton(onClick = { onDeleteClicked() }) {
        Icon(
            imageVector = Icons.Rounded.Delete,
            contentDescription = "Delete Action",
            tint = MaterialTheme.colorScheme.topAppBarContentColor
        )
    }
}

@Composable
fun UpdateAction(
    onUpdateClicked: (Actions) -> Unit
) {
    IconButton(onClick = { onUpdateClicked(Actions.UPDATE) }) {
        Icon(
            imageVector = Icons.Rounded.Check,
            contentDescription = "Update Action",
            tint = MaterialTheme.colorScheme.topAppBarContentColor
        )

    }
}

@Preview
@Composable
fun PreviewNewTaskAppBar() {
    NewTaskAppBar(navigateToListScreen = {})
}


@Preview
@Composable
fun PreviewExistingTaskAppBar() {
    ExistingTaskAppBar(
        navigateToListScreen = {},
        selectedTask = TaskEntity(0, "something", "nothing", Priority.HIGH)
    )
}







