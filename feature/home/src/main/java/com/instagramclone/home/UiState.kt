package com.instagramclone.home

import com.instagramclone.util.models.Post
import com.instagramclone.util.models.Story

data class UiState(
    var name: String = "",
    var username: String = "",
    var profileImage: String = "",
    var posts: List<Post> = emptyList(),
    var stories: List<Story> = emptyList(),
    var isLoading: Boolean = false,
    var error: String = ""
)
