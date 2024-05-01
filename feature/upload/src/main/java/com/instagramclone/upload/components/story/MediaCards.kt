package com.instagramclone.upload.components.story

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.instagramclone.ui.R
import com.instagramclone.util.constants.Utils.IgOffBackground
import com.instagramclone.util.constants.formatMinSec
import com.instagramclone.util.models.Media

/**
 * MediaCards composable for Stories to display selected image or video
 * @param modifier Requires [Modifier]
 * @param mediaList Requires List of [Media], i.e Images and Videos
 */
@Composable
fun MediaCards(
    modifier: Modifier = Modifier,
    mediaList: List<Media>,
    onPhotosClick: () -> Unit,
    onVideosClick: () -> Unit,
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(3),
        modifier = Modifier.fillMaxWidth()
    ) {
        item(
            key = "SelectionCards",
            span = StaggeredGridItemSpan.FullLine
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                MediaSelectionCard(
                    text = stringResource(R.string.photos),
                    icon = R.drawable.photos,
                    onClick = onPhotosClick
                )
                MediaSelectionCard(
                    text = stringResource(R.string.videos),
                    icon = R.drawable.videos,
                    onClick = onVideosClick
                )
            }
        }

        item(
            key = "RecentsHeader",
            span = StaggeredGridItemSpan.FullLine
        ) {
            Text(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp, bottom = 10.dp, start = 20.dp, end = 20.dp),
                text = stringResource(id = R.string.recents),
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Start
                )
            )
        }

        items(
            items = mediaList,
            key = { media -> media.id!! }
        ) { media ->
            MediaCardItem(media = media)
        }
    }
}

@Composable
fun MediaCardItem(
    modifier: Modifier = Modifier,
    media: Media
) {
    val igBackground = IgOffBackground
    var color by remember { mutableStateOf(igBackground) }

    Surface(
        modifier = modifier
            .height(220.dp)
            .width(120.dp)
            .padding(all = 1.dp),
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
        )
    )
}