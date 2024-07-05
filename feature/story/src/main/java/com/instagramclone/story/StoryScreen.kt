package com.instagramclone.story

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import com.instagramclone.story.components.IGStoryPager
import com.instagramclone.story.components.StoryScreenCard
import com.instagramclone.util.constants.Utils.IgBackground
import com.instagramclone.util.constants.getAlphaForBackground
import com.instagramclone.util.models.Story
import com.instagramclone.util.models.UserStory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StoryScreen(
    storyIndex: Int,
    visible: Boolean,
    userStories: () -> List<UserStory>,
    innerPadding: PaddingValues,
    onDismiss: () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + scaleIn(
            animationSpec = tween(),
            transformOrigin = TransformOrigin(0.5f, 0.1f)
        ),
        exit = scaleOut(
            animationSpec = tween(),
            transformOrigin = TransformOrigin(0.5f, 0.1f)
        ) + fadeOut(animationSpec = tween(durationMillis = 600))
    ) {
        val state = rememberPagerState(
            initialPage = 0,
            pageCount = { userStories().size }
        )

        LaunchedEffect(key1 = storyIndex) {
            state.scrollToPage(page = storyIndex)
        }

        val scope = rememberCoroutineScope()

        var currentStoryIndex by remember { mutableIntStateOf(0) }
        var userStoryIndex by remember { mutableIntStateOf(0) }

        var offsetY by remember { mutableFloatStateOf(0f) }
        val animateOffsetY by animateFloatAsState(
            targetValue = offsetY,
            animationSpec = spring(stiffness = Spring.StiffnessLow),
            label = "animatedOffset"
        )

        var scale by remember { mutableFloatStateOf(1f) }
        val animateScale by animateFloatAsState(
            targetValue = scale,
            animationSpec = spring(stiffness = Spring.StiffnessLow),
            label = "animatedScale"
        )

        var alpha by remember { mutableFloatStateOf(1f) }
        val animatedAlpha by animateFloatAsState(
            targetValue = alpha,
            animationSpec = tween(),
            label = "animatedAlpha"
        )

        var isLongPressed by remember { mutableStateOf(false) }

        val screenWidth = LocalConfiguration.current.screenWidthDp

        if (userStories().isNotEmpty()) {
            IGStoryPager(
                state = state,
                modifier = Modifier
                    .padding(innerPadding)
                    .background(color = if (visible) IgBackground.copy(alpha = animatedAlpha) else Color.Transparent)
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragEnd = {
                                if (offsetY <= 200) {
                                    scale = 1f
                                    offsetY = 0f
                                } else {
                                    onDismiss()
                                }

                                // Animate alpha as per drag offset on end drag
                                alpha = getAlphaForBackground(offsetY = offsetY)
                            },
                            onDrag = { change, dragAmount ->
                                change.consume()
                                // Can only drag between -10f(max top) and 500f(max bottom)
                                if (offsetY >= -10f && offsetY <= 500f) offsetY += dragAmount.y
                                else offsetY = 0f
                                scale = (scale - dragAmount.y / 1000).coerceIn(0.9f, 1f)

                                // Animate alpha as per drag offset
                                alpha = getAlphaForBackground(offsetY = offsetY)
                            }
                        )
                    }
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onLongPress = { isLongPressed = true },
                            onPress = {
                                awaitRelease()
                                isLongPressed = false
                            },
                            onTap = { offset ->
                                if (offset.x <= screenWidth / 2) {
                                    if (currentStoryIndex > 0) { // If current story is not first story then switch to previous story
                                        currentStoryIndex -= 1
                                    } else {
                                        if (userStoryIndex > 0) { // Else switch to previous user's story if exists
                                            scope.launch(Dispatchers.Main) {
                                                state.animateScrollToPage(
                                                    page = state.currentPage - 1,
                                                    animationSpec = spring(stiffness = Spring.StiffnessLow)
                                                )
                                            }
                                        }
                                    }
                                } else {
                                    // If current story is not last story then switch to next story
                                    if (currentStoryIndex < userStories()[userStoryIndex].stories.lastIndex) {
                                        currentStoryIndex += 1
                                    } else {
                                        if (userStoryIndex < userStories().lastIndex) { // Else switch to next user's story if exists
                                            scope.launch(Dispatchers.Main) {
                                                state.animateScrollToPage(
                                                    page = state.currentPage + 1,
                                                    animationSpec = spring(stiffness = Spring.StiffnessLow)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        )
                    },
                userScrollEnabled = offsetY == 0f && userStories().size != 1,
            ) { page ->
                userStoryIndex = page // update UserStoryIndex on page change

                if (state.isScrollInProgress) currentStoryIndex = 0 // Switching index back to 0 on user story change

                // Check if the current page is fully visible or is dragged
                val inFocus = state.currentPageOffsetFraction == 0f

                StoryScreenCard(
                    userStory = userStories()[page],
                    currentStoryIndex = currentStoryIndex,
                    modifier = Modifier
                        .offset {
                            IntOffset(x = 0, y = animateOffsetY.roundToInt())
                        }
                        .scale(animateScale),
                    inFocus = inFocus,
                    isLongPressed = isLongPressed,
                    onFinish = {
                        // If the current story is not the last story then switch to next story
                        if (currentStoryIndex < userStories()[page].stories.lastIndex) {
                            currentStoryIndex += 1
                        } else {
                            // Else switch to next user story if exists
                            if (page < userStories().lastIndex) {
                                scope.launch {
                                    state.animateScrollToPage(
                                        page = state.currentPage + 1,
                                        animationSpec = spring(stiffness = Spring.StiffnessLow)
                                    )
                                }
                            } else {
                                onDismiss() // Exit StoryScreen otherwise
                            }
                        }
                    }
                )
            }
        }

        BackHandler(onBack = onDismiss)
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0XFF000000
)
@Composable
private fun StoryScreenPreview() {
    StoryScreen(
        storyIndex = 0,
        visible = true,
        userStories = {
            listOf(
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
            )
        },
        innerPadding = PaddingValues(),
        onDismiss = { }
    )
}