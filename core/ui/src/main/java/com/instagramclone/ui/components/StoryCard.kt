package com.instagramclone.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
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
import com.instagramclone.util.models.Story

@Composable
fun StoryCard(
    story: Story,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    var size by remember { mutableFloatStateOf(1f) }
    val scale by animateFloatAsState(
        animationSpec = tween(100),
        targetValue = size,
        finishedListener = { size = 1f },
        label = ""
    )
    Box(
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .height(100.dp)
            .width(80.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                shape = CircleShape,
                modifier = Modifier
                    .scale(scale)
                    .clickable(
                        indication = null,
                        interactionSource = interactionSource,
                        onClick = {
                            size = 0.9f
                            onClick()
                        }
                    )
                    .size(80.dp)
                    .border(
                        brush = Brush.linearGradient(
                            start = Offset(x = 0.0f, y = 50.0f),
                            end = Offset(x = 200.0f, y = 250.0f),
                            colors = if (story.isViewed) {
                                listOf(
                                    Color.DarkGray,
                                    Color.DarkGray
                                )
                            } else {
                                listOf(
                                    Color(0xFFFF00DE),
                                    Color(0xFFC7181E),
                                    Color(0xFFC7181E),
                                    Color(0xFFFFEB3B),
                                    Color(0xFFC7181E),
                                )
                            }
                        ),
                        shape = CircleShape,
                        width = if (story.isViewed) 1.dp else 2.dp
                    )
                    .border(width = 5.dp, color = Utils.IgBlack, shape = CircleShape),
                color = Utils.IgOffBlack
            ) {
                if (story.profileImage.isNotEmpty()) {
                    AsyncImage(
                        modifier = Modifier.fillMaxSize(),
                        model = story.profileImage,
                        contentScale = ContentScale.Crop,
                        filterQuality = FilterQuality.Low,
                        contentDescription = stringResource(R.string.story_placeholder, story.username)
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.profile),
                        contentDescription = stringResource(id = R.string.profile_image)
                    )
                }
            }
            Text(
                modifier = Modifier.padding(vertical = 2.dp),
                text = story.username,
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White
                )
            )
        }
    }
}

@Preview(apiLevel = 33)
@Composable
fun StoryCardPreview() {
    StoryCard(
        story = Story(
            username = "pra_sidh_22",
            isViewed = true
        ),
        onClick = {  }
    )
}