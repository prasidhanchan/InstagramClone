package com.instagramclone.util.models

data class Story(
    var username: String = "",
    var profileImage: String = "",
    var timeStamp: Long = 0L,
    var image: String = "",
    var isVerified: Boolean = false,
    var isViewed: Boolean = false,
    var likes: Boolean= false
)
