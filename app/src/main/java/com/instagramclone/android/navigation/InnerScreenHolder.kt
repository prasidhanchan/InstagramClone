package com.instagramclone.android.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.instagramclone.home.HomeViewModel
import com.instagramclone.util.constants.Utils

@Composable
fun InnerScreenHolder(
    viewModel: HomeViewModel,
    profileImage: String?,
    navigateToLogin: () -> Unit
) {
    val navHostController = rememberNavController()
    var currentRoute by remember { mutableStateOf<NavScreens>(NavScreens.HomeScreen) }
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route

    val showBottomBar = when(currentDestination) {
        NavScreens.HomeScreen.route -> true
        NavScreens.SearchScreen.route -> true
        NavScreens.ReelsScreen.route -> true
        NavScreens.ProfileScreen.route -> true
        else -> false
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Utils.IgBlack,
        bottomBar = {
            if (showBottomBar) {
                IGBottomBar(
                    profileImage = profileImage,
                    navigateToRoute = { item ->
                        currentRoute = item
                        navHostController.navigate(item.route) {
                            popUpTo(navHostController.graph.startDestinationId)
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    navHostController = navHostController
                )
            }
        },
        content = { innerPadding ->
            InnerScreenNavigation(
                innerPadding = innerPadding,
                viewModelHome = viewModel,
                navHostController = navHostController,
                navigateToLogin = navigateToLogin
            )
        }
    )
}