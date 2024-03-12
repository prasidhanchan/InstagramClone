package com.instagramclone.profile

import androidx.activity.compose.BackHandler
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
import com.instagramclone.ui.components.IGRegularAppBar
import com.instagramclone.ui.components.Posts
import com.instagramclone.ui.R
import com.instagramclone.ui.components.IGLoader
import com.instagramclone.util.constants.Utils
import com.instagramclone.util.models.Post

@Composable
fun PostsScreen(
    innerPadding: PaddingValues,
    uiState: UiState,
    currentUserId: String,
    isMyProfile: Boolean,
    scrollState: LazyListState,
    onFollowClick: () -> Unit,
    onLikeClick: () -> Unit,
    onUnlikeClick: () -> Unit,
    onSendClick: () -> Unit,
    onSaveClick: () -> Unit,
    onUnfollowClick: () -> Unit,
    onDeletePostClick: (Post) -> Unit = {  },
    onUsernameClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
            .background(color = Utils.IgBlack),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IGRegularAppBar(
            text = stringResource(id = R.string.posts),
            onBackClick = onBackClick,
            trailingIcon = {
                if (!isMyProfile) {
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
                enableHeader = false,
                posts = uiState.myPosts,
                currentUserId = currentUserId,
                onLikeClick = onLikeClick,
                onUnLikeClick = onUnlikeClick,
                onSendClick = onSendClick,
                onSaveClick = onSaveClick,
                onUsernameClick = onUsernameClick,
                onUnfollowClick = onUnfollowClick,
                onDeletePostClick = onDeletePostClick,
                scrollState = scrollState
            )
            // For Other user's profile
        } else if (!isMyProfile && uiState.selectedUserPosts.isNotEmpty()) {
            Posts(
                enableHeader = false,
                posts = uiState.selectedUserPosts,
                currentUserId = currentUserId,
                onLikeClick = onLikeClick,
                onUnLikeClick = onUnlikeClick,
                onSendClick = onSendClick,
                onSaveClick = onSaveClick,
                onUsernameClick = onUsernameClick,
                onUnfollowClick = onUnfollowClick,
                onDeletePostClick = onDeletePostClick,
                scrollState = scrollState
            )
        } else {
            IGLoader()
        }
        BackHandler(
            enabled = uiState.showPostScreen,
            onBack = onBackClick
        )
    }
}

@Preview(
    apiLevel = 33,
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
                    images = listOf("a")
                ),
                Post(
                    profileImage = "b",
                    images = listOf("b")
                ),
            )
        ),
        currentUserId = "",
        isMyProfile = true,
        scrollState = LazyListState(),
        onFollowClick = {  },
        onLikeClick = {  },
        onUnlikeClick = {  },
        onSendClick = {  },
        onSaveClick = {  },
        onUnfollowClick = {  },
        onDeletePostClick = {  },
        onUsernameClick = {  },
        onBackClick = {  }
    )
}