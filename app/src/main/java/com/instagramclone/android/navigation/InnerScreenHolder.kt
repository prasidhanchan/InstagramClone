package com.instagramclone.android.navigation

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun InnerScreenHolder(
    profileImage: String?,
) {
    val navHostController = rememberNavController()
    var currentRoute by remember { mutableStateOf<NavScreens>(NavScreens.HomeScreen) }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            IGBottomBar(
                profileImage = profileImage,
                navigateToRoute = { item ->
                    Log.d("IGNAV", "InnerScreenHolder: $profileImage")
                    currentRoute = item
                    navHostController.navigate(item.route) {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(navHostController.graph.startDestinationId)
                    }
                },
                navHostController = navHostController
            )
        },
        content = {
            InnerScreenNavigation(navHostController = navHostController)
        }
    )
}