package com.instagramclone.firebase.repository

import com.instagramclone.firebase.models.IGUser
import com.instagramclone.util.models.DataOrException
import com.instagramclone.util.models.Post

interface HomeRepository {
    suspend fun getUserData(): DataOrException<IGUser, Boolean, Exception>
    suspend fun getAllPost(): DataOrException<List<Post>, Boolean, Exception>
}