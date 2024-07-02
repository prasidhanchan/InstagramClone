package com.instagramclone.android.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.util.UnstableApi
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.instagramclone.profile.ProfileViewModel
import com.instagramclone.util.constants.Utils

@UnstableApi
@Composable
fun InnerScreenHolder(
    viewModelProfile: ProfileViewModel = hiltViewModel(),
    navigateToLogin: () -> Unit
) {
    val navHostController = rememberNavController()
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route

    val uiState by viewModelProfile.uiState.collectAsState()

    val showBottomBar = when(currentDestination) {
        Routes.HomeScreen.route -> true
        Routes.SearchScreen.route -> true
        Routes.ReelsScreen.route -> true
        Routes.MyProfileScreen.route -> true
        else -> false
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Utils.IgBackground,
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
                viewModelProfile = viewModelProfile,
                navHostController = navHostController,
                navigateToLogin = navigateToLogin
            )
        }
    )
}