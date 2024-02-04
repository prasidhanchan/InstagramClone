package com.instagramclone.firebase.repository

import com.instagramclone.firebase.models.IGUser
import com.instagramclone.util.models.DataOrException

interface HomeRepository {
    suspend fun getUserData(): DataOrException<IGUser, Boolean, Exception>
}