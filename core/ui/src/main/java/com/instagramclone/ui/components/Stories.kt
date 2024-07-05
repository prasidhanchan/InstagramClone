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
    onViewMyStoryClick: () -> Unit,
    onStoryClick: (storyIndex: Int) -> Unit,
    userStories: List<UserStory>
) {
    val userStoriesFiltered =
        if (userStories.isNotEmpty()) userStories.filter { story -> story.userId != currentUserId } // Filter out the current user stories
        else emptyList()
    val myStory =
        if (userStories.any { userStory -> userStory.userId == currentUserId })
            userStories.first { story -> story.userId == currentUserId } // Filtering my stories
        else UserStory()

    LazyRow(
        content = {
            item(key = "addStory") {
                if (userStories.any { story -> story.userId == currentUserId }) {
                    StoryCard(
                        userStory = myStory,
                        currentUserId = currentUserId,
                        onClick = onViewMyStoryClick
                    )
                } else {
                    AddStoryCard(
                        profileImage = profileImage,
                        onClick = onAddStoryClick
                    )
                }
            }

            if (userStories.isNotEmpty()) {
                items(
                    items = userStoriesFiltered,
                    key = { userStory -> userStory.userId }
                ) { story ->
                    StoryCard(
                        userStory = story,
                        currentUserId = currentUserId,
                        onClick = { onStoryClick(userStoriesFiltered.indexOf(story)) }
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
        onViewMyStoryClick = { },
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