package com.instagramclone.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.instagramclone.home.components.IGHomeAppBar
import com.instagramclone.story.Stories
import com.instagramclone.story.StoryScreen
import com.instagramclone.ui.R
import com.instagramclone.ui.components.IGDialog
import com.instagramclone.ui.components.IGLoader
import com.instagramclone.ui.components.Posts
import com.instagramclone.ui.components.Stories
import com.instagramclone.util.constants.Utils.IgBackground
import com.instagramclone.util.constants.Utils.IgError
import com.instagramclone.util.models.Post
import com.instagramclone.util.models.Story
import com.instagramclone.util.models.UserStory
import com.instagramclone.util.test.TestPlayer
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@UnstableApi
@Composable
fun HomeScreen(
    innerPadding: PaddingValues,
    uiState: UiState,
    following: List<String>,
    profileImage: String,
    selectedPost: Post,
    currentUserId: String,
    exoPlayer: ExoPlayer,
    currentPosition: Long,
    duration: Long,
    onWatchAgainClick: (String) -> Unit,
    onLikeClick: (Post) -> Unit,
    onUnLikeClick: (Post) -> Unit,
    onSendClick: () -> Unit,
    onSaveClick: () -> Unit,
    onUnfollowClick: () -> Unit,
    onDeletePostClick: (Post) -> Unit,
    onDeleteStoryClick: (Story) -> Unit,
    setSelectedPost: (Post) -> Unit,
    onUsernameClick: (String) -> Unit,
    onAddStoryClick: () -> Unit,
    updateViews: (Story) -> Unit,
    setShowStoryScreen: (Boolean) -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    val state = rememberLazyListState()

    var userStoryIndex by remember { mutableIntStateOf(0) }
    var selectedStory by remember { mutableStateOf(Stories.USER_STORY) }

    val scope = rememberCoroutineScope()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = IgBackground
    ) {
        if (!uiState.isLoading) {
            if (following.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Posts(
                        innerPadding = innerPadding,
                        posts = uiState.posts,
                        currentUserId = currentUserId,
                        exoPlayer = exoPlayer,
                        currentPosition = currentPosition,
                        duration = duration,
                        onWatchAgainClick = onWatchAgainClick,
                        onLikeClick = onLikeClick,
                        onUnLikeClick = onUnLikeClick,
                        onSendClick = onSendClick,
                        onSaveClick = onSaveClick,
                        onUnfollowClick = onUnfollowClick,
                        onDeletePostClick = { post ->
                            showDeleteDialog = true
                            setSelectedPost(post)
                        },
                        onUsernameClick = onUsernameClick,
                        state = state
                    ) {
                        IGHomeAppBar()

                        Stories(
                            profileImage = profileImage,
                            currentUserId = currentUserId,
                            onAddStoryClick = onAddStoryClick,
                            onViewMyStoryClick = {
                                userStoryIndex = 0
                                selectedStory = Stories.MY_STORY
                                setShowStoryScreen(true)
                            },
                            onStoryClick = { storyIndex ->
                                userStoryIndex = storyIndex
                                selectedStory = Stories.USER_STORY
                                setShowStoryScreen(true)
                            },
                            userStories = uiState.userStories,
                            myStories = uiState.myStories
                        )

                        HorizontalDivider(
                            modifier = Modifier.padding(top = 8.dp),
                            thickness = 0.5.dp,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)
                        )
                    }
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.follow_someone),
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    )
                }
            }

            IGDialog(
                title = stringResource(R.string.delete_this_post),
                subTitle = stringResource(R.string.delete_post_permanently),
                showDialog = showDeleteDialog,
                showBlueOrRedButton = true,
                blueOrRedButton = IgError,
                button1Text = stringResource(id = R.string.cancel),
                button2Text = stringResource(id = R.string.delete),
                onBlueOrRedClick = {
                    showDeleteDialog = false
                    onDeletePostClick(selectedPost)
                },
                onWhiteClick = {
                    showDeleteDialog = false
                    setSelectedPost(Post()) // Clearing selected post on cancel click
                }
            )

        } else {
            IGLoader()
        }
    }

    StoryScreen(
        storyIndex = userStoryIndex,
        visible = uiState.showStoryScreen,
        userStories = {
            if (selectedStory == Stories.MY_STORY) uiState.myStories else uiState.userStories
        },
        currentUserId = currentUserId,
        innerPadding = innerPadding,
        onDeleteStoryClick = { story ->
            scope.launch {
                setShowStoryScreen(false)
                delay(800L)
                onDeleteStoryClick(story)
            }
        },
        updateViews = updateViews,
        onDismiss = { setShowStoryScreen(false) }
    )
}

@UnstableApi
@Preview(
    showBackground = true,
    backgroundColor = 0XFF000000
)
@Composable
fun HomeScreenPreview() {
    val userStories = listOf(
        UserStory(
            username = "android",
            profileImage = "https://firebasestorage.googleapis.com/v0/b/instagram-clone-3eeaf.appspot.com/o/ProfileImage%2F20240210_170809.jpg?alt=media&token=4e68b3db-5759-462f-9814-b28212fd5604",
            stories = listOf(
                Story(
                    userId = "android"
                )
            )
        ),
        UserStory(
            username = "virat.kohli",
            profileImage = "https://firebasestorage.googleapis.com/v0/b/instagram-clone-3eeaf.appspot.com/o/ProfileImage%2FEPSUNyR3_400x400.jpg?alt=media&token=39c92864-418f-4724-9998-4ad44697c3b3",
            stories = listOf(
                Story(
                    userId = "virat.kohli"
                )
            )
        ),
        UserStory(
            username = "youtubeindia",
            profileImage = "https://imgs.search.brave.com/7J12IIN_wYv0GWQHLDlpj5PZDJb2JKGPN-OuJW1sqyc/rs:fit:860:0:0/g:ce/aHR0cHM6Ly9saDMu/Z29vZ2xldXNlcmNv/bnRlbnQuY29tL3o2/U2w0ajl6UTg4b1VL/TnkwRzNQQU1pVnd5/OER6UUxoX3lneXZC/WHYwelZOVVpfd1FQ/Tl9uN0VBUjJCeTNk/aG9VcFg3a1RwYUhq/UlBuaTFNSHdLcGFC/SmJwTnFkRXNIWnNI/NHE",
            stories = listOf(
                Story(
                    userId = "youtubeindia",
                )
            )
        ),
        UserStory(
            username = "googlefordevs",
            profileImage = "https://imgs.search.brave.com/eYgUjaUBhnrYuDAX5uIvRKn0Qv6eTtxljMLuI77i53Q/rs:fit:860:0:0/g:ce/aHR0cHM6Ly90NC5m/dGNkbi5uZXQvanBn/LzAzLzA4LzU0LzM3/LzM2MF9GXzMwODU0/Mzc4N19EbVBvMUlF/THRLWTloRzhFOEds/VzhLSEVzUkM3SmlE/Ti5qcGc",
            stories = listOf(
                Story(
                    userId = "googlefordevs"
                )
            )
        )
    )

    HomeScreen(
        innerPadding = PaddingValues(),
        uiState = UiState(
            userStories = userStories,
            posts = listOf(
                Post(
                    mediaList = listOf(""),
                    username = "kawaki",
                    isVerified = true
                )
            )
        ),
        following = listOf("1"),
        profileImage = "",
        selectedPost = Post(),
        currentUserId = "12345",
        exoPlayer = TestPlayer(),
        currentPosition = 1000L,
        duration = 800L,
        onWatchAgainClick = { },
        onLikeClick = { },
        onUnLikeClick = { },
        onSendClick = { },
        onSaveClick = { },
        onUnfollowClick = { },
        onDeletePostClick = { },
        onDeleteStoryClick = { },
        setSelectedPost = { },
        onUsernameClick = { },
        onAddStoryClick = { },
        updateViews = { },
        setShowStoryScreen = { }
    )
}