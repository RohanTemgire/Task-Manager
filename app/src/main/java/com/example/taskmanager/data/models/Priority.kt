package com.example.taskmanager.data.models

import androidx.compose.ui.graphics.Color
import com.example.taskmanager.ui.theme.HighPriorityColor
import com.example.taskmanager.ui.theme.LowPriorityColor
import com.example.taskmanager.ui.theme.MediumPriorityColor
import com.example.taskmanager.ui.theme.NonePriorityColor

enum class Priority(val color : Color) {
    HIGH(HighPriorityColor),
    MEDIUM(MediumPriorityColor),
    LOW(LowPriorityColor),
    NONE(NonePriorityColor)
}