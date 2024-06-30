package com.instagramclone.upload.components.post

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.instagramclone.ui.R
import com.instagramclone.ui.components.IGPlayer
import com.instagramclone.util.constants.Utils.IgBackground
import com.instagramclone.util.constants.Utils.IgOffColor
import com.instagramclone.util.constants.formatMinSec
import com.instagramclone.util.models.Media

@UnstableApi
@Composable
fun SelectedMediaCard(
    media: Media?,
    exoPlayer: ExoPlayer,
    description: String?
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp),
        contentAlignment = Alignment.Center
    ) {
        if (media?.duration == null) {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = media?.data,
                contentScale = ContentScale.Crop,
                contentDescription = description
            )
        } else {
            IGPlayer(
                exoPlayer = exoPlayer,
                onLongPress = { exoPlayer.pause() },
                onPress = { exoPlayer.play() }
            )
        }
    }
}

@Composable
fun DividerCard() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        color = IgBackground
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = stringResource(R.string.recents),
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    }
}

@Composable
fun MediaCardItem(
    media: Media,
    selectedImage: Uri?,
    onImageSelected: (Media) -> Unit
) {
    Box(
        modifier = Modifier
            .width(80.dp)
            .height(100.dp)
            .padding(all = 1.dp)
            .background(color = IgBackground)
            .clickable(
                onClick = {
                    onImageSelected(media)
                }
            ),
        contentAlignment = Alignment.BottomEnd
    ) {
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = ImageRequest.Builder(LocalContext.current)
                .data(media.data)
                .crossfade(true)
                .build(),
            contentScale = ContentScale.Crop,
            filterQuality = FilterQuality.None,
            contentDescription = media.name
        )

        if (media.duration != null) {
            Text(
                modifier = Modifier.padding(vertical = 5.dp, horizontal = 5.dp),
                text = media.duration?.toLong()?.formatMinSec() ?: "00:00",
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White
                )
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = if (media.data == selectedImage) IgOffColor.copy(alpha = 0.8f) else Color.Transparent
                )
        )
    }
}

@UnstableApi
@Preview
@Composable
private fun MediaCardsPreview() {
    MediaCardItem(
        media = Media(
            id = "1",
            name = "",
            data = null,
            duration = null,
            timeStamp = null,
            mimeType = null
        ),
        selectedImage = Uri.EMPTY,
        onImageSelected = { }
    )
}