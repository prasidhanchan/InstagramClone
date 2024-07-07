package com.instagramclone.upload.components.story

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.palette.graphics.Palette
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.instagramclone.ui.components.IGPlayer
import com.instagramclone.util.constants.Utils.IgOffBackground
import com.instagramclone.util.models.Media

/**
 * Selected story composable to display the selected media, i.e, the video or image.
 * @param selectedMedia The media that is currently selected.
 * @param modifier The Modifier to be applied the card.
 * @param exoPlayer Requires the [ExoPlayer] instance from ViewModel.
 * @param isUploading Indicates whether the story is being uploaded or not.
 */
@UnstableApi
@Composable
fun SelectedStoryCard(
    selectedMedia: Media,
    modifier: Modifier = Modifier,
    exoPlayer: ExoPlayer,
    isUploading: Boolean
) {
    val igOffBackground = IgOffBackground
    var color by remember { mutableStateOf(igOffBackground) }

    Surface(
        modifier = modifier
            .padding(vertical = 10.dp)
            .fillMaxWidth()
            .fillMaxHeight(0.8f),
        shape = RoundedCornerShape(30.dp),
        color = color
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (selectedMedia.duration == null) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(selectedMedia.data)
                        .crossfade(true)
                        .allowHardware(false)
                        .listener(
                            onSuccess = { _, result ->
                                Palette.Builder(result.drawable.toBitmap()).generate { palette ->
                                    palette?.let { mPalette ->
                                        color =
                                            Color(mPalette.getDominantColor(igOffBackground.toArgb()))
                                    }
                                }
                            }
                        )
                        .build(),
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit,
                    contentDescription = selectedMedia.name
                )
            } else {
                IGPlayer(
                    resize = AspectRatioFrameLayout.RESIZE_MODE_FIT,
                    exoPlayer = exoPlayer,
                    onLongPress = { exoPlayer.pause() },
                    onPress = { exoPlayer.play() }
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = if (isUploading) Color.Black.copy(alpha = 0.5f) else Color.Transparent)
            )

            if (isUploading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(40.dp),
                    color = Color.White,
                    strokeWidth = 4.dp,
                    strokeCap = StrokeCap.Round
                )
            }
        }
    }
}