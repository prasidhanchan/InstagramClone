package com.instagramclone.firebase.repository

import com.instagramclone.util.models.Post

interface UploadContentRepository {

    suspend fun  uploadPost(post: Post, onSuccess: () -> Unit, onError: (String) -> Unit)
}