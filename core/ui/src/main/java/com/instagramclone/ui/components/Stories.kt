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
    userStories: List<UserStory>,
    myStories: List<UserStory>
) {
    LazyRow(
        content = {
            item(key = "addStory") {
                if (myStories.isNotEmpty() && myStories.first().stories.isNotEmpty()) {
                    StoryCard(
                        userStory = myStories.first(),
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
                    items = userStories,
                    key = { userStory -> userStory.userId }
                ) { story ->
                    StoryCard(
                        userStory = story,
                        currentUserId = currentUserId,
                        onClick = { onStoryClick(userStories.indexOf(story)) }
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
        ),
        myStories = listOf(
            UserStory(
                stories = listOf(
                    Story(
                        userId = "pra_sidh_22"
                    ),
                    Story(
                        userId = "youtubeindia"
                    )
                )
            )
        )
    )
}