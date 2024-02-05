package com.instagramclone.util.models

data class Post(
    var profileImage: String = "",
    var username: String = "",
    var timeStamp: Long = 0L,
    var isVerified: Boolean = false,
    var images: List<String> = emptyList(),
    var likes: Int = 0,
    var comments: List<String> = emptyList(),
    var caption: String = ""
)
