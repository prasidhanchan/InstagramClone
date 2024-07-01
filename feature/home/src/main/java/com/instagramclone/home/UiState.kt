package com.instagramclone.home

import com.instagramclone.util.models.Post
import com.instagramclone.util.models.UserStory

data class UiState(
    var posts: List<Post> = emptyList(),
    var userStories: List<UserStory> = emptyList(),
    var isLoading: Boolean = true,
    var error: String = ""
)
