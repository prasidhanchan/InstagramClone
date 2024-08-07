package com.instagramclone.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.instagramclone.ui.R
import com.instagramclone.ui.components.IGLoader
import com.instagramclone.ui.components.IGRegularAppBar
import com.instagramclone.ui.components.Posts
import com.instagramclone.util.constants.Utils
import com.instagramclone.util.models.Post
import com.instagramclone.util.test.TestPlayer

@UnstableApi
@Composable
fun PostsScreen(
    innerPadding: PaddingValues,
    uiState: UiState,
    currentUserId: String,
    exoPlayer: ExoPlayer,
    currentPosition: Long,
    duration: Long,
    onWatchAgainClick: (String) -> Unit,
    isMyProfile: Boolean,
    scrollState: LazyListState,
    onFollowClick: () -> Unit,
    onLikeClick: (Post) -> Unit,
    onUnlikeClick: (Post) -> Unit,
    onSendClick: () -> Unit,
    onSaveClick: () -> Unit,
    onUnfollowClick: () -> Unit,
    onDeletePostClick: (Post) -> Unit = { },
    onUsernameClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
            .background(color = Utils.IgBackground),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IGRegularAppBar(
            text = stringResource(id = R.string.posts),
            onBackClick = onBackClick,
            trailingIcon = {
                if (!isMyProfile && !uiState.isFollowing) {
                    Text(
                        modifier = Modifier.clickable(onClick = onFollowClick),
                        text = stringResource(id = R.string.follow),
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Utils.IgBlue
                        )
                    )
                }
            }
        )
        // For my profile
        if (isMyProfile && uiState.myPosts.isNotEmpty()) {
            Posts(
                posts = uiState.myPosts,
                currentUserId = currentUserId,
                exoPlayer = exoPlayer,
                currentPosition = currentPosition,
                duration = duration,
                onWatchAgainClick = onWatchAgainClick,
                enableHeader = false,
                onLikeClick = onLikeClick,
                onUnLikeClick = onUnlikeClick,
                onSendClick = onSendClick,
                onSaveClick = onSaveClick,
                onUnfollowClick = onUnfollowClick,
                onDeletePostClick = onDeletePostClick,
                onUsernameClick = onUsernameClick,
                state = scrollState
            )
            // For Other user's profile
        } else if (!isMyProfile && uiState.selectedUserPosts.isNotEmpty()) {
            Posts(
                posts = uiState.selectedUserPosts,
                currentUserId = currentUserId,
                exoPlayer = exoPlayer,
                currentPosition = currentPosition,
                duration = duration,
                onWatchAgainClick = onWatchAgainClick,
                enableHeader = false,
                onLikeClick = onLikeClick,
                onUnLikeClick = onUnlikeClick,
                onSendClick = onSendClick,
                onSaveClick = onSaveClick,
                onUnfollowClick = onUnfollowClick,
                onDeletePostClick = onDeletePostClick,
                onUsernameClick = onUsernameClick,
                state = scrollState
            )
        } else {
            IGLoader()
        }
    }
}

@UnstableApi
@Preview(
    showBackground = true,
    backgroundColor = 0XFF000000
)
@Composable
fun PostScreenPreview() {
    PostsScreen(
        innerPadding = PaddingValues(),
        uiState = UiState(
            username = "pra_sidh_22",
            myPosts = listOf(
                Post(
                    profileImage = "a",
                    mediaList = listOf("a")
                ),
                Post(
                    profileImage = "b",
                    mediaList = listOf("b")
                ),
            )
        ),
        currentUserId = "",
        exoPlayer = TestPlayer(),
        currentPosition = 1000L,
        duration = 800L,
        onWatchAgainClick = { },
        isMyProfile = true,
        scrollState = LazyListState(),
        onFollowClick = { },
        onLikeClick = { },
        onUnlikeClick = { },
        onSendClick = { },
        onSaveClick = { },
        onUnfollowClick = { },
        onDeletePostClick = { },
        onUsernameClick = { }
    ) { }
}