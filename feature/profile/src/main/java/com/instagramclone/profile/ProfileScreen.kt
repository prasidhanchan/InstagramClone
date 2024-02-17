package com.instagramclone.profile

import androidx.compose.foundation.background
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
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.instagramclone.ui.components.IGProfileAppBar
import com.instagramclone.ui.components.ProfileCard
import com.instagramclone.util.constants.Utils
import com.instagramclone.ui.R
import com.instagramclone.ui.components.IGLoader

@Composable
fun ProfileScreen(
    innerPadding: PaddingValues,
    uiState: UiState,
    onEditProfileClick: () -> Unit,
    onMoreClick: () -> Unit
) {
    if (!uiState.isLoading) {
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
                    onMoreClick = onMoreClick
                )
            }

            item (
                key = "profileCard",
                span = { GridItemSpan(currentLineSpan = 3) }
            ){
                ProfileCard(
                    profileImage = uiState.profileImage,
                    name = uiState.name,
                    bio = uiState.bio,
                    links = uiState.links,
                    posts = uiState.myPosts.size,
                    followers = uiState.followers.size,
                    following = uiState.following.size,
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
                    Divider(
                        modifier = Modifier.padding(bottom = 2.dp),
                        thickness = 1.dp,
                        color = Color.White
                    )
                }
            }

            items(
                key = { myPost -> myPost },
                items = uiState.myPosts
            ) { myPost ->
                Box(
                    modifier = Modifier
                        .padding(1.dp)
                        .size(120.dp)
                        .background(color = Utils.IgOffBlack),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        modifier = Modifier.fillMaxSize(),
                        model = myPost,
                        contentScale = ContentScale.Crop,
                        contentDescription = stringResource(
                            com.instagramclone.profile.R.string.post_info,
                            uiState.username
                        )
                    )
                }
            }
        }
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
fun ProfileScreenPreview() {
    ProfileScreen(
        innerPadding = PaddingValues(),
        uiState = UiState(
            username = "pra_sidh_22",
            name = "Prasidh Gopal Anchan",
            myPosts = listOf(
                "1",
                "2",
                "3",
                "4"
            ),
            followers = listOf("", ""),
            following = listOf("")
        ),
        onEditProfileClick = {  },
        onMoreClick = {  }
    )
}