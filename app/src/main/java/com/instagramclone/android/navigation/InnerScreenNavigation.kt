package com.instagramclone.android.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.rememberLazyListState
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
    viewModelHome: HomeViewModel,
    viewModelProfile: ProfileViewModel = hiltViewModel(),
    innerPadding: PaddingValues
) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val context = LocalContext.current

    NavHost(
        navController = navHostController,
        startDestination = NavScreens.HomeScreen.route
    ) {
        composable(NavScreens.HomeScreen.route) {
            val uiState by viewModelHome.uiState.collectAsState()

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
            val uiState by viewModelProfile.uiState.collectAsState()
            val scrollState = rememberLazyListState()

            LaunchedEffect(key1 = uiState.isUserDetailChanged) {
                if (uiState.isUserDetailChanged) {
                    viewModelProfile.getUserData()
                    viewModelProfile.setIsUserDetailChanged(false)
                }
            }

            LaunchedEffect(key1 = uiState.showPostScreen) {
                if (uiState.postIndex != null) {
                    scrollState.scrollToItem(uiState.postIndex!!)
                }
            }

            ProfileScreen(
                innerPadding = innerPadding,
                uiState = uiState,
                currentUserId = currentUser?.uid!!,
                scrollState = scrollState,
                onFollowClick = { },
                onLikeClicked = { },
                onUnlikeClicked = { },
                onSendClicked = { },
                onSaveClicked = { },
                onUsernameClicked = { },
                onEditProfileClick = { navHostController.navigate(NavScreens.EditProfileScreen.route) },
                onMoreClick = { },
                onPostClick = { postIndex ->
                    viewModelProfile.setShowPostScreen(value = true, postIndex = postIndex)
                },
                setShowPostScreen = {
                    viewModelProfile.setShowPostScreen(value = it, postIndex = 0)
                }
            )
        }
        composable(NavScreens.EditProfileScreen.route) {
            val uiState by viewModelProfile.uiState.collectAsState()

            LaunchedEffect(key1 = uiState.isUserDetailChanged) {
                if (uiState.isUserDetailChanged) {
                    viewModelProfile.getUserData()
                }
            }

            EditProfileScreen(
                innerPadding = innerPadding,
                uiState = uiState,
                onClickEditText = { text -> navHostController.navigate(NavScreens.EditTextScreen.route + "/$text") },
                onDeleteProfileClick = {
                    viewModelProfile.updateUserDetails(
                        text = context.getString(R.string.profileimage),
                        value = "",
                        context = context,
                        onSuccess = {
                            viewModelProfile.setIsUserDetailChanged(true)
                            viewModelHome.getUserData()
                        }
                    )
                },
                setNewImage = { newImage ->
                    viewModelProfile.setNewImage(newImage = newImage)
                },
                onUploadClicked = {
                    viewModelProfile.convertToUrl(
                        newImage = uiState.newProfileImage!!,
                        onSuccess = { downloadUrl ->
                            viewModelProfile.updateUserDetails(
                                text = context.getString(R.string.profileimage),
                                value = downloadUrl,
                                context = context,
                                onSuccess = {
                                    viewModelProfile.setIsUserDetailChanged(true)
                                    viewModelHome.getUserData()
                                }
                            )
                            viewModelProfile.setNewImage(null)
                        }
                    )
                },
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
            val uiState by viewModelProfile.uiState.collectAsState()
            val text = backStack.arguments?.getString("text")
            val usernames by viewModelProfile.usernames.collectAsState()

            val scope = rememberCoroutineScope()

            LaunchedEffect(key1 = uiState.isLoading) {
                when (text) {
                    context.getString(R.string.name) -> viewModelProfile.setText(text = uiState.name)
                    context.getString(R.string.username) -> viewModelProfile.setText(text = uiState.username)
                    context.getString(R.string.bio) -> viewModelProfile.setText(text = uiState.bio)
                    context.getString(R.string.links) -> viewModelProfile.setText(text = uiState.links)
                    else -> viewModelProfile.setText(uiState.gender)
                }
            }

            LaunchedEffect(key1 = Unit) {
                viewModelProfile.getUserData()
                if (text == context.getString(R.string.username)) {
                    viewModelProfile.getAllUsernames()
                }
            }

            EditTextScreen(
                innerPadding = innerPadding,
                text = text!!,
                onValueChange = {
                    viewModelProfile.setText(it)
                    if (text == context.getString(R.string.username)) {
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
                isUsernameAvailable = (uiState.username == uiState.textState ||
                        usernames?.contains(uiState.textState) == false),
                isUpdating = uiState.isUpdating,
                onCancelClick = {
                    navHostController.popBackStack()
                    viewModelProfile.clearText()
                    viewModelProfile.clearError()
                },
                onDoneClick = {
                    when (text) {
                        context.getString(R.string.name) -> {
                            viewModelProfile.updateUserDetails(
                                text = text,
                                value = uiState.textState,
                                context = context,
                                onSuccess = {
                                    viewModelProfile.setIsUserDetailChanged(true)
                                    navHostController.popBackStack()
                                    viewModelProfile.clearText()
                                }
                            )
                        }

                        context.getString(R.string.username) -> {
                            if (uiState.textState.matches(Regex("^[a-zA-Z0-9_.]+|[a-zA-Z]+\$\n"))) {
                                viewModelProfile.updateUserDetails(
                                    text = text,
                                    value = uiState.textState,
                                    context = context,
                                    onSuccess = {
                                        viewModelProfile.setIsUserDetailChanged(true)
                                        navHostController.popBackStack()
                                        viewModelProfile.clearText()
                                    }
                                )
                            } else {
                                viewModelProfile.setError(context.getString(R.string.username_format_incorrect))
                            }
                        }

                        else -> {
                            viewModelProfile.clearError()
                            viewModelProfile.updateUserDetails(
                                text = text,
                                value = uiState.textState,
                                context = context,
                                onSuccess = {
                                    viewModelProfile.setIsUserDetailChanged(true)
                                    navHostController.popBackStack()
                                    viewModelProfile.clearText()
                                }
                            )
                        }
                    }
                }
            )
        }
    }
}