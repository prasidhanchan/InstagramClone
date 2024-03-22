package com.instagramclone.android.navigation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseAuth
import com.instagramclone.home.HomeScreen
import com.instagramclone.home.HomeViewModel
import com.instagramclone.post.UploadContentScreen
import com.instagramclone.post.UploadContentViewModel
import com.instagramclone.post.UploadPostScreen
import com.instagramclone.profile.EditProfileScreen
import com.instagramclone.profile.EditTextScreen
import com.instagramclone.profile.MyProfileScreen
import com.instagramclone.profile.PostsScreen
import com.instagramclone.profile.ProfileViewModel
import com.instagramclone.profile.SettingsAndPrivacyScreen
import com.instagramclone.profile.UserProfileScreen
import com.instagramclone.ui.R
import com.instagramclone.util.models.Post
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun InnerScreenNavigation(
    navHostController: NavHostController,
    viewModelHome: HomeViewModel,
    viewModelProfile: ProfileViewModel = hiltViewModel(),
    viewModelUpload: UploadContentViewModel = hiltViewModel(),
    innerPadding: PaddingValues,
    navigateToLogin: () -> Unit
) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val context = LocalContext.current

    NavHost(
        navController = navHostController,
        startDestination = NavScreens.HomeScreen.route
    ) {
        composable(
            route = NavScreens.HomeScreen.route,
            enterTransition = {
                fadeIn(
                    animationSpec = tween(350)
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(350)
                )
            }
        ) {
            val uiState by viewModelHome.uiState.collectAsState()
            val uiStateProfile by viewModelProfile.uiState.collectAsState()

            HomeScreen(
                innerPadding = innerPadding,
                uiState = uiState,
                selectedPost = uiStateProfile.selectedPost,
                currentUserId = currentUser?.uid!!,
                onLikeClick = {
                    viewModelProfile.like(
                        userId = it.userId,
                        timeStamp = it.timeStamp,
                        onSuccess = {

                        }
                    )
                },
                onUnLikeClick = {
                    viewModelProfile.unLike(
                        userId = it.userId,
                        timeStamp = it.timeStamp,
                        onSuccess = {

                        }
                    )
                },
                onSendClick = { },
                onSaveClick = { },
                onUnfollowClick = { },
                onDeletePostClick = { post ->
                    viewModelProfile.deletePost(
                        post = post,
                        onSuccess = {
                            viewModelProfile.getMyData()
                        }
                    )
                },
                setSelectedPost = { viewModelProfile.setSelectedPost(it) },
                onUsernameClick = { userId ->
                    navHostController.navigate(NavScreens.UserProfileScreen.route + "/$userId")
                }
            )
        }
        composable(
            route = NavScreens.MyProfileScreen.route,
            enterTransition = {
                fadeIn(
                    animationSpec = tween(350)
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(350)
                )
            }
        ) {
            val uiState by viewModelProfile.uiState.collectAsState()

            LaunchedEffect(key1 = uiState.isUserDetailChanged) {
                if (uiState.isUserDetailChanged) {
                    viewModelProfile.getMyData()
                    viewModelProfile.getMyPosts()
                    viewModelProfile.setIsUserDetailChanged(false)
                }
            }

            MyProfileScreen(
                innerPadding = innerPadding,
                uiState = uiState,
                selectedPost = uiState.selectedPost,
                onFollowClick = { }, // Not required
                onUnfollowClick = { }, // Not required
                onDeletePostClick = { post ->
                    viewModelProfile.deletePost(
                        post = post,
                        onSuccess = {
                            viewModelProfile.getMyData()
                            viewModelHome.getAllPosts()
                            viewModelProfile.getMyPosts()
                        }
                    )
                },
                onEditProfileClick = { navHostController.navigate(NavScreens.EditProfileScreen.route) },
                navigateToPostsWithPostIndex = {
                    navHostController.navigate(
                        NavScreens.PostsScreen.route + "/${it.substringBefore("-")}/${it.substringAfter("-")}"
                    ) // userId-1
                },
                setSelectedPost = { viewModelProfile.setSelectedPost(it) },
                onSettingsAndPrivacyClicked = {
                    navHostController.navigate(NavScreens.SettingsAndPrivacyScreen.route)
                }
            )
        }
        composable(
            route = "${NavScreens.UserProfileScreen.route}/{userId}",
            arguments = listOf(
                navArgument("userId") {
                    type = NavType.StringType
                    nullable = true
                }
            ),
            enterTransition = {
                slideInHorizontally(
                    animationSpec = tween(350),
                    initialOffsetX = { it }
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    animationSpec = tween(350),
                    targetOffsetX = { it }
                )
            }
        ) { backStack ->
            val uiState by viewModelProfile.uiState.collectAsState()

            val userId = backStack.arguments?.getString("userId")

            LaunchedEffect(key1 = Unit) {
                if (uiState.selectedUserPosts.isEmpty()) {
                    viewModelProfile.getUserProfile(userId = userId!!)
                    viewModelProfile.getUserPosts(userId = userId)
                }
            }

            UserProfileScreen(
                innerPadding = innerPadding,
                uiState = uiState,
                currentUserId = currentUser?.uid!!,
                onFollowClick = {
                    viewModelProfile.follow(
                        userId = userId!!,
                        onSuccess = {
                            viewModelProfile.setIsFollowing(isFollowing = true) // For PostsScreen follow click
                            viewModelProfile.setIsUserDetailChanged(value = true)
                        }
                    )
                },
                onUnfollowClick = {
                    viewModelProfile.unFollow(
                        userId = userId!!,
                        onSuccess = {
                            viewModelProfile.setIsUserDetailChanged(value = true)
                        }
                    )
                },
                navigateToPostsWithPostIndex = {
                    navHostController.navigate(
                        NavScreens.PostsScreen.route + "/${it.substringBefore("-")}/${it.substringAfter("-")}"
                    ) // userId-1
                },
                onEditProfileClick = {
                    navHostController.navigate(NavScreens.EditProfileScreen.route)
                },
                setIsFollowing = {
                    viewModelProfile.setIsFollowing(it)
                },
                onBackClick = {
                    navHostController.popBackStack()
                    viewModelProfile.clearUserProfile()
                }
            )
        }
        composable(
            route = "${NavScreens.PostsScreen.route}/{userId}/{postIndex}",
            arguments = listOf(
                navArgument("userId") {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("postIndex") {
                    type = NavType.IntType
                }
            ),
            enterTransition = {
                slideInHorizontally(
                    animationSpec = tween(350),
                    initialOffsetX = { it }
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    animationSpec = tween(350),
                    targetOffsetX = { it }
                )
            }
        ) { backStack ->
            val uiState by viewModelProfile.uiState.collectAsState()
            val scrollState = rememberLazyListState()

            val userId = backStack.arguments?.getString("userId")
            val postIndex = backStack.arguments?.getInt("postIndex")

            LaunchedEffect(key1 = Unit) {
                if (postIndex != null) {
                    scrollState.scrollToItem(postIndex)
                }
            }

            PostsScreen(
                innerPadding = innerPadding,
                uiState = uiState,
                currentUserId = currentUser?.uid!!,
                isMyProfile = currentUser.uid == userId,
                scrollState = scrollState,
                onFollowClick = {
                    viewModelProfile.follow(
                        userId = userId!!,
                        onSuccess = {
                            viewModelProfile.setIsUserDetailChanged(value = true)
                            viewModelHome.getAllPosts()
                        }
                    )
                },
                onLikeClick = {
                    viewModelProfile.like(
                        userId = it.userId,
                        timeStamp = it.timeStamp,
                        onSuccess = {
                            if (currentUser.uid == userId) {
                                viewModelProfile.setIsUserDetailChanged(value = true)
                            }
                            viewModelHome.getAllPosts()
                        }
                    )
                },
                onUnlikeClick = {
                    viewModelProfile.unLike(
                        userId = it.userId,
                        timeStamp = it.timeStamp,
                        onSuccess = {
                            if (currentUser.uid == userId) {
                                viewModelProfile.setIsUserDetailChanged(value = true)
                            }
                            viewModelHome.getAllPosts()
                        }
                    )
                },
                onSendClick = { },
                onSaveClick = { },
                onUnfollowClick = {
                    viewModelProfile.unFollow(
                        userId = userId!!,
                        onSuccess = {
                            viewModelProfile.setIsUserDetailChanged(value = true)
                            viewModelHome.getAllPosts()
                        }
                    )
                },
                onDeletePostClick = {
                    viewModelProfile.deletePost(
                        post = it,
                        onSuccess = {
                            viewModelHome.getAllPosts()
                        }
                    )
                },
                onUsernameClick = { id ->
                    navHostController.navigate(NavScreens.UserProfileScreen.route + "/$id")
                },
                onBackClick = {
                    navHostController.popBackStack()
                }
            )
        }
        composable(
            route = NavScreens.EditProfileScreen.route,
            enterTransition = {
                fadeIn(
                    animationSpec = tween(350)
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(350)
                )
            }
        ) {
            val uiState by viewModelProfile.uiState.collectAsState()

            LaunchedEffect(key1 = uiState.isUserDetailChanged) {
                if (uiState.isUserDetailChanged) {
                    viewModelProfile.getMyData()
                    viewModelProfile.getMyPosts()
                }
            }

            val scope = rememberCoroutineScope()

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
                onPasswordChange = {
                    viewModelProfile.setPassword(value = it)

                    scope.launch {
                        delay(1000L)
                        viewModelProfile.clearError()
                    }
                },
                onNewPasswordChange = {
                    viewModelProfile.setNewPassword(value = it)

                    scope.launch {
                        delay(1000L)
                        viewModelProfile.clearError()
                    }
                },
                onRePasswordChange = {
                    viewModelProfile.setRePassword(value = it)

                    scope.launch {
                        delay(1000L)
                        viewModelProfile.clearError()
                    }
                },
                onChangePasswordClick = {
                    viewModelProfile.changePassword(
                        currentPassword = uiState.password,
                        passwordState = uiState.passwordState,
                        newPasswordState = uiState.newPasswordState,
                        rePasswordState = uiState.newPasswordState,
                        context = context,
                        onSuccess = {
                            viewModelProfile.updateUserDetails(
                                text = context.getString(R.string.password),
                                value = uiState.newPasswordState.trim(),
                                context = context,
                                onSuccess = {
                                    viewModelProfile.logOut()
                                    navigateToLogin()
                                    Toast.makeText(
                                        context,
                                        context.getString(R.string.your_password_has_been_reset),
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            )
                        }
                    )
                },
                onBackClick = {
                    navHostController.popBackStack()
                }
            )
        }
        composable(
            route = "${NavScreens.EditTextScreen.route}/{text}",
            arguments = listOf(
                navArgument("text") {
                    type = NavType.StringType
                }
            ),
            enterTransition = {
                fadeIn(
                    animationSpec = tween(350)
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(350)
                )
            }
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
                viewModelProfile.getMyData()
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
                onGenderSelected = { viewModelProfile.setGender(it) },
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

                        context.getString(R.string.gender) -> {
                            viewModelProfile.updateUserDetails(
                                text = text,
                                value = uiState.gender,
                                context = context,
                                onSuccess = {
                                    viewModelProfile.setIsUserDetailChanged(true)
                                    navHostController.popBackStack()
                                }
                            )
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
        composable(
            route = NavScreens.SettingsAndPrivacyScreen.route,
            enterTransition = {
                fadeIn(
                    animationSpec = tween(350)
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(350)
                )
            }
        ) {
            val uiState by viewModelProfile.uiState.collectAsState()

            SettingsAndPrivacyScreen(
                innerPadding = innerPadding,
                uiState = uiState,
                onLogoutClick = {
                    viewModelProfile.logOut()
                    navigateToLogin()
                },
                onPasswordChange = { viewModelProfile.setPassword(value = it) },
                onNewPasswordChange = { viewModelProfile.setNewPassword(value = it) },
                onRePasswordChange = { viewModelProfile.setRePassword(value = it) },
                onChangePasswordClick = {
                    viewModelProfile.changePassword(
                        currentPassword = uiState.password,
                        passwordState = uiState.passwordState,
                        newPasswordState = uiState.newPasswordState,
                        rePasswordState = uiState.newPasswordState,
                        context = context,
                        onSuccess = {
                            viewModelProfile.updateUserDetails(
                                text = context.getString(R.string.password),
                                value = uiState.newPasswordState.trim(),
                                context = context,
                                onSuccess = {
                                    viewModelProfile.logOut()
                                    navigateToLogin()
                                    Toast.makeText(
                                        context,
                                        context.getString(R.string.your_password_has_been_reset),
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            )
                        }
                    )
                },
                onBackClick = {
                    navHostController.popBackStack()
                }
            )
        }
        composable(
            route = NavScreens.UploadContentScreen.route,
            enterTransition = {
                slideInHorizontally(
                    animationSpec = tween(),
                    initialOffsetX = { -it }
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    animationSpec = tween(),
                    targetOffsetX = { -it }
                )
            }
        ) {
            val uiState by viewModelUpload.uiState.collectAsState()

            val permissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission()
            ) { }

            LaunchedEffect(key1 = Unit) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (
                        ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.READ_MEDIA_IMAGES
                        ) == PackageManager.PERMISSION_DENIED
                    ) {
                        permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                    }
                } else {
                    if (ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ) == PackageManager.PERMISSION_DENIED
                    ) {
                        permissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    }
                }
            }

            UploadContentScreen(
                innerPadding = innerPadding,
                uiState = uiState,
                onImageSelected = { viewModelUpload.setImage(image = it) },
                onNextClick = { navHostController.navigate(NavScreens.UploadPostScreen.route) },
                onBackClick = {
                    navHostController.popBackStack()
                    viewModelUpload.setImage(uiState.images.firstOrNull())
                }
            )
        }
        composable(
            route = NavScreens.UploadPostScreen.route,
            enterTransition = {
                fadeIn(
                    animationSpec = tween()
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween()
                )
            }
        ) {
            val uiState by viewModelUpload.uiState.collectAsState()
            val uiStateProfile by viewModelProfile.uiState.collectAsState()

            val timeStamp = System.currentTimeMillis()

            UploadPostScreen(
                innerPadding = innerPadding,
                uiState = uiState,
                onCaptionChange = { viewModelUpload.setCaption(caption = it) },
                onBackClick = { navHostController.popBackStack() },
                onShareClick = {
                    viewModelUpload.uploadPost(
                        post = Post(
                            profileImage = uiStateProfile.profileImage,
                            userId = currentUser?.uid!!,
                            username = uiStateProfile.username,
                            timeStamp = timeStamp,
                            isVerified = true, // TODO change
                            images = listOf(uiState.selectedImage?.data.toString()),
                            caption = uiState.caption
                        ),
                        onSuccess = {
                            navHostController.navigate(NavScreens.HomeScreen.route) {
                                popUpTo(navHostController.graph.startDestinationId)
                            }
                            viewModelUpload.setCaption(caption = "")
                        }
                    )
                }
            )
        }
    }
}