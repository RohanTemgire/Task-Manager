package com.example.taskmanager.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.taskmanager.navigation.destinations.listComposable
import com.example.taskmanager.navigation.destinations.splashComposable
import com.example.taskmanager.navigation.destinations.taskComposable
import com.example.taskmanager.ui.viewmodels.SharedViewmodel
import com.example.taskmanager.utils.Constants.LIST_SCREEN
import com.example.taskmanager.utils.Constants.SPLASH_SCREEN

@Composable
fun SetupNavigation(
    navController: NavHostController,
    sharedViewmodel: SharedViewmodel
) {
    val screen = remember(navController) {
        Screens(navController = navController)
    }

    NavHost(
        navController = navController,
        startDestination = SPLASH_SCREEN
    ) {
        splashComposable(
            navigateToListScreen = screen.splash
        )

        listComposable(
            navigateToTaskScreen = screen.task,
            sharedViewmodel
        )
        taskComposable(
            navigateToListScreen = screen.list,
            sharedViewmodel = sharedViewmodel
        )
    }
}