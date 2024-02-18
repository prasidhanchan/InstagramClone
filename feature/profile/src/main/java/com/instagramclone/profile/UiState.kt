package com.instagramclone.profile

import com.instagramclone.util.models.Post

data class UiState(
    var profileImage: String = "",
    var username: String = "",
    var name: String = "",
    var bio: String = "",
    var links: String = "",
    var gender: String = "Unknown",
    var myPosts: List<Post> = emptyList(),
    var followers: List<String> = emptyList(),
    var following: List<String> = emptyList(),
    var isLoading: Boolean = false,
    var isUpdating: Boolean = false,
    var isUserDetailChanged: Boolean = false,
    var error: String = "",
    var textState: String = ""
)
