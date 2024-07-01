package com.instagramclone.remote.repository

import com.instagramclone.util.models.Post
import com.instagramclone.util.models.UserStory

interface UploadContentRepository {

    suspend fun uploadPost(post: Post, onSuccess: () -> Unit, onError: (String) -> Unit)

    suspend fun uploadStory(
        userStory: UserStory,
        currentUserId: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    )
}