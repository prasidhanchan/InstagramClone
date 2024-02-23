package com.instagramclone.firebase.repository

import android.net.Uri
import com.instagramclone.firebase.models.IGUser
import com.instagramclone.util.models.DataOrException

interface ProfileRepository {
    suspend fun getUserData(): DataOrException<IGUser, Boolean, Exception>
    suspend fun getAllUsernames(): DataOrException<List<String>, Boolean, Exception>
    suspend fun updateUserDetails(key: String, value: String,onSuccess: () -> Unit, onError: (String) -> Unit)
    suspend fun convertToUrl(newImage: Uri, onSuccess: (String) -> Unit, onError: (String) -> Unit)
    suspend fun changePassword(password: String, onSuccess: () -> Unit, onError: (String) -> Unit)
    fun logOut()
}