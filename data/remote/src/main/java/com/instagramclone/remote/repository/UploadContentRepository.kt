package com.instagramclone.remote.repository

import com.instagramclone.util.models.Post
import com.instagramclone.util.models.Story

interface UploadContentRepository {

    suspend fun  uploadPost(post: Post, onSuccess: () -> Unit, onError: (String) -> Unit)

    suspend fun uploadStory(story: Story, onSuccess: () -> Unit, onError: (String) -> Unit)
}