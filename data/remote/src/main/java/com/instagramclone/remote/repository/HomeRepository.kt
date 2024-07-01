package com.instagramclone.remote.repository

import com.google.firebase.auth.FirebaseUser
import com.instagramclone.remote.models.IGUser
import com.instagramclone.util.models.DataOrException
import com.instagramclone.util.models.Post
import com.instagramclone.util.models.UserStory
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    suspend fun getUserData(currentUser: FirebaseUser?): DataOrException<IGUser, Boolean, Exception>

    suspend fun getPosts(following: List<String>, currentUserId: String): Flow<DataOrException<List<Post>, Boolean, Exception>>

    suspend fun getStories(following: List<String>, currentUserId: String): Flow<DataOrException<List<UserStory>, Boolean, Exception>>
}