package com.instagramclone.ui.components

import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.instagramclone.util.models.Story
import com.instagramclone.util.models.UserStory

@Composable
fun Stories(
    profileImage: String,
    currentUserId: String,
    onAddStoryClick: () -> Unit,
    onStoryClick: () -> Unit,
    userStories: List<UserStory>
) {
    LazyRow(
        content = {
            item(
                key = "addStory"
            ) {
                if (!userStories.any { story -> story.userId == currentUserId }) {
                    AddStoryCard(
                        profileImage = profileImage,
                        onClick = onAddStoryClick
                    )
                } else {
                    StoryCard(
                        userStory = userStories.first { story -> story.userId == currentUserId },
                        currentUserId = currentUserId,
                        onClick = onAddStoryClick
                    )
                }
            }

            if (userStories.isNotEmpty()) {
                items(
                    items = userStories.filter { story -> story.userId != currentUserId }, // Filter out the current user stories
                    key = { userStory -> userStory.userId }
                ) { story ->
                    StoryCard(
                        userStory = story,
                        currentUserId = currentUserId,
                        onClick = onStoryClick
                    )
                }
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
        userStories = listOf(
            UserStory(
                stories = listOf(
                    Story(
                        userId = "pra_sidh_22"
                    ),
                    Story(
                        userId = "youtubeindia"
                    ),
                    Story(
                        userId = "virat.kohli"
                    ),
                    Story(
                        userId = "mustang"
                    ),
                    Story(
                        userId = "googlefordevs"
                    )
                )
            )
        )
    )
}