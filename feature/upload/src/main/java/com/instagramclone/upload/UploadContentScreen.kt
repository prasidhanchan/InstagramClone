package com.instagramclone.upload

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.instagramclone.upload.components.UploadSelectionCard
import com.instagramclone.ui.R
import com.instagramclone.util.test.TestPlayer
import com.instagramclone.util.models.Media

@UnstableApi
@Composable
fun UploadContentScreen(
    innerPadding: PaddingValues,
    uiState: UiState,
    exoPlayer: ExoPlayer,
    onMediaSelected: (Media) -> Unit,
    onPhotosClick: () -> Unit,
    onVideosClick: () -> Unit,
    onNextClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    var selectedText by remember { mutableStateOf(context.getString(R.string.post_caps)) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        UploadStoryScreen(
            innerPadding = innerPadding,
            mediaList = uiState.mediaList,
            onPhotosClick = onPhotosClick,
            onVideosClick = onVideosClick,
            onBackClick = onBackClick
        )

        UploadPostScreen(
            visible = selectedText == context.getString(R.string.post_caps),
            innerPadding = innerPadding,
            uiState = uiState,
            exoPlayer = exoPlayer,
            onMediaSelected = onMediaSelected,
            onNextClick = onNextClick,
            onBackClick = onBackClick
        )

        UploadSelectionCard(
            selectedText = selectedText,
            onClick = { selected ->
                exoPlayer.stop()
                selectedText = selected
            }
        )
    }

    BackHandler {
        onBackClick()
        exoPlayer.release()
    }
}

@UnstableApi
@Preview
@Composable
private fun ShareContentScreenPreview() {
    UploadContentScreen(
        innerPadding = PaddingValues(),
        uiState = UiState(),
        exoPlayer = TestPlayer(),
        onMediaSelected = { },
        onPhotosClick = { },
        onVideosClick = { },
        onNextClick = { },
        onBackClick = { }
    )
}