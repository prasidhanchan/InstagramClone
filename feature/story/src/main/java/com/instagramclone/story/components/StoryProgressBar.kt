package com.instagramclone.story.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

/**
 * Story progress bar composable to indicate the progress of each story.
 * @param inFocus Whether the story is in focus or not.
 * @param isFirstStory Whether the story is the first story or not.
 * @param modifier Modifier to be applied to the Progress bar.
 * @param onFinish Callback to be invoked when the progress is complete.
 */
@Composable
fun StoryProgressBar(
    inFocus: Boolean,
    isFirstStory: Boolean,
    modifier: Modifier = Modifier,
    onFinish: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(15.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        var progress by remember { mutableFloatStateOf(0f) }
        val animatedProgress by animateFloatAsState(
            targetValue = progress,
            animationSpec = tween(durationMillis = 100),
            label = "animatedProgress"
        )

        LaunchedEffect(key1 = inFocus) {
            if (isFirstStory) progress = 0f
            if (inFocus) {
                delay(400L)
                repeat(20) {
                    progress += 0.05f
                    delay(100L)
                }

                // On progress complete
                onFinish()
            }

            // If story is half seen and switched to the next one then complete the progress
            if (!inFocus && progress > 0f) progress = 1f
        }

        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(2.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Box(
                modifier = Modifier
                    .padding(horizontal = 2.dp)
                    .fillMaxSize()
                    .clip(RoundedCornerShape(20.dp))
                    .background(color = Color.White.copy(alpha = 0.5f)),
            )

            Box(
                modifier = Modifier
                    .padding(horizontal = 2.dp)
                    .fillMaxHeight()
                    .fillMaxWidth(animatedProgress)
                    .clip(RoundedCornerShape(20.dp))
                    .background(color = Color.White),
            )
        }
    }
}

@Preview
@Composable
private fun StoryProgressBarPreview() {
    StoryProgressBar(
        inFocus = true,
        isFirstStory = false,
        onFinish = { }
    )
}