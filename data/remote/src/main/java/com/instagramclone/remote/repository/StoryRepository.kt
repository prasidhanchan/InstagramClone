package com.instagramclone.remote.repository

import com.instagramclone.util.models.Story

interface StoryRepository {

    suspend fun deleteStory(story: Story,currentUserId: String, onSuccess: () -> Unit, onError: (String) -> Unit)

    suspend fun updateStoryViews(story: Story, currentUserId: String, onError: (String) -> Unit)
}