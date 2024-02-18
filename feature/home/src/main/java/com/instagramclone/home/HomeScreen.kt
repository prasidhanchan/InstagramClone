package com.instagramclone.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.instagramclone.ui.components.IGHomeAppBar
import com.instagramclone.ui.components.Posts
import com.instagramclone.ui.components.Stories
import com.instagramclone.util.models.Post
import com.instagramclone.util.models.Story

@Composable
fun HomeScreen(
    innerPadding: PaddingValues,
    uiState: UiState,
    currentUserId: String,
    onLikeClicked: () -> Unit,
    onUnLikeClicked: () -> Unit,
    onSendClicked: () -> Unit,
    onSaveClicked: () -> Unit,
    onUsernameClicked: () -> Unit
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
    val posts = listOf(
        Post(
            username = "pra_sidh_22",
            images = listOf(
                "https://beebom.com/wp-content/uploads/2023/08/Jujutsu-Kaisen-Shibuya-Incident-arc.jpg?quality=75&strip=all",
                "https://www.themobileindian.com/wp-content/uploads/2021/06/pubg.jpg"
            ),
            profileImage = "https://firebasestorage.googleapis.com/v0/b/instagram-clone-3eeaf.appspot.com/o/ProfileImage%2FvJdDRERlD9VVleMOJDIoYTXSYu53.jpg?alt=media&token=e1831424-81db-44ac-baaa-b8e4dced084b",
            likes = listOf("1234", "12345", "123456", "85488"),
            isVerified = true,
            caption = "Who you picking?"
        ),
        Post(
            username = "fordmustang",
            images = listOf("https://imgs.search.brave.com/M0SnwILQ61kDRs5hVMb68edBdpVgueVd9YsKRBjj168/rs:fit:860:0:0/g:ce/aHR0cHM6Ly9tZWRp/YS5nZXR0eWltYWdl/cy5jb20vaWQvNDU5/MjYxOTA5L3Bob3Rv/L2ZvcmQtbXVzdGFu/Zy5qcGc_cz02MTJ4/NjEyJnc9MCZrPTIw/JmM9ZC1DLW0waFJ6/bVAyR000NnZEd1Yt/S1VTUHQzUDk0Q2Q2/bG1OYmxZRk9jMD0"),
            profileImage = "https://imgs.search.brave.com/FlvNKl9wbnrJaG6u7micYzZp6BUUgF_VwwOWBSNqI1k/rs:fit:860:0:0/g:ce/aHR0cHM6Ly93YWxs/cGFwZXJjYXZlLmNv/bS93cC95Rk52M1ll/LmpwZw",
            likes = listOf("1234", "12345"),
            isVerified = true,
            caption = "fordmustang After four and a half years of hard work and dedication, Mark Passarelli's fully factory-restored'68 Ford Mustang® GT Fastback is ready to speed off into the\n" +
                    "sunset."
        )
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Posts(
            innerPadding = innerPadding,
            topContent = {
                IGHomeAppBar()

                Stories(
                    profileImage = uiState.profileImage,
                    onAddStoryClick = { /* TODO */ },
                    onStoryClick = { /* TODO */ },
                    stories = stories
                )
                Divider(
                    modifier = Modifier.padding(top = 8.dp),
                    thickness = 0.5.dp,
                    color = Color.White.copy(alpha = 0.2f)
                )
            },
            posts = posts,
            currentUserId = currentUserId,
            onLikeClicked = onLikeClicked,
            onUnLikeClicked = onUnLikeClicked,
            onSendClicked = onSendClicked,
            onSaveClicked = onSaveClicked,
            onUsernameClicked = onUsernameClicked
        )
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
        uiState = UiState(stories = stories),
        currentUserId = "12345",
        onLikeClicked = { },
        onUnLikeClicked = { },
        onSendClicked = { },
        onSaveClicked = { },
        onUsernameClicked = { },
    )
}