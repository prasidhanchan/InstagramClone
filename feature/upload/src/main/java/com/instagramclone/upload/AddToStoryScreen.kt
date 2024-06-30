package com.instagramclone.upload

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.instagramclone.upload.components.story.AddToStoryAppBar
import com.instagramclone.upload.components.story.SelectedStoryCard
import com.instagramclone.upload.components.story.YourStoryCard
import com.instagramclone.util.constants.Utils.IgBackground
import com.instagramclone.util.models.Media
import com.instagramclone.util.test.TestPlayer

@UnstableApi
@Composable
fun AddToStoryScreen(
    innerPadding: PaddingValues,
    profileImage: String,
    uiState: UiState,
    modifier: Modifier = Modifier,
    exoPlayer: ExoPlayer,
    onAddStoryClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = IgBackground
    ) {
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AddToStoryAppBar(onBackClick = onBackClick)

            SelectedStoryCard(
                selectedMedia = uiState.selectedMedia!!,
                exoPlayer = exoPlayer,
                isUploading = uiState.isUploading
            )

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                YourStoryCard(
                    profileImage = profileImage,
                    onYourStoryClick = onAddStoryClick
                )
            }
        }
    }
}

@UnstableApi
@Preview
@Composable
private fun AddToStoryScreenPreview() {
    AddToStoryScreen(
        innerPadding = PaddingValues(),
        profileImage = "",
        uiState = UiState(
            selectedMedia = Media(
                id = "1",
                name = "",
                data = null,
                duration = "1000",
                timeStamp = null,
                mimeType = null
            )
        ),
        exoPlayer = TestPlayer(),
        onAddStoryClick = { },
        onBackClick = { }
    )
}