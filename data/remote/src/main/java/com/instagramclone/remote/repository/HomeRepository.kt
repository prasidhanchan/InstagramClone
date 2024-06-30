package com.instagramclone.remote.repository

import com.google.firebase.auth.FirebaseUser
import com.instagramclone.remote.models.IGUser
import com.instagramclone.util.models.DataOrException
import com.instagramclone.util.models.Post
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    suspend fun getUserData(currentUser: FirebaseUser?): DataOrException<IGUser, Boolean, Exception>
    suspend fun getAllPost(): Flow<DataOrException<List<Post>, Boolean, Exception>>
}