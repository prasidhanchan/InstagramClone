package com.instagramclone.profile

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.instagramclone.firebase.models.IGUser
import com.instagramclone.ui.R
import com.instagramclone.ui.components.IGLoader
import com.instagramclone.ui.components.ProfileCard
import com.instagramclone.util.constants.Utils
import com.instagramclone.util.models.Post

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(
    innerPadding: PaddingValues,
    uiState: UiState,
    currentUserId: String,
    onFollowClick: () -> Unit,
    onUnfollowClick: () -> Unit,
    navigateToPostsWithPostIndex: (String) -> Unit,
    onEditProfileClick: () -> Unit,
    setIsFollowing: (Boolean) -> Unit,
    onBackClick: () -> Unit
) {
    var showSheet by remember { mutableStateOf(false) }
    var currentFollower by remember { mutableIntStateOf(0) }
    var currentFollowing by remember { mutableIntStateOf(0) }

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
                    IGUserProfileAppBar(
                        username = uiState.selectedUserProfile.username,
                        onMoreClick = { showSheet = true },
                        onBackClick = onBackClick
                    )
                }

                item(
                    key = "profileCard",
                    span = { GridItemSpan(currentLineSpan = 3) }
                ) {
                    ProfileCard(
                        myProfile = currentUserId == uiState.selectedUserProfile.userId,
                        profileImage = uiState.selectedUserProfile.profileImage,
                        name = uiState.selectedUserProfile.name,
                        bio = uiState.selectedUserProfile.bio,
                        links = uiState.selectedUserProfile.links,
                        posts = uiState.selectedUserPosts.size,
                        followers = uiState.selectedUserProfile.followersList.size.plus(currentFollower),
                        following = uiState.selectedUserProfile.followingList.size,
                        isFollowing = uiState.isFollowing,
                        onFollowClick = {
                            setIsFollowing(true) // Manually setting to avoid refreshing screen
                            currentFollower += 1 // Manually Adding follower
                            currentFollowing += 1 // Manually Adding following
                            onFollowClick()
                        },
                        onUnFollowClick = {
                            setIsFollowing(false) // Manually setting to avoid refreshing screen
                            currentFollower -= 1 // Manually Removing follower
                            currentFollowing -= 1 // Manually Remove following
                            onUnfollowClick()
                        },
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
                    items = uiState.selectedUserPosts
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
                                            "${myPost.userId}-${uiState.selectedUserPosts.indexOf(myPost)}"
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

                }
            }
        }
    } else {
        IGLoader()
    }
    BackHandler(onBack = onBackClick)
}

@Composable
fun IGUserProfileAppBar(
    username: String,
    onMoreClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .height(50.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Icon(
            modifier = Modifier.clickable(
                onClick = onBackClick
            ),
            painter = painterResource(id = R.drawable.back),
            tint = Color.White,
            contentDescription = stringResource(id = R.string.back)
        )
        Text(
            modifier = Modifier
                .padding(horizontal = 30.dp)
                .weight(6f),
            text = username,
            style = TextStyle(
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            ),
            textAlign = TextAlign.Start
        )

        Icon(
            modifier = Modifier.clickable(
                indication = null,
                interactionSource = interactionSource,
                onClick = onMoreClick
            ),
            painter = painterResource(id = R.drawable.more2),
            tint = Color.White,
            contentDescription = stringResource(id = R.string.more)
        )
    }
}

@Preview(
    apiLevel = 33,
    showBackground = true,
    backgroundColor = 0XFF000000
)
@Composable
private fun UserProfileScreenPreview() {
    UserProfileScreen(
        innerPadding = PaddingValues(),
        uiState = UiState(
            selectedUserProfile = IGUser(
                username = "pra_sidh_22",
                name = "Prasidh Gopal Anchan",
                followersList = listOf("", ""),
                followingList = listOf("")
            ),
            selectedUserPosts = listOf(
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
        onFollowClick = { },
        onUnfollowClick = { },
        navigateToPostsWithPostIndex = { },
        onEditProfileClick = { },
        setIsFollowing = { }
    ) { }
}