package com.example.taskmanager.navigation.destinations

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.taskmanager.ui.screens.task.TaskScreen
import com.example.taskmanager.ui.viewmodels.SharedViewmodel
import com.example.taskmanager.utils.Actions
import com.example.taskmanager.utils.Constants

fun NavGraphBuilder.taskComposable(
    navigateToListScreen: (Actions) -> Unit,
    sharedViewmodel: SharedViewmodel
) {
    composable(
        route = Constants.TASK_SCREEN,
        arguments = listOf(
            navArgument(
                Constants.TASK_ARGUMENT_KEY
            ) {
                type = NavType.IntType
            })
    ) { navBackStackEntry ->
        val taskId = navBackStackEntry.arguments!!.getInt(Constants.TASK_ARGUMENT_KEY)
        LaunchedEffect(key1 = taskId){
            sharedViewmodel.getSelectedTask(taskId = taskId)
        }
        val selectedTask by sharedViewmodel.selectedTask.collectAsState()

        // bug was encountered here, first taskid was provided -> this causes raised condition bug, meaning
        // that sometimes launched effect is run before we get the selectedTask.
        // To avoid this we change the launchedEffect's key to selectedTask.
        // By this everytime the key changes i.e. after getting our selectedTask, launchedEffect will be triggered.
        LaunchedEffect(key1 = selectedTask) {
            if (selectedTask != null || taskId == -1) {
                sharedViewmodel.updateTaskFields(selectedTask = selectedTask)
            }
        }

        TaskScreen(
            navigateToListScreen = navigateToListScreen,
            selectedTask = selectedTask,
            sharedViewModel = sharedViewmodel
        )
    }
}