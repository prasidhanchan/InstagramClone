package com.instagramclone.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.instagramclone.ui.R
import com.instagramclone.util.constants.Utils

@Composable
fun AddStoryCard(
    profileImage: String,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .width(80.dp)
            .height(100.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .clickable(
                    indication = null,
                    interactionSource = interactionSource,
                    onClick = onClick
                ),
            contentAlignment = Alignment.BottomEnd
        ) {

            Surface(
                modifier = Modifier.size(75.dp),
                shape = CircleShape,
                color = Utils.IgOffBlack
            ) {
                if (profileImage.isNotEmpty()) {
                    AsyncImage(
                        modifier = Modifier.fillMaxSize(),
                        model = profileImage,
                        contentScale = ContentScale.Crop,
                        filterQuality = FilterQuality.None,
                        contentDescription = stringResource(id = R.string.profile_image)
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.profile),
                        contentDescription = stringResource(id = R.string.profile_image)
                    )
                }
            }
            Surface(
                modifier = Modifier
                    .size(28.dp)
                    .offset(x = (-2).dp),
                shape = CircleShape,
                color = Utils.IgBlue,
                border = BorderStroke(width = 2.dp, color = Utils.IgOffBlack)
            ) {
                Icon(
                    modifier = Modifier.padding(5.dp),
                    imageVector = Icons.Filled.Add,
                    tint = Color.White,
                    contentDescription = stringResource(R.string.add_story)
                )
            }
        }
        Text(
            modifier = Modifier.padding(vertical = 2.dp),
            text = stringResource(R.string.your_story),
            style = TextStyle(
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = Color.White
            )
        )
    }
}

@Preview(apiLevel = 33)
@Composable
fun AddStoryCardPreview() {
    AddStoryCard(
        profileImage = "",
        onClick = { }
    )
}