package com.instagramclone.upload.components.story

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.instagramclone.util.constants.Utils.IgOffBackground
import com.instagramclone.util.constants.formatMinSec
import com.instagramclone.util.models.Media

@Composable
fun MediaCardItem(
    modifier: Modifier = Modifier,
    media: Media,
    onMediaSelected: (Media) -> Unit
) {
    val igBackground = IgOffBackground
    var color by remember { mutableStateOf(igBackground) }

    Surface(
        modifier = modifier
            .height(220.dp)
            .width(120.dp)
            .padding(all = 1.dp)
            .clickable(
                onClick = { onMediaSelected(media) }
            ),
        color = IgOffBackground
    ) {
        Box(
            modifier = Modifier.wrapContentSize(Alignment.Center),
            contentAlignment = Alignment.BottomEnd
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color.copy(alpha = 0.5f))
            )

            if (media.duration != null) {
                Text(
                    modifier = Modifier.padding(vertical = 5.dp, horizontal = 5.dp),
                    text = media.duration?.toLong()?.formatMinSec() ?: "00:00",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha = 0.6f)
                    )
                )
            }

            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(media.data)
                    .crossfade(true)
                    .allowHardware(false)
                    .listener(
                        onSuccess = { _, result ->
                            Palette.Builder(result.drawable.toBitmap()).generate { palette ->
                                palette?.let { mPalette ->
                                    color = Color(mPalette.dominantSwatch?.rgb ?: 1)
                                }
                            }
                        }
                    )
                    .build(),
                contentScale = ContentScale.FillWidth,
                contentDescription = media.name
            )
        }
    }
}

@Preview
@Composable
private fun MediaCardPreview() {
    MediaCardItem(
        media = Media(
            id = "1",
            name = "",
            data = null,
            duration = "1000",
            timeStamp = null,
            mimeType = null
        ),
        onMediaSelected = { }
    )
}