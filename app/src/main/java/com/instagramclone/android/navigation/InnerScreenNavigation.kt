package com.instagramclone.android.navigation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseAuth
import com.instagramclone.android.BuildConfig.IG_AVATAR
import com.instagramclone.home.HomeScreen
import com.instagramclone.home.HomeViewModel
import com.instagramclone.home.VideoPlayerViewModel
import com.instagramclone.profile.EditProfileScreen
import com.instagramclone.profile.EditTextScreen
import com.instagramclone.profile.MyProfileScreen
import com.instagramclone.profile.PostsScreen
import com.instagramclone.profile.ProfileViewModel
import com.instagramclone.profile.SettingsAndPrivacyScreen
import com.instagramclone.profile.UserProfileScreen
import com.instagramclone.ui.R
import com.instagramclone.upload.AddCaptionScreen
import com.instagramclone.upload.AddToStoryScreen
import com.instagramclone.upload.UploadContentScreen
import com.instagramclone.upload.UploadContentViewModel
import com.instagramclone.util.models.Post
import com.instagramclone.util.models.Story
import com.instagramclone.util.models.UserStory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@UnstableApi
@Composable
fun InnerScreenNavigation(
    innerPadding: PaddingValues,
    navHostController: NavHostController,
    viewModelHome: HomeViewModel = hiltViewModel(),
    viewModelUpload: UploadContentViewModel = hiltViewModel(),
    viewModelProfile: ProfileViewModel,
    navigateToLogin: () -> Unit
) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val context = LocalContext.current

    NavHost(
        navController = navHostController,
        startDestination = Routes.HomeScreen.route
    ) {
        composable(
            route = Routes.HomeScreen.route,
            enterTransition = {
                fadeIn(animationSpec = tween(350))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(350))
            }
        ) {
            val uiState by viewModelHome.uiState.collectAsState()
            val uiStateProfile by viewModelProfile.uiState.collectAsState()

            val viewModelPlayer: VideoPlayerViewModel = hiltViewModel()
            val currentPosition by viewModelPlayer.currentPosition.collectAsState()
            val duration by viewModelPlayer.duration.collectAsState()
            var isLaunched by rememberSaveable { mutableStateOf(false) }

            LaunchedEffect(key1 = Unit, key2 = uiStateProfile.isUserDetailChanged) {
                if (!isLaunched || uiStateProfile.isUserDetailChanged) {
                    viewModelProfile.getMyData()
                    delay(3000L)
                    if (uiStateProfile.following.isNotEmpty()) {
                        viewModelHome.getPosts(following = uiStateProfile.following)
                        viewModelHome.getStories(following = uiStateProfile.following)
                        isLaunched = true
                    }
                }
            }

            HomeScreen(
                innerPadding = innerPadding,
                uiState = uiState,
                following = uiStateProfile.following,
                profileImage = uiStateProfile.profileImage,
                selectedPost = uiStateProfile.selectedPost,
                currentUserId = currentUser?.uid ?: "",
                exoPlayer = viewModelPlayer.exoPlayer,
                currentPosition = currentPosition,
                duration = duration,
                onWatchAgainClick = { url ->
                    viewModelPlayer.startPlayer(url = url)
                },
                onLikeClick = { post ->
                    viewModelProfile.like(
                        userId = post.userId,
                        timeStamp = post.timeStamp
                    )
                },
                onUnLikeClick = { post ->
                    viewModelProfile.unLike(
                        userId = post.userId,
                        timeStamp = post.timeStamp
                    )
                },
                onSendClick = { },
                onSaveClick = { },
                onUnfollowClick = { },
                onDeletePostClick = { post ->
                    viewModelProfile.deletePost(
                        post = post,
                        onSuccess = { viewModelProfile.getMyData() }
                    )
                },
                onDeleteStoryClick = { story ->
                    viewModelHome.deleteStory(
                        story = story,
                        onSuccess = { viewModelHome.setShowStoryScreen(false) }
                    )
                },
                setSelectedPost = { post -> viewModelProfile.setSelectedPost(post) },
                onUsernameClick = { userId ->
                    navHostController.navigate(Routes.UserProfileScreen.route + "/$userId")
                },
                onAddStoryClick = {
                    navHostController.navigate("${Routes.UploadContentScreen.route}/STORY")
                },
                updateViews = viewModelHome::updateStoryViews,
                setShowStoryScreen = viewModelHome::setShowStoryScreen
            )
        }

        composable(
            route = Routes.MyProfileScreen.route,
            enterTransition = {
                fadeIn(animationSpec = tween(350))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(350))
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
                            viewModelHome.getPosts(following = uiState.following)
                            viewModelProfile.getMyPosts()
                        }
                    )
                },
                onEditProfileClick = { navHostController.navigate(Routes.EditProfileScreen.route) },
                navigateToPostsWithPostIndex = {
                    navHostController.navigate(
                        Routes.PostsScreen.route +
                                "/${it.substringBefore("-")}/${it.substringAfter("-")}"
                    ) // userId-1
                },
                setSelectedPost = { post -> viewModelProfile.setSelectedPost(post) },
                onSettingsAndPrivacyClicked = {
                    navHostController.navigate(Routes.SettingsAndPrivacyScreen.route)
                }
            )
        }

        composable(
            route = "${Routes.UserProfileScreen.route}/{userId}",
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
                currentUserId = currentUser?.uid ?: "",
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
                        Routes.PostsScreen.route +
                                "/${it.substringBefore("-")}/${it.substringAfter("-")}"
                    ) // userId-1
                },
                onEditProfileClick = {
                    navHostController.navigate(Routes.EditProfileScreen.route)
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
            route = "${Routes.PostsScreen.route}/{userId}/{postIndex}",
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
            val viewModelPlayer: VideoPlayerViewModel = hiltViewModel()
            val currentPosition by viewModelPlayer.currentPosition.collectAsState()
            val duration by viewModelPlayer.duration.collectAsState()

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
                currentUserId = currentUser?.uid ?: "",
                exoPlayer = viewModelPlayer.exoPlayer,
                currentPosition = currentPosition,
                duration = duration,
                onWatchAgainClick = { url ->
                    viewModelPlayer.startPlayer(url = url)
                },
                isMyProfile = currentUser?.uid == userId,
                scrollState = scrollState,
                onFollowClick = {
                    viewModelProfile.follow(
                        userId = userId!!,
                        onSuccess = {
                            viewModelProfile.setIsUserDetailChanged(value = true)
                        }
                    )
                },
                onLikeClick = { post ->
                    viewModelProfile.like(
                        userId = post.userId,
                        timeStamp = post.timeStamp,
                        onSuccess = {
                            if (currentUser?.uid == userId) {
                                viewModelProfile.setIsUserDetailChanged(value = true)
                            }
                        }
                    )
                },
                onUnlikeClick = {
                    viewModelProfile.unLike(
                        userId = it.userId,
                        timeStamp = it.timeStamp,
                        onSuccess = {
                            if (currentUser?.uid == userId) {
                                viewModelProfile.setIsUserDetailChanged(value = true)
                            }
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
                        }
                    )
                },
                onDeletePostClick = {
                    viewModelProfile.deletePost(
                        post = it,
                        onSuccess = {

                        }
                    )
                },
                onUsernameClick = { id ->
                    navHostController.navigate(Routes.UserProfileScreen.route + "/$id")
                }
            ) {
                navHostController.popBackStack()
            }
        }

        composable(
            route = Routes.EditProfileScreen.route,
            enterTransition = {
                fadeIn(animationSpec = tween(350))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(350))
            }
        ) {
            val uiState by viewModelProfile.uiState.collectAsState()

            LaunchedEffect(key1 = uiState.isUserDetailChanged) {
                if (uiState.isUserDetailChanged) {
                    viewModelProfile.getMyData()
                }
            }

            val scope = rememberCoroutineScope()

            EditProfileScreen(
                innerPadding = innerPadding,
                uiState = uiState,
                onClickEditText = { text -> navHostController.navigate(Routes.EditTextScreen.route + "/$text") },
                onDeleteProfileClick = {
                    viewModelProfile.updateUserDetails(
                        text = context.getString(R.string.profileimage),
                        value = IG_AVATAR,
                        context = context,
                        onSuccess = {
                            viewModelProfile.setIsUserDetailChanged(true)
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
            route = "${Routes.EditTextScreen.route}/{text}",
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
            route = Routes.SettingsAndPrivacyScreen.route,
            enterTransition = {
                fadeIn(animationSpec = tween(350))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(350))
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
            route = "${Routes.UploadContentScreen.route}/{text}",
            arguments = listOf(
                navArgument("text") {
                    type = NavType.StringType
                }
            ),
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
        ) { backStack ->
            val text = backStack.arguments?.getString("text")

            val uiState by viewModelUpload.uiState.collectAsState()
            val viewModelPlayer: VideoPlayerViewModel = hiltViewModel()

            val permissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestMultiplePermissions()
            ) { isAllowed ->
                if (isAllowed.all { it.value }) {
                    viewModelUpload.getMedia()
                } else {
                    Toast.makeText(
                        context,
                        "Please give access to photos and videos",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            LaunchedEffect(key1 = Unit) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (
                        ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.READ_MEDIA_IMAGES
                        ) == PackageManager.PERMISSION_DENIED ||
                        ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.READ_MEDIA_VIDEO
                        ) == PackageManager.PERMISSION_DENIED
                    ) {
                        permissionLauncher.launch(
                            arrayOf(
                                Manifest.permission.READ_MEDIA_IMAGES,
                                Manifest.permission.READ_MEDIA_VIDEO
                            )
                        )
                    } else {
                        viewModelPlayer.exoPlayer.clearMediaItems() // Clear Previous MediaItems from HomeScreen
                        viewModelUpload.getMedia()
                    }
                } else {
                    if (ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ) == PackageManager.PERMISSION_DENIED
                    ) {
                        permissionLauncher.launch(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE))
                    } else {
                        viewModelPlayer.exoPlayer.clearMediaItems() // Clear Previous MediaItems from HomeScreen
                        viewModelUpload.getMedia()
                    }
                }
            }

            // Play the first media on launch if it is a video
            LaunchedEffect(key1 = Unit) {
                delay(1000L)
                if (uiState.mediaList.isNotEmpty() && uiState.mediaList.first().duration != null) {
                    viewModelPlayer.startPlayer(url = uiState.mediaList.first().data.toString())
                }
            }

            UploadContentScreen(
                text = text!!,
                innerPadding = innerPadding,
                uiState = uiState,
                exoPlayer = viewModelPlayer.exoPlayer,
                onMediaSelected = { media ->
                    if (media.duration != null) viewModelPlayer.startPlayer(url = media.data.toString())
                    viewModelUpload.setMedia(media = media)
                },
                onStorySelected = { selectedMedia ->
                    viewModelUpload.setMedia(media = selectedMedia)
                    navHostController.navigate(Routes.AddToStoryScreen.route)
                },
                onPhotosClick = {
                    viewModelUpload.getImages()
                },
                onVideosClick = {
                    viewModelUpload.getVideos()
                },
                onNextClick = {
                    navHostController.navigate(Routes.AddCaptionScreen.route)
                },
                onBackClick = {
                    navHostController.popBackStack()
                    viewModelUpload.setMedia(uiState.mediaList.firstOrNull())
                }
            )
        }

        composable(
            route = Routes.AddCaptionScreen.route,
            enterTransition = {
                fadeIn(animationSpec = tween())
            },
            exitTransition = {
                fadeOut(animationSpec = tween())
            }
        ) {
            val uiState by viewModelUpload.uiState.collectAsState()
            val uiStateProfile by viewModelProfile.uiState.collectAsState()

            val timeStamp = System.currentTimeMillis()

            AddCaptionScreen(
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
                            mediaList = listOf(uiState.selectedMedia?.data.toString()),
                            caption = uiState.caption,
                            mimeType = uiState.selectedMedia?.mimeType ?: ""
                        ),
                        onSuccess = {
                            navHostController.navigate(Routes.HomeScreen.route) {
                                popUpTo(navHostController.graph.startDestinationId) {
                                    inclusive = true
                                }

                                launchSingleTop = true
                            }
                            viewModelUpload.setCaption(caption = "")
                        }
                    )
                }
            )
        }

        composable(
            route = Routes.AddToStoryScreen.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            val uiState by viewModelUpload.uiState.collectAsState()
            val uiStateProfile by viewModelProfile.uiState.collectAsState()
            val viewModelPlayer: VideoPlayerViewModel = hiltViewModel()

            val timeStamp = System.currentTimeMillis()


            LaunchedEffect(key1 = Unit) {
                if (uiState.selectedMedia?.duration != null) {
                    viewModelPlayer.startPlayer(url = uiState.selectedMedia?.data.toString())
                }
            }

            AddToStoryScreen(
                innerPadding = innerPadding,
                profileImage = uiStateProfile.profileImage,
                uiState = uiState,
                exoPlayer = viewModelPlayer.exoPlayer,
                onAddStoryClick = {
                    viewModelUpload.uploadStory(
                        userStory = UserStory(
                            userId = currentUser?.uid!!,
                            username = uiStateProfile.username,
                            profileImage = uiStateProfile.profileImage,
                            stories = listOf(
                                Story(
                                    userId = currentUser.uid,
                                    timeStamp = timeStamp,
                                    image = uiState.selectedMedia?.data.toString(),
                                    isVerified = true, // TODO: Change
                                    mimeType = uiState.selectedMedia?.mimeType!!
                                )
                            )
                        ),
                        currentUserId = currentUser.uid,
                        onSuccess = {
                            navHostController.navigate(Routes.HomeScreen.route) {
                                popUpTo(navHostController.graph.startDestinationId) {
                                    inclusive = true
                                }

                                launchSingleTop = true
                            }
                        }
                    )
                },
                onBackClick = { navHostController.popBackStack() }
            )
        }
    }
}