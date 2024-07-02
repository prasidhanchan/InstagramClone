package com.instagramclone.story

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import com.instagramclone.story.components.StoryScreenCard
import com.instagramclone.util.constants.Utils.IgBackground
import com.instagramclone.util.models.Story
import com.instagramclone.util.models.UserStory

@Composable
fun StoryScreen(
    userStories: List<UserStory>,
    innerPadding: PaddingValues,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit
) {
    var currentStoryIndex by remember { mutableIntStateOf(0) }
    var userStoryIndex by remember { mutableIntStateOf(0) }

    var offsetY by remember { mutableFloatStateOf(0f) }

    var scale by remember { mutableFloatStateOf(1f) }
    val animateScale by animateFloatAsState(
        targetValue = scale,
        animationSpec = spring(),
        label = "animatedScale"
    )

    val screenWidth = LocalConfiguration.current.screenWidthDp

    if (userStories.isNotEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = IgBackground.copy(alpha = 0.4f)),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = modifier
                    .fillMaxSize()
                    .offset {
                        IntOffset(x = 0, y = offsetY.toInt())
                    }
                    .scale(animateScale)
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragEnd = {
                                if (offsetY <= 800) {
                                    scale = 1f
                                    offsetY = 0f
                                } else {
                                    scale = 1f // TODO: Remove scale and offset
                                    offsetY = 0f
                                    onDismiss()
                                }
                            },
                            onDrag = { change, dragAmount ->
                                change.consume()
                                if (offsetY >= 0) offsetY += dragAmount.y else offsetY = 0f
                                scale = (scale - dragAmount.y / 5000).coerceIn(0.8f, 1f)
                            }
                        )
                    }
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = { offset ->
                                if (offset.x <= screenWidth / 2) {
                                    if (currentStoryIndex > 0) { // If current story is not first story then switch to previous story
                                        currentStoryIndex -= 1
                                    } else {
                                        if (userStoryIndex > 0) { // Else switch to previous user's story if exists
                                            userStoryIndex -= 1
                                            currentStoryIndex = userStories[userStoryIndex].stories.lastIndex // Switch to the previous user's last story
                                        }
                                    }
                                } else {
                                    // If current story is not last story then switch to next story
                                    if (currentStoryIndex < userStories[userStoryIndex].stories.lastIndex) {
                                        currentStoryIndex += 1
                                    } else{
                                        if (userStoryIndex < userStories.lastIndex) { // Else switch to next user's story if exists
                                            userStoryIndex += 1
                                            currentStoryIndex = 0 // Start from first story of the next user
                                        }
                                    }
                                }
                            }
                        )
                    },
                color = IgBackground
            ) {
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    StoryScreenCard(
                        userStory = userStories[userStoryIndex],
                        currentStoryIndex = currentStoryIndex,
                        onFinish = {
                            // If the current story is not the last story then switch to next story
                            if (currentStoryIndex < userStories[userStoryIndex].stories.lastIndex) {
                                currentStoryIndex += 1
                            } else {
                                // Else switch to next user story if exists
                                if (userStoryIndex < userStories.lastIndex) {
                                    userStoryIndex += 1
                                    currentStoryIndex = 0 // Start from first story of the next user
                                } else {
                                    onDismiss() // Exit StoryScreen otherwise
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0XFF000000
)
@Composable
private fun StoryScreenPreview() {
    StoryScreen(
        userStories = listOf(
            UserStory(
                username = "pra_sidh_22",
                stories = listOf(
                    Story(
                        timeStamp = 1719840723950L
                    ),
                    Story()
                )
            ),
            UserStory(
                username = "_kawaki_"
            )
        ),
        innerPadding = PaddingValues(),
        onDismiss = { }
    )
}