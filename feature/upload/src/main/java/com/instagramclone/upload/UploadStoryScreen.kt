package com.instagramclone.upload

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.instagramclone.ui.R
import com.instagramclone.ui.components.IGRegularAppBar
import com.instagramclone.upload.components.story.MediaCardItem
import com.instagramclone.upload.components.story.MediaSelectionCard
import com.instagramclone.util.constants.Utils.IgBackground
import com.instagramclone.util.models.Media

@Composable
fun UploadStoryScreen(
    innerPadding: PaddingValues,
    mediaList: List<Media>,
    onPhotosClick: () -> Unit,
    onVideosClick: () -> Unit,
    onStorySelected: (Media) -> Unit,
    onBackClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = IgBackground
    ) {
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IGRegularAppBar(
                text = stringResource(id = R.string.add_story),
                leadingIcon = R.drawable.cross,
                textAlign = TextAlign.Center,
                onBackClick = onBackClick
            )

            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(3),
                modifier = Modifier.fillMaxWidth()
            ) {
                item(
                    key = "selectionCards",
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
                    key = "recentsHeader",
                    span = StaggeredGridItemSpan.FullLine
                ) {
                    Text(
                        modifier = Modifier
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
                    MediaCardItem(
                        media = media,
                        onMediaSelected = onStorySelected
                    )
                }
            }
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
        onStorySelected = { },
        onBackClick = { }
    )
}