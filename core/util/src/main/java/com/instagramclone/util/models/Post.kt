package com.instagramclone.util.models

data class Post(
    var profileImage: String = "",
    var username: String = "",
    var verified: Boolean = false,
    var image: String = "",
    var likes: Int = 0,
    var comments: List<String> = emptyList()
)
