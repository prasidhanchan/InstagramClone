package com.instagramclone.story.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
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
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Story progress bar composable to indicate the progress of each story.
 * @param inFocus Whether the story is in focus or not.
 * @param isLongPressed Whether the story is long pressed or not, i.e to pause the progress.
 * @param isFirstStory Whether the story is the first story or not.
 * @param modifier Modifier to be applied to the Progress bar.
 * @param onFinish Callback to be invoked when the progress is complete.
 */
@Composable
fun StoryProgressBar(
    inFocus: Boolean,
    isLongPressed: Boolean,
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
        val animateProgress = remember { Animatable(0f) }

        var lastTime by remember { mutableLongStateOf(0L) }

        LaunchedEffect(key1 = isFirstStory) {
            if (isFirstStory) animateProgress.snapTo(0f) // If switched back to first story from next then set progress to 0
        }

        LaunchedEffect(key1 = inFocus, key2 = isLongPressed) {
            launch(Dispatchers.Default) {
                if (inFocus) {
                    if (!isLongPressed) {
                        val currentTime = System.currentTimeMillis()
                        if (lastTime == 0L) lastTime = System.currentTimeMillis()
                        val remainingTime =
                            currentTime - lastTime // Calculating remaining time to avoid animation slow down

                        animateProgress.animateTo(
                            targetValue = 1f,
                            animationSpec = tween(
                                durationMillis = (4500 - remainingTime.toInt()).coerceAtLeast(500),
                                easing = LinearEasing
                            ),
                            block = { if (value >= 1f) onFinish() } // On progress complete
                        )
                    }
                } else {
                    lastTime = 0L // Restore the elapsed time for next story of the same user
                }

                // If story is half seen and switched to the next one then complete the progress
                if (!inFocus && animateProgress.value > 0f) animateProgress.snapTo(1f)
            }
        }

        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(1.dp),
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
                    .fillMaxWidth(animateProgress.value)
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
        isLongPressed = false,
        isFirstStory = false,
        onFinish = { }
    )
}