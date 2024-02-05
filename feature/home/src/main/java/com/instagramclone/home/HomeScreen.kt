package com.instagramclone.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.instagramclone.ui.components.IGHomeAppBar
import com.instagramclone.ui.components.PostCard
import com.instagramclone.ui.components.Stories
import com.instagramclone.util.constants.Utils
import com.instagramclone.util.models.Post
import com.instagramclone.util.models.Story

@Composable
fun HomeScreen(
    uiState: UiState
) {
    val stories = listOf(
        Story(
            username = "pra_sidh_22"
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
            username = "googlefordevs",
            isViewed = true
        )
    )
    val post = Post(
        username = "pra_sidh_22",
        images = listOf("https://www.themobileindian.com/wp-content/uploads/2021/06/pubg.jpg"),
        profileImage = "https://firebasestorage.googleapis.com/v0/b/instagram-clone-3eeaf.appspot.com/o/ProfileImage%2FvJdDRERlD9VVleMOJDIoYTXSYu53.jpg?alt=media&token=e1831424-81db-44ac-baaa-b8e4dced084b",
        likes = 52456,
        isVerified = true,
        caption = "Who you picking?"
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Utils.IgBlack
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IGHomeAppBar()

            Stories(
                profileImage = uiState.profileImage,
                onAddStoryClick = { /*TODO*/ },
                onStoryClick = { /*TODO*/ },
                stories = stories
            )

            Divider(
                modifier = Modifier.padding(top = 8.dp),
                thickness = 0.5.dp,
                color = Color.White.copy(alpha = 0.2f)
            )

            PostCard(
                post = post,
                onLikeClicked = {  },
                onSaveClicked = {  },
                onSendClicked = {  },
                onUsernameClicked = {  }
            )
            PostCard(
                post = post,
                onLikeClicked = {  },
                onSaveClicked = {  },
                onSendClicked = {  },
                onUsernameClicked = {  }
            )
        }
    }
}

@Preview(apiLevel = 33)
@Composable
fun HomeScreenPreview() {
    val stories = listOf(
        Story(
            username = "pra_sidh_22"
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
        uiState = UiState(stories = stories)
    )
}