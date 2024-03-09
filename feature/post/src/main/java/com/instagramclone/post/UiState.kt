package com.instagramclone.post

import com.instagramclone.util.models.Image

data class UiState(
    var images: List<Image> = emptyList(),
    var selectedImage: Image? = null,
    var caption: String = "",
    var isUploading: Boolean = false,
    var error: String = ""
)
