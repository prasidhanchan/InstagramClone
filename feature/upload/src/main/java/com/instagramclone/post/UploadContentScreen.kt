package com.instagramclone.post

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
import com.instagramclone.post.components.UploadSelectionCard
import com.instagramclone.ui.R
import com.instagramclone.util.models.Image

@Composable
fun UploadContentScreen(
    innerPadding: PaddingValues,
    uiState: UiState,
    onImageSelected: (Image) -> Unit,
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
            innerPadding = innerPadding
        )

        UploadPostScreen(
            visible = selectedText == context.getString(R.string.post_caps),
            innerPadding = innerPadding,
            uiState = uiState,
            onImageSelected = onImageSelected,
            onNextClick = onNextClick,
            onBackClick = onBackClick
        )

        UploadSelectionCard(
            selectedText = selectedText,
            onClick = {
                selectedText = it
            }
        )
    }
}

@Preview
@Composable
private fun ShareContentScreenPreview() {
    UploadContentScreen(
        innerPadding = PaddingValues(),
        uiState = UiState(),
        onImageSelected = { },
        onNextClick = { },
        onBackClick = { }
    )
}