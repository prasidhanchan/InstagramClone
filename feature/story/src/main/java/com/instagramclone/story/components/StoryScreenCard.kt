package com.instagramclone.story.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.instagramclone.ui.R
import com.instagramclone.util.constants.formatToStoryTimeStamp
import com.instagramclone.util.models.Story
import com.instagramclone.util.models.UserStory

/**
 * Story screen card with progress bar, header and loader to display the user's story.
 * @param userStory User's story to be displayed.
 * @param currentStoryIndex Index of the current story be viewed.
 * @param modifier Modifier to be applied to the Card.
 * @param inFocus Whether the story is in focus or not.
 * @param isLongPressed Whether the story is long pressed or not.
 * @param onFinish Callback invoked when the current story is finished, i.e when the progress is complete.
 */
@Composable
fun StoryScreenCard(
    userStory: UserStory,
    currentStoryIndex: Int,
    modifier: Modifier = Modifier,
    inFocus: Boolean,
    isLongPressed: Boolean,
    onFinish: () -> Unit = { }
) {
    var imageLoaded by remember { mutableStateOf(false) }
    val animatedAlpha by animateFloatAsState(
        targetValue = if (isLongPressed) 0f else 1f,
        animationSpec = tween(durationMillis = 600),
        label = "animatedAlpha"
    )

    val igBackground = Color.Black
    var color by remember { mutableStateOf(igBackground) }
    val animateColor by animateColorAsState(
        targetValue = color,
        animationSpec = tween(durationMillis = 500),
        label = "animatedColor"
    )

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .padding(vertical = 30.dp)
                .fillMaxSize()
                .then(modifier),
            shape = RoundedCornerShape(12.dp),
            color = animateColor
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (userStory.stories.isNotEmpty()) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(userStory.stories[currentStoryIndex].image)
                            .crossfade(true)
                            .allowHardware(false)
                            .listener(
                                onStart = { imageLoaded = false },
                                onSuccess = { _, result ->
                                    imageLoaded = true

                                    Palette.Builder(result.drawable.toBitmap()).generate { palette ->
                                        palette.let { mPalette ->
                                            color = Color(mPalette?.getDominantColor(igBackground.toArgb()) ?: 1)
                                        }
                                    }
                                }
                            )
                            .build(),
                        contentScale = ContentScale.Crop,
                        contentDescription = stringResource(id = R.string.user_story, userStory.username)
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(animatedAlpha),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start
                ) {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 5.dp)
                            .fillMaxWidth()
                            .height(15.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        userStory.stories.forEach { story ->
                            val isFirstStory = currentStoryIndex == 0

                            StoryProgressBar(
                                inFocus = inFocus && currentStoryIndex == userStory.stories.indexOf(story) && imageLoaded,
                                isLongPressed = isLongPressed,
                                isFirstStory = isFirstStory,
                                modifier = Modifier.weight(1f),
                                onFinish = onFinish
                            )
                        }
                    }

                    StoryScreenHeader(
                        userStory = userStory,
                        currentStoryIndex = currentStoryIndex
                    )
                }
            }
        }

        StoryLoader(loading = !imageLoaded)
    }
}

/**
 * Story header to display the user's profile image, username and timestamp.
 * @param userStory User's story.
 * @param currentStoryIndex Index of the current story be viewed.
 * @param modifier Modifier to be applied to the Row.
 * @see StoryScreenCard
 */
@Composable
fun StoryScreenHeader(
    userStory: UserStory,
    currentStoryIndex: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .padding(horizontal = 15.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        AsyncImage(
            model = userStory.profileImage,
            modifier = Modifier
                .padding(end = 10.dp)
                .size(30.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
            contentDescription = userStory.username
        )

        Text(
            text = userStory.username,
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        )

        Spacer(modifier = Modifier.width(10.dp))

        Text(
            text = userStory.stories[currentStoryIndex].timeStamp.formatToStoryTimeStamp(),
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun StoryScreenCardPreview() {
    StoryScreenCard(
        userStory = UserStory(
            username = "pra_sidh_22",
            stories = listOf(
                Story(
                    timeStamp = 1719840723950L
                ),
                Story(
                    timeStamp = 12000L
                )
            )
        ),
        currentStoryIndex = 0,
        inFocus = true,
        isLongPressed = false
    )
}