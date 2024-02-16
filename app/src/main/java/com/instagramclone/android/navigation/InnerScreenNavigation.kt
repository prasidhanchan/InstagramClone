package com.instagramclone.android.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.firebase.auth.FirebaseAuth
import com.instagramclone.home.HomeScreen
import com.instagramclone.home.HomeViewModel
import com.instagramclone.profile.EditProfileScreen
import com.instagramclone.profile.ProfileScreen
import com.instagramclone.profile.ProfileViewModel
import com.instagramclone.profile.UiState

@Composable
fun InnerScreenNavigation(
    navHostController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel(),
    innerPadding: PaddingValues
) {
    val currentUser = FirebaseAuth.getInstance().currentUser

    NavHost(
        navController = navHostController,
        startDestination = NavScreens.HomeScreen.route
    ) {
        composable(NavScreens.HomeScreen.route) {
            val uiState by viewModel.uiState.collectAsState()
            HomeScreen(
                innerPadding = innerPadding,
                uiState = uiState,
                currentUserId = currentUser?.uid!!,
                onLikeClicked = {  },
                onUnLikeClicked = {  },
                onSendClicked = {  },
                onSaveClicked = {  },
                onUsernameClicked = {  },
            )
        }
        composable(NavScreens.ProfileScreen.route) {
            val viewModelProfile: ProfileViewModel = hiltViewModel()
            val uiState by viewModelProfile.uiState.collectAsState()
            ProfileScreen(
                innerPadding = innerPadding,
                uiState = uiState,
                onEditProfileClick = { navHostController.navigate(NavScreens.EditProfileScreen.route) },
                onMoreClick = {  }
            )
        }
        composable(NavScreens.EditProfileScreen.route) {
            EditProfileScreen(
                innerPadding = innerPadding,
                uiState = UiState(
                    username = "pra_sidh_22",
                    name = "Prasidh Gopal Anchan",
                    bio = "Android developer",
                    links = "https://www.linktr.ee/prasidhanchan"
                ),
                onClickEditText = { /*TODO*/ },
                onBackClick = { navHostController.popBackStack() }
            )
        }
    }
}