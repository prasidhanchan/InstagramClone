package com.instagramclone.util.models

import android.net.Uri
import android.support.v4.media.session.PlaybackStateCompat.State

@State
data class Image(
    val id: String?,
    val name: String?,
    val data: Uri?
)
