package com.instagramclone.upload

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.instagramclone.upload.components.story.MediaSelectionCard
import com.instagramclone.ui.R
import com.instagramclone.ui.components.IGRegularAppBar
import com.instagramclone.upload.components.story.MediaCards
import com.instagramclone.util.constants.Utils.IgBackground
import com.instagramclone.util.models.Media

@Composable
fun UploadStoryScreen(
    innerPadding: PaddingValues,
    mediaList: List<Media>,
    onPhotosClick: () -> Unit,
    onVideosClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = IgBackground
    ) {
        Column(
            modifier = Modifier.padding(innerPadding),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IGRegularAppBar(
                text = stringResource(id = R.string.add_story),
                leadingIcon = R.drawable.cross,
                textAlign = TextAlign.Center,
                onBackClick = onBackClick
            )
            MediaCards(
                mediaList = mediaList,
                onPhotosClick = onPhotosClick,
                onVideosClick = onVideosClick
            )
        }
    }
}

@Preview
@Composable
private fun UploadStoryScreenPreview() {
    UploadStoryScreen(
        innerPadding = PaddingValues(),
        mediaList = listOf(
            Media(
                id = "1",
                name = "",
                data = null,
                duration = "1200",
                timeStamp = null,
                mimeType = null
            ),
            Media(
                id = "2",
                name = "",
                data = null,
                duration = null,
                timeStamp = null,
                mimeType = null
            ),
            Media(
                id = "3",
                name = "",
                data = null,
                duration = null,
                timeStamp = null,
                mimeType = null
            )
        ),
        onPhotosClick = { },
        onVideosClick = { },
        onBackClick = { }
    )
}