package com.instagramclone.ui.components

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
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.instagramclone.ui.R
import com.instagramclone.util.constants.Utils
import com.instagramclone.util.models.Image

@Composable
fun ImageCards(
    modifier: Modifier = Modifier,
    images: List<Image>,
    selectedImage: Image?,
    onImageSelected: (Image) -> Unit
) {
    LazyVerticalStaggeredGrid(
        modifier = modifier.fillMaxSize(),
        columns = StaggeredGridCells.Fixed(4)
    ) {
        item(
            key = "selectedImage",
            span = StaggeredGridItemSpan.FullLine
        ) {
            SelectedImageCard(
                selectedImage = selectedImage?.data,
                description = selectedImage?.name
            )
        }

        item(
            key = "imagesDivider",
            span = StaggeredGridItemSpan.FullLine,
            content = { DividerCard() }
        )

        items(
            items = images,
            key = { it.id!! }
        ) { image ->
            ImageItem(
                image = image,
                selectedImage = selectedImage?.data,
                onImageSelected = onImageSelected
            )
        }
    }
}

@Composable
fun SelectedImageCard(
    selectedImage: Uri?,
    description: String?
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = selectedImage,
            contentScale = ContentScale.Crop,
            contentDescription = description
        )
    }
}

@Composable
fun DividerCard() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        color = Utils.IgBlack
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
                    color = Color.White
                )
            )
        }
    }
}

@Composable
fun ImageItem(
    image: Image,
    selectedImage: Uri?,
    onImageSelected: (Image) -> Unit
) {
    Box(
        modifier = Modifier
            .width(80.dp)
            .height(100.dp)
            .padding(all = 1.dp)
            .background(color = Utils.IgOffWhite)
            .clickable(
                onClick = {
                    onImageSelected(image)
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = image.data,
            contentScale = ContentScale.Crop,
            filterQuality = FilterQuality.None,
            contentDescription = image.name
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = if (image.data == selectedImage) Utils.IgOffWhite.copy(alpha = 0.8f) else Color.Transparent
                )
        )
    }
}