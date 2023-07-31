package com.example.taskmanager.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.taskmanager.data.models.Priority
import com.example.taskmanager.ui.theme.taskItemTextColor
import kotlin.math.exp

@Composable
fun PriorityDropDown(
    priority: Priority,
    onPrioritySelected: (Priority) -> Unit
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    val angle: Float by animateFloatAsState(targetValue = if (expanded) 180f else 0f)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable {
                expanded = true
            }
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                shape = MaterialTheme.shapes.extraSmall
            ),
        verticalAlignment = Alignment.CenterVertically,

    ) {
        Canvas(
            modifier = Modifier
                .size(16.dp)
                .weight(1f)
        ) {
            drawCircle(color = priority.color)
        }
        Text(
            modifier = Modifier.weight(8f),
            text = priority.name,
            style = MaterialTheme.typography.bodyLarge
        )
        IconButton(
            modifier = Modifier
                .alpha(0.5f)
                .rotate(angle)
                .weight(1.5f),
            onClick = { expanded = true }
        ) {
            Icon(
                imageVector = Icons.Rounded.ArrowDropDown,
                contentDescription = "Dropdown arrow",
            )
        }
        DropdownMenu(
            modifier = Modifier.fillMaxWidth(fraction = 0.94f),
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }) {
            DropdownMenuItem(
                text = {
                    PriorityItem(priority = Priority.LOW)
                },
                onClick = {
                    expanded = false
                    onPrioritySelected(Priority.LOW)
                }
            )
            DropdownMenuItem(
                text = {
                    PriorityItem(priority = Priority.MEDIUM)
                },
                onClick = {
                    expanded = false
                    onPrioritySelected(Priority.MEDIUM)
                }
            )
            DropdownMenuItem(
                text = {
                    PriorityItem(priority = Priority.HIGH)
                },
                onClick = {
                    expanded = false
                    onPrioritySelected(Priority.HIGH)
                }
            )
        }

    }
}

@Preview
@Composable
fun Preview() {
    PriorityDropDown(priority = Priority.HIGH, onPrioritySelected = {})
}