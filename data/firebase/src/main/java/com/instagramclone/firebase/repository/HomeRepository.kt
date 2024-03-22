package com.instagramclone.firebase.repository

import com.instagramclone.firebase.models.IGUser
import com.instagramclone.util.models.DataOrException
import com.instagramclone.util.models.Post
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    suspend fun getUserData(): DataOrException<IGUser, Boolean, Exception>
    suspend fun getAllPost(): Flow<DataOrException<List<Post>, Boolean, Exception>>
}