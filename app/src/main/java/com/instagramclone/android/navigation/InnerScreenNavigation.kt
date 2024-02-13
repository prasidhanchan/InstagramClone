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
            ProfileScreen(
                innerPadding = innerPadding,
                uiState = UiState(
                    username = "pra_sidh_22",
                    name = "Prasidh Gopal Anchan",
                    bio = "Android developer",
                    links = "https://www.linktr.ee/prasidhanchan",
                    myPosts = listOf(
                        "https://i.pinimg.com/564x/98/58/74/9858745cd157f2797065e639c5b3bf23.jpg",
                        "https://wallpaperaccess.in/public/uploads/preview/oshi-no-ko-yoasobi-anime-girl-wallpaper-s.jpg",
                        "https://cdn.hero.page/pfp/5bb14a97-d70c-4fa3-b462-3a8183481905-cool-one-piece-luffy-pfp-cool-anime-pfp-1.png",
                        "https://www.animeinformer.com/wp-content/uploads/2022/08/demon-slayer-pfp.png.webp"
                    )
                ),
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