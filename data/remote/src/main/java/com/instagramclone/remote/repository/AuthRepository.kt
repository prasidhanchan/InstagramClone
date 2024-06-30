package com.instagramclone.remote.repository

import android.net.Uri
import com.instagramclone.remote.models.IGUser
import com.instagramclone.util.models.DataOrException

interface AuthRepository {
    fun signInUser(email: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit)
    fun loginUser(email: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit)
    suspend fun loginUserWithUsername(username: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit)
    fun sendPasswordResetEmail(email: String, onSuccess: () -> Unit, onError: (String) -> Unit)
    fun addUserToDB(igUser: IGUser, onSuccess: () -> Unit, onError: (String) -> Unit)
    suspend fun convertToUrl(uri: Uri?): DataOrException<Uri?, Boolean, Exception>
    fun updateProfileImage(profileImage: String, onSuccess: () -> Unit, onError: (String) -> Unit)
    suspend fun getAllEmails(): DataOrException<List<String>, Boolean, Exception>
    suspend fun getAllUsernames(): DataOrException<List<String>, Boolean, Exception>
    suspend fun getAllUsers(): DataOrException<List<IGUser>, Boolean, Exception>
}