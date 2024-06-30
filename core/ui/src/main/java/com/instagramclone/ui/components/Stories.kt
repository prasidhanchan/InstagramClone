package com.instagramclone.ui.components

import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.instagramclone.util.models.Story

@Composable
fun Stories(
    profileImage: String,
    currentUserId: String,
    onAddStoryClick: () -> Unit,
    onStoryClick: () -> Unit,
    stories: List<Story>
) {
    LazyRow(
        content = {
            item(
                key = "addStory"
            ) {
                if (!stories.any { story -> story.userId == currentUserId }) {
                    AddStoryCard(
                        profileImage = profileImage,
                        onClick = onAddStoryClick
                    )
                } else {
                    StoryCard(
                        story = stories.first { story -> story.userId == currentUserId },
                        onClick = onAddStoryClick
                    )
                }
            }

            items(
                items = stories.filter { story -> story.userId != currentUserId }, // Filter out the current user stories
                key = { story -> story.username }
            ) { story ->
                StoryCard(
                    story = story,
                    onClick = onStoryClick
                )
            }
        }
    )
}

@Preview
@Composable
fun StoriesPreview() {
    Stories(
        profileImage = "",
        currentUserId = "",
        onAddStoryClick = { },
        onStoryClick = { },
        stories = listOf(
            Story(
                username = "pra_sidh_22"
            ),
            Story(
                username = "youtubeindia"
            ),
            Story(
                username = "virat.kohli"
            ),
            Story(
                username = "mustang"
            ),
            Story(
                username = "googlefordevs"
            )
        )
    )
}