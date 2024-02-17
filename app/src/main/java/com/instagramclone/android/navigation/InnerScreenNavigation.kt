package com.instagramclone.android.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseAuth
import com.instagramclone.home.HomeScreen
import com.instagramclone.home.HomeViewModel
import com.instagramclone.profile.EditProfileScreen
import com.instagramclone.profile.EditTextScreen
import com.instagramclone.profile.ProfileScreen
import com.instagramclone.profile.ProfileViewModel
import com.instagramclone.ui.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
                onLikeClicked = { },
                onUnLikeClicked = { },
                onSendClicked = { },
                onSaveClicked = { },
                onUsernameClicked = { },
            )
        }
        composable(NavScreens.ProfileScreen.route) {
            val viewModelProfile: ProfileViewModel = hiltViewModel()
            val uiState by viewModelProfile.uiState.collectAsState()

            LaunchedEffect(key1 = Unit) {
                viewModelProfile.getUserData()
            }

            ProfileScreen(
                innerPadding = innerPadding,
                uiState = uiState,
                onEditProfileClick = { navHostController.navigate(NavScreens.EditProfileScreen.route) },
                onMoreClick = { }
            )
        }
        composable(NavScreens.EditProfileScreen.route) {
            val viewModelProfile: ProfileViewModel = hiltViewModel()
            val uiState by viewModelProfile.uiState.collectAsState()

            LaunchedEffect(key1 = Unit) {
                viewModelProfile.getUserData()
            }

            EditProfileScreen(
                innerPadding = innerPadding,
                uiState = uiState,
                onClickEditText = { text -> navHostController.navigate(NavScreens.EditTextScreen.route + "/$text") },
                onBackClick = { navHostController.popBackStack() }
            )
        }
        composable(
            "${NavScreens.EditTextScreen.route}/{text}",
            arguments = listOf(
                navArgument("text") {
                    type = NavType.StringType
                }
            )
        ) { backStack ->
            val viewModelProfile: ProfileViewModel = hiltViewModel()
            val uiState by viewModelProfile.uiState.collectAsState()
            val text = backStack.arguments?.getString("text")
            val usernames by viewModelProfile.usernames.collectAsState()

            val context = LocalContext.current
            val scope = rememberCoroutineScope()

            LaunchedEffect(key1 = uiState.isLoading) {
                when (text) {
                    "Name" -> viewModelProfile.setText(text = uiState.name)
                    "Username" -> viewModelProfile.setText(text = uiState.username)
                    "Bio" -> viewModelProfile.setText(text = uiState.bio)
                    else -> viewModelProfile.setText(uiState.links)
                }
            }

            LaunchedEffect(key1 = Unit) {
                viewModelProfile.getUserData()
                viewModelProfile.getAllUsernames()
            }

            EditTextScreen(
                innerPadding = innerPadding,
                text = text!!,
                onValueChange = {
                    viewModelProfile.setText(it)
                    if (text == "Username") {
                        scope.launch {
                            delay(1000L)
                            if (uiState.textState == uiState.username || usernames?.contains(uiState.textState) == false) {
                                viewModelProfile.clearError()
                            } else {
                                viewModelProfile.setError(error = context.getString(R.string.username_already_exists))
                            }
                        }
                    }
                },
                uiState = uiState,
                isUsernameAvailable = (uiState.username == uiState.textState || usernames?.contains(
                    uiState.textState
                ) == false),
                onCancelClick = {
                    navHostController.popBackStack()
                    viewModelProfile.clearText()
                    viewModelProfile.clearError()
                },
                onDoneClick = {
                    when (text) {
                        "Name" -> {
                            viewModelProfile.updateUserDetails(
                                text = text,
                                value = uiState.textState,
                                onSuccess = {
                                    navHostController.popBackStack()
                                }
                            )
                            viewModelProfile.clearText()
                        }

                        "Username" -> {
                            if (uiState.textState.matches(Regex("^[a-zA-Z0-9_.]+|[a-zA-Z]+\$\n"))) {
                                viewModelProfile.updateUserDetails(
                                    text = text,
                                    value = uiState.textState,
                                    onSuccess = {
                                        navHostController.popBackStack()
                                    }
                                )
                                viewModelProfile.clearText()
                            } else {
                                viewModelProfile.setError(context.getString(R.string.username_format_incorrect))
                            }
                        }

                        else -> {
                            viewModelProfile.clearError()
                            viewModelProfile.updateUserDetails(
                                text = text,
                                value = uiState.textState,
                                onSuccess = {
                                    navHostController.popBackStack()
                                }
                            )
                        }
                    }
                }
            )
        }
    }
}