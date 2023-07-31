package com.example.taskmanager.ui.screens.list

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.taskmanager.components.PriorityItem
import com.example.taskmanager.R
import com.example.taskmanager.components.DisplayAlertDialog
import com.example.taskmanager.data.models.Priority
import com.example.taskmanager.ui.theme.topAppBarBackgroundColor
import com.example.taskmanager.ui.theme.topAppBarContentColor
import com.example.taskmanager.ui.viewmodels.SharedViewmodel
import com.example.taskmanager.utils.Actions
import com.example.taskmanager.utils.SearchAppBarState

@Composable
fun ListAppBar(
    searchAppBarState: SearchAppBarState,
    searchTextState: String,
    viewmodel: SharedViewmodel
) {
    when (searchAppBarState) {
        SearchAppBarState.CLOSED -> {
            DefaultListAppBar(
                onSearchClicked = {
                    viewmodel.searchAppBarState.value = SearchAppBarState.OPENED
                },
                onSortClicked = {viewmodel.persistSortState(it)},
                onDeleteAllConfirmed = {
                    viewmodel.action.value = Actions.DELETE_ALL
                })
        }

        else -> {
            SearchAppBar(
                text = searchTextState,
                onTextChanged = { newtext ->
                    viewmodel.searchTextState.value = newtext
                },
                onCloseClicked = {
                    viewmodel.searchAppBarState.value = SearchAppBarState.CLOSED
                    viewmodel.searchTextState.value = ""
                },
                onSearchClicked = {
                    viewmodel.searchDatabase(it)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultListAppBar(
    onSearchClicked: () -> Unit,
    onSortClicked: (Priority) -> Unit,
    onDeleteAllConfirmed: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = "Tasks",
                color = MaterialTheme.colorScheme.topAppBarContentColor
            )
        },
        colors = TopAppBarDefaults
            .smallTopAppBarColors(
                containerColor = MaterialTheme
                    .colorScheme
                    .topAppBarBackgroundColor
            ),
        actions = {
            ListAppBarActions(
                onSearchClicked = onSearchClicked,
                onSortClicked = onSortClicked,
                onDeleteAllConfirmed = onDeleteAllConfirmed
            )
        }
    )
}

@Composable
fun ListAppBarActions(
    onSearchClicked: () -> Unit,
    onSortClicked: (Priority) -> Unit,
    onDeleteAllConfirmed: () -> Unit
) {
    var openDialog by remember {
        mutableStateOf(false)
    }
    DisplayAlertDialog(
        title = stringResource(id = R.string.delete_all_tasks),
        message = stringResource(id = R.string.delete_all_tasks_confirmation),
        openDialog = openDialog,
        closeDialog = { openDialog = false },
        onYesSelected = {
            onDeleteAllConfirmed()
        }
    )
    SearchTasks(onSearchClicked)
    SortAction(onSortClicked = onSortClicked)
    OnDeleteAllConfirm(onDeleteAllClicked = { openDialog = true })
}

@Composable
fun SearchTasks(
    onSearchClicked: () -> Unit
) {
    IconButton(onClick = onSearchClicked) {
        Icon(
            imageVector = Icons.Rounded.Search,
            contentDescription = "Search Tasks",
            tint = Color.White
        )
    }
}

@Composable
fun SortAction(
    onSortClicked: (Priority) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    IconButton(onClick = {
        expanded = true
    }) {
        Icon(
            painter = painterResource(id = R.drawable.ic_filter_list),
            contentDescription = "Sort Tasks",
            tint = Color.White
        )
        DropdownMenu(expanded = expanded,
            onDismissRequest = { expanded = false }) {
            DropdownMenuItem(
                text = { PriorityItem(priority = Priority.LOW) },
                onClick = {
                    expanded = false
                    onSortClicked(Priority.LOW)
                })
            DropdownMenuItem(
                text = { PriorityItem(priority = Priority.HIGH) },
                onClick = {
                    expanded = false
                    onSortClicked(Priority.HIGH)
                })
            DropdownMenuItem(
                text = { PriorityItem(priority = Priority.NONE) },
                onClick = {
                    expanded = false
                    onSortClicked(Priority.NONE)
                })
        }
    }
}

@Composable
fun OnDeleteAllConfirm(
    onDeleteAllClicked: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    IconButton(onClick = {
        expanded = true
    }) {
        Icon(
            painter = painterResource(id = R.drawable.ic_vertical_menu),
            contentDescription = "Delete all tasks",
            tint = Color.White
        )
        DropdownMenu(expanded = expanded,
            onDismissRequest = { expanded = false }) {
            DropdownMenuItem(
                text = {
                    Text(
                        text = "Delete All",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(start = 12.dp)
                    )
                },
                onClick = {
                    expanded = false
                    onDeleteAllClicked()
                })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchAppBar(
    text: String,
    onTextChanged: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearchClicked: (String) -> Unit
) {
    var deleteTextOnFirstClick by remember { mutableStateOf(false) }
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shadowElevation = 4.dp,
        color = MaterialTheme.colorScheme.topAppBarBackgroundColor,
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = text,
            onValueChange = { onTextChanged(it) },
            placeholder = {
                Text(
                    text = "Search..",
                    color = Color.White,
                    modifier = Modifier.alpha(0.7f)
                )
            },
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.topAppBarContentColor,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
            ),
            singleLine = true,
            leadingIcon = {
                Icon(
                    modifier = Modifier.alpha(0.5f),
                    imageVector = Icons.Rounded.Search,
                    contentDescription = "Search Icon",
                    tint = MaterialTheme.colorScheme.topAppBarContentColor
                )
            },
            trailingIcon = {
                IconButton(onClick = {
                    if (deleteTextOnFirstClick) {
                        onTextChanged("")
                        deleteTextOnFirstClick = false
                    } else {
                        if (text.isNotEmpty()) {
                            onTextChanged("")
                        } else {
                            onCloseClicked()
                            deleteTextOnFirstClick = true
                        }
                    }
                }) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = "Close",
                        tint = MaterialTheme.colorScheme.topAppBarContentColor
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearchClicked(text)
                }
            ),
            colors = TextFieldDefaults.textFieldColors(
                cursorColor = MaterialTheme.colorScheme.topAppBarContentColor,
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                containerColor = Color.Transparent
            )
        )
    }
}

@Preview
@Composable
fun DefaultListAppBarPreview() {
    DefaultListAppBar({}, {}, {})
}

@Preview
@Composable
fun SearchAppBarPreview() {
    SearchAppBar(
        text = "",
        {}, {}, {}
    )
}