package com.instagramclone.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.instagramclone.ui.R
import com.instagramclone.ui.components.IGDialog
import com.instagramclone.ui.components.IGHomeAppBar
import com.instagramclone.ui.components.IGLoader
import com.instagramclone.ui.components.Posts
import com.instagramclone.ui.components.Stories
import com.instagramclone.util.constants.Utils
import com.instagramclone.util.models.Post
import com.instagramclone.util.models.Story

@Composable
fun HomeScreen(
    innerPadding: PaddingValues,
    uiState: UiState,
    profileImage: String,
    username: String,
    selectedPost: Post,
    currentUserId: String,
    onLikeClick: (Post) -> Unit,
    onUnLikeClick: (Post) -> Unit,
    onSendClick: () -> Unit,
    onSaveClick: () -> Unit,
    onUnfollowClick: () -> Unit,
    onDeletePostClick: (Post) -> Unit,
    setSelectedPost: (Post) -> Unit,
    onUsernameClick: (String) -> Unit
) {
    val stories = listOf(
        Story(
            username = "android",
            profileImage = "https://firebasestorage.googleapis.com/v0/b/instagram-clone-3eeaf.appspot.com/o/ProfileImage%2F20240210_170809.jpg?alt=media&token=4e68b3db-5759-462f-9814-b28212fd5604"
        ),
        Story(
            username = "virat.kohli",
            profileImage = "https://firebasestorage.googleapis.com/v0/b/instagram-clone-3eeaf.appspot.com/o/ProfileImage%2FEPSUNyR3_400x400.jpg?alt=media&token=39c92864-418f-4724-9998-4ad44697c3b3"
        ),
        Story(
            username = "fordmustang",
            profileImage = "https://imgs.search.brave.com/FlvNKl9wbnrJaG6u7micYzZp6BUUgF_VwwOWBSNqI1k/rs:fit:860:0:0/g:ce/aHR0cHM6Ly93YWxs/cGFwZXJjYXZlLmNv/bS93cC95Rk52M1ll/LmpwZw"
        ),
        Story(
            username = "youtubeindia",
            profileImage = "https://imgs.search.brave.com/7J12IIN_wYv0GWQHLDlpj5PZDJb2JKGPN-OuJW1sqyc/rs:fit:860:0:0/g:ce/aHR0cHM6Ly9saDMu/Z29vZ2xldXNlcmNv/bnRlbnQuY29tL3o2/U2w0ajl6UTg4b1VL/TnkwRzNQQU1pVnd5/OER6UUxoX3lneXZC/WHYwelZOVVpfd1FQ/Tl9uN0VBUjJCeTNk/aG9VcFg3a1RwYUhq/UlBuaTFNSHdLcGFC/SmJwTnFkRXNIWnNI/NHE"
        ),
        Story(
            username = "googlefordevs",
            profileImage = "https://imgs.search.brave.com/eYgUjaUBhnrYuDAX5uIvRKn0Qv6eTtxljMLuI77i53Q/rs:fit:860:0:0/g:ce/aHR0cHM6Ly90NC5m/dGNkbi5uZXQvanBn/LzAzLzA4LzU0LzM3/LzM2MF9GXzMwODU0/Mzc4N19EbVBvMUlF/THRLWTloRzhFOEds/VzhLSEVzUkM3SmlE/Ti5qcGc",
            isViewed = true
        )
    )

    var showDeleteDialog by remember { mutableStateOf(false) }
    val scrollState = rememberLazyListState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Utils.IgBlack
    ) {
        if (!uiState.isLoading && uiState.posts.isNotEmpty()) {
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
                    onLikeClick = onLikeClick,
                    onUnLikeClick = onUnLikeClick,
                    onSendClick = onSendClick,
                    onSaveClick = onSaveClick,
                    onUnfollowClick = onUnfollowClick,
                    onDeletePostClick = {
                        showDeleteDialog = true
                        setSelectedPost(it)
                    },
                    onUsernameClick = onUsernameClick,
                    scrollState = scrollState
                ) {
                    IGHomeAppBar()

                    Stories(
                        profileImage = profileImage,
                        onAddStoryClick = { /* TODO */ },
                        onStoryClick = { /* TODO */ },
                        stories = stories
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(top = 8.dp),
                        thickness = 0.5.dp,
                        color = Color.White.copy(alpha = 0.2f)
                    )
                }
            }
            IGDialog(
                title = stringResource(R.string.delete_this_post),
                subTitle = stringResource(R.string.delete_post_permanently),
                showDialog = showDeleteDialog,
                showBlueOrRedButton = true,
                blueOrRedButton = Utils.IgError,
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
}

@Preview(
    apiLevel = 33,
    showBackground = true,
    backgroundColor = 0XFF000000
)
@Composable
fun HomeScreenPreview() {
    val stories = listOf(
        Story(
            username = "android"
        ),
        Story(
            username = "youtubeindia"
        ),
        Story(
            username = "virat.kohli"
        ),
        Story(
            username = "mustang"
        ),
        Story(
            username = "googlefordevs"
        )
    )
    HomeScreen(
        innerPadding = PaddingValues(),
        uiState = UiState(
            stories = stories,
            posts = listOf(
                Post(
                    images = listOf(""),
                    username = "kawaki",
                    isVerified = true
                )
            )
        ),
        profileImage = "",
        username = "pra_sidh_22",
        selectedPost = Post(),
        currentUserId = "12345",
        onLikeClick = { },
        onUnLikeClick = { },
        onSendClick = { },
        onSaveClick = { },
        onUnfollowClick = { },
        onDeletePostClick = { },
        setSelectedPost = { },
        onUsernameClick = { }
    )
}