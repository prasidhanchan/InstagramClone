package com.instagramclone.util.models

data class Story(
    var userId: String = "",
    var timeStamp: Long = 0L,
    var image: String = "",
    var isVerified: Boolean = false,
    var views: List<String> = emptyList(),
    var liked: Boolean = false,
    var mimeType : String = ""
)
