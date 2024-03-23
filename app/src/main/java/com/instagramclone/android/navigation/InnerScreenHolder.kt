package com.instagramclone.android.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.instagramclone.profile.ProfileViewModel
import com.instagramclone.util.constants.Utils

@Composable
fun InnerScreenHolder(
    viewModel: ProfileViewModel = hiltViewModel(),
    navigateToLogin: () -> Unit
) {
    val navHostController = rememberNavController()
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route

    val uiState by viewModel.uiState.collectAsState()

    val showBottomBar = when(currentDestination) {
        NavScreens.HomeScreen.route -> true
        NavScreens.SearchScreen.route -> true
        NavScreens.ReelsScreen.route -> true
        NavScreens.MyProfileScreen.route -> true
        else -> false
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Utils.IgBlack,
        bottomBar = {
            if (showBottomBar) {
                IGBottomBar(
                    profileImage = uiState.profileImage,
                    navHostController = navHostController
                )
            }
        },
        content = { innerPadding ->
            InnerScreenNavigation(
                innerPadding = innerPadding,
                viewModelProfile = viewModel,
                navHostController = navHostController,
                navigateToLogin = navigateToLogin
            )
        }
    )
}