package com.example.taskmanager.navigation.destinations

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.taskmanager.ui.screens.splash.SplashScreen
import com.example.taskmanager.utils.Constants.SPLASH_SCREEN

fun NavGraphBuilder.splashComposable(
    navigateToListScreen : () -> Unit
) {
    composable(
        route = SPLASH_SCREEN
    ) {
        SplashScreen(navigateToListScreen)
    }
}