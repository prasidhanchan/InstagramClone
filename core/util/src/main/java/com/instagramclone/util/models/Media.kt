package com.instagramclone.util.models

import android.net.Uri
import androidx.compose.runtime.Stable

@Stable
data class Media(
    val id: String?,
    val name: String?,
    val data: Uri?,
    val duration: String?,
    val timeStamp: Long?,
    val mimeType: String?
)
