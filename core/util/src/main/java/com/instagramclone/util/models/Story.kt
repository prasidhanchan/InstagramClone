package com.instagramclone.util.models

data class Story(
    var username: String = "",
    var profileImage: String = "",
    var timeStamp: Long = 0L,
    var stories: List<String> = emptyList(),
    var isVerified: Boolean = false,
    var isViewed: Boolean = false
)
