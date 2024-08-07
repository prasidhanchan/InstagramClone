package com.instagramclone.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.instagramclone.ui.R
import com.instagramclone.util.constants.Utils.IgBackground
import com.instagramclone.util.constants.Utils.IgOffBackground
import com.instagramclone.util.models.Story
import com.instagramclone.util.models.UserStory

@Composable
fun StoryCard(
    userStory: UserStory,
    currentUserId: String,
    onClick: () -> Unit
) {
    var scale by remember { mutableFloatStateOf(1f) }
    val animatedScale by animateFloatAsState(
        targetValue = scale,
        animationSpec = tween(200),
        label = "storyCardAnimation"
    )

    val isAllStoryViewed = userStory.stories.last().views.contains(currentUserId)

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
                    .scale(animatedScale)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = {
                                scale = 0.9f
                                awaitRelease()
                                onClick()
                                scale = 1f
                            },
                            onLongPress = { scale = 0.9f }
                        )
                    }
                    .size(80.dp)
                    .border(
                        brush = Brush.linearGradient(
                            start = Offset(x = 0.0f, y = 50.0f),
                            end = Offset(x = 200.0f, y = 250.0f),
                            colors = if (isAllStoryViewed) {
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
                        width = if (isAllStoryViewed) 1.dp else 2.dp
                    )
                    .border(width = 5.dp, color = IgBackground, shape = CircleShape),
                color = IgOffBackground
            ) {
                AsyncImage(
                    modifier = Modifier.fillMaxSize(),
                    model = userStory.profileImage,
                    contentScale = ContentScale.Crop,
                    filterQuality = FilterQuality.None,
                    contentDescription = stringResource(
                        R.string.story_placeholder,
                        userStory.username
                    )
                )
            }

            Text(
                modifier = Modifier.padding(vertical = 2.dp),
                text = if (userStory.userId != currentUserId) userStory.username
                else stringResource(id = R.string.your_story),
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    }
}

@Preview
@Composable
fun StoryCardPreview() {
    StoryCard(
        userStory = UserStory(
            stories = listOf(
                Story(
                    userId = "pra_sidh_22",
                    views = listOf("1")
                )
            )
        ),
        currentUserId = "1",
        onClick = { }
    )
}