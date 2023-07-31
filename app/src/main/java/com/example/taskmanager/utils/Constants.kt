package com.example.taskmanager.utils

import androidx.compose.ui.unit.dp

object Constants {

    const val DATABASE_TABLE_NAME = "task_table"
    const val DATABASE_NAME = "task_db"

    const val LIST_SCREEN = "list/{action}"
    const val TASK_SCREEN = "task/{taskId}"
    const val SPLASH_SCREEN = "splash"

    const val LIST_ARGUMENT_KEY = "action"
    const val TASK_ARGUMENT_KEY = "taskId"

    const val TITLE_CHAR_LIMIT = 35

    const val PREFERENCE_NAME = "todo_preferences"
    const val PREFERENCE_KEY = "sort_state"

    val LOGO_HEIGHT = 100.dp
    const val SPLASH_SCREEN_DELAY = 2000L

}