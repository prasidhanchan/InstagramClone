package com.instagramclone.android.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.instagramclone.home.HomeScreen
import com.instagramclone.home.HomeViewModel

@Composable
fun InnerScreenNavigation(
    navHostController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel()
) {

    NavHost(
        navController = navHostController,
        startDestination = NavScreens.HomeScreen.route
    ) {
        composable(NavScreens.HomeScreen.route) {
            val uiState by viewModel.uiState.collectAsState()
            HomeScreen(
                uiState = uiState
            )
        }
    }
}