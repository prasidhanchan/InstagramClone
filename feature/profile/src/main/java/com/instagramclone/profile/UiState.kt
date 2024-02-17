package com.instagramclone.profile

data class UiState(
    var profileImage: String = "",
    var username: String = "",
    var name: String = "",
    var bio: String = "",
    var links: String = "",
    var gender: String = "Unknown",
    var myPosts: List<String> = emptyList(),
    var followers: List<String> = emptyList(),
    var following: List<String> = emptyList(),
    var isLoading: Boolean = false,
    var error: String = "",
    var textState: String = ""
)
