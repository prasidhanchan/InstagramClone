package com.instagramclone.home

import com.instagramclone.util.models.Post
import com.instagramclone.util.models.Story

data class UiState(
    var posts: List<Post> = emptyList(),
    var stories: List<Story> = emptyList(),
    var isLoading: Boolean = true,
    var error: String = ""
)
