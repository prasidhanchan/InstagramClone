package com.instagramclone.story.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.instagramclone.util.models.UserStory

/**
 * IG Story pager to display the user's stories.
 * @param state Pager state to control the pager.
 * @param modifier Modifier to be applied to the Pager.
 * @param userScrollEnabled Whether the user can scroll the pager or not.
 * @param content Content to be displayed in each page.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun IGStoryPager(
    state: PagerState,
    modifier: Modifier = Modifier,
    userScrollEnabled: Boolean = true,
    content: @Composable (Int) -> Unit
) {
    HorizontalPager(
        state = state,
        modifier = Modifier
            .fillMaxSize()
            .then(modifier),
        userScrollEnabled = userScrollEnabled,
        flingBehavior = PagerDefaults.flingBehavior(
            state = state,
            lowVelocityAnimationSpec = spring(stiffness = Spring.StiffnessLow),
            snapPositionalThreshold = 0.5f
        )
    ) { page ->
        content(page)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
private fun IGStoryPagerPreview() {
    val userStories = listOf(
        UserStory(
            username = "pra_sidh_22",
            stories = listOf()
        ),
        UserStory(
            username = "_kawaki_",
            stories = listOf()
        ),
        UserStory(
            username = "virat.kohli",
            stories = listOf()
        ),
        UserStory(
            username = "android",
            stories = listOf()
        )
    )

    val state = rememberPagerState(initialPage = 0, pageCount = { userStories.size })

    IGStoryPager(
        state = state,
        content = { page ->
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = when (page) {
                    0 -> Color.Black
                    1 -> Color.Green
                    2 -> Color.Blue.copy(alpha = 0.5f)
                    else -> Color.Yellow
                }
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = userStories[page].username,
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.background
                        )
                    )
                }
            }
        }
    )
}