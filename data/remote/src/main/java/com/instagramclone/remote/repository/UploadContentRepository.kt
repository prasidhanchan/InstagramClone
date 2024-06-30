package com.instagramclone.remote.repository

import com.instagramclone.util.models.Post

interface UploadContentRepository {

    suspend fun  uploadPost(post: Post, onSuccess: () -> Unit, onError: (String) -> Unit)
}