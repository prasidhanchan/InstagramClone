package com.instagramclone.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.instagramclone.ui.R
import com.instagramclone.ui.components.IGDialog
import com.instagramclone.ui.components.IGLoader
import com.instagramclone.ui.components.IGProfileAppBar
import com.instagramclone.ui.components.MoreCard
import com.instagramclone.ui.components.ProfileCard
import com.instagramclone.util.constants.Utils
import com.instagramclone.util.models.Post

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyProfileScreen(
    innerPadding: PaddingValues,
    uiState: UiState,
    selectedPost: Post,
    onFollowClick: () -> Unit,
    onUnfollowClick: () -> Unit,
    onDeletePostClick: (Post) -> Unit,
    onEditProfileClick: () -> Unit,
    navigateToPostsWithPostIndex: (String) -> Unit,
    setSelectedPost: (Post) -> Unit,
    onSettingsAndPrivacyClicked: () -> Unit
) {
    var showSheet by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (!uiState.isLoading) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Utils.IgBlack
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.Top,
                horizontalArrangement = Arrangement.Start
            ) {
                item(
                    key = "profileAppBar",
                    span = { GridItemSpan(currentLineSpan = 3) }
                ) {
                    IGProfileAppBar(
                        username = uiState.username,
                        onMoreClick = { showSheet = true }
                    )
                }

                item(
                    key = "profileCard",
                    span = { GridItemSpan(currentLineSpan = 3) }
                ) {
                    ProfileCard(
                        profileImage = uiState.profileImage,
                        name = uiState.name,
                        bio = uiState.bio,
                        links = uiState.links,
                        posts = uiState.myPosts.size,
                        followers = uiState.followers.size,
                        following = uiState.following.size,
                        onFollowClick = onFollowClick,
                        onUnFollowClick = onUnfollowClick,
                        onEditProfileClick = onEditProfileClick
                    )
                }

                item(
                    span = { GridItemSpan(currentLineSpan = 3) }
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            modifier = Modifier.padding(vertical = 10.dp),
                            painter = painterResource(id = R.drawable.grid),
                            tint = Color.White,
                            contentDescription = stringResource(com.instagramclone.profile.R.string.posts)
                        )
                        HorizontalDivider(
                            modifier = Modifier.padding(bottom = 2.dp),
                            thickness = 1.dp,
                            color = Color.White
                        )
                    }
                }

                items(
                    key = { myPost -> myPost.images },
                    items = uiState.myPosts
                ) { myPost ->
                    if (myPost.images.isNotEmpty()) {
                        Box(
                            modifier = Modifier
                                .padding(1.dp)
                                .size(120.dp)
                                .background(color = Utils.IgOffBlack)
                                .clickable(
                                    onClick = {
                                        navigateToPostsWithPostIndex(
                                            "${myPost.userId}-${uiState.myPosts.indexOf(myPost)}"
                                        )
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            AsyncImage(
                                modifier = Modifier.fillMaxSize(),
                                model = myPost.images.first() /* TODO integrate pager */,
                                contentScale = ContentScale.Crop,
                                contentDescription = stringResource(
                                    com.instagramclone.profile.R.string.post_info,
                                    uiState.username
                                )
                            )
                        }
                    }
                }
            }
        }

        if (showSheet) {
            ModalBottomSheet(
                dragHandle = {
                    BottomSheetDefaults.DragHandle(
                        width = 40.dp,
                        color = Utils.IgOffWhite
                    )
                },
                containerColor = Utils.IgOffBlack,
                onDismissRequest = { showSheet = false }
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 5.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    MoreCard(
                        modifier = Modifier.scale(0.8f),
                        icon = painterResource(id = R.drawable.settings),
                        title = stringResource(R.string.settings_and_privacy),
                        onClick = {
                            showSheet = false
                            onSettingsAndPrivacyClicked()
                        }
                    )
                }
            }
        }

        IGDialog(
            title = stringResource(id = R.string.delete_this_post),
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

@Preview(
    apiLevel = 33,
    showBackground = true,
    backgroundColor = 0XFF000000
)
@Composable
private fun MyProfileScreenPreview() {
    MyProfileScreen(
        innerPadding = PaddingValues(),
        uiState = UiState(
            username = "pra_sidh_22",
            name = "Prasidh Gopal Anchan",
            myPosts = listOf(
                Post(
                    profileImage = "a",
                    images = listOf("a")
                ),
                Post(
                    profileImage = "b",
                    images = listOf("b")
                ),
            ),
            followers = listOf("", ""),
            following = listOf("")
        ),
        selectedPost = Post(),
        onFollowClick = { },
        onUnfollowClick = { },
        onDeletePostClick = { },
        onEditProfileClick = { },
        navigateToPostsWithPostIndex = { },
        setSelectedPost = { }
    ) { }
}