package com.example.taskmanager.ui.screens.task

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.taskmanager.components.PriorityDropDown
import com.example.taskmanager.data.models.Priority
import com.example.taskmanager.utils.Constants.TITLE_CHAR_LIMIT

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskContent(
    title: String,
    onTitleChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    priority: Priority,
    onPriorityChange: (Priority) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(top = 72.dp, start = 12.dp, end = 12.dp, bottom = 12.dp)
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = title,
            onValueChange = { if(it.length < TITLE_CHAR_LIMIT) onTitleChange(it) },
            label = { Text(text = "Title") },
            textStyle = MaterialTheme.typography.bodyLarge,
            singleLine = true,
        )
        Spacer(modifier = Modifier.height(6.dp))
        PriorityDropDown(
            priority = priority,
            onPrioritySelected = onPriorityChange
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxSize(),
            value = description,
            onValueChange = { onDescriptionChange(it) },
            label = { Text(text = "Description")},
            textStyle = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview
@Composable
fun TaskContentPreview(){
    TaskContent(
        title = "",
        onTitleChange = {},
        description = "",
        onDescriptionChange = {},
        priority = Priority.MEDIUM,
        onPriorityChange = {}
    )
}