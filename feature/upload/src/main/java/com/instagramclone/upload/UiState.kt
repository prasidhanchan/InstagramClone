package com.instagramclone.upload

import com.instagramclone.util.models.Media

data class UiState(
    var mediaList: List<Media> = emptyList(),
    var selectedMedia: Media? = null,
    var caption: String = "",
    var isUploading: Boolean = false,
    var error: String = "",
    var playerCurrentPosition: Long = 0L,
    var playerDuration: Long = 0L
)
