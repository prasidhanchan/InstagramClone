package com.instagramclone.firebase.repository

import android.net.Uri
import com.instagramclone.firebase.models.IGUser
import com.instagramclone.util.models.DataOrException
import com.instagramclone.util.models.Post

interface ProfileRepository {
    suspend fun getUserData(): DataOrException<IGUser, Boolean, Exception>
    suspend fun getAllUsernames(): DataOrException<List<String>, Boolean, Exception>
    suspend fun updateUserDetails(key: String, value: String,onSuccess: () -> Unit, onError: (String) -> Unit)
    suspend fun convertToUrl(newImage: Uri, onSuccess: (String) -> Unit, onError: (String) -> Unit)
    suspend fun changePassword(password: String, onSuccess: () -> Unit, onError: (String) -> Unit)
    fun logOut()
    suspend fun getMyPosts(): DataOrException<List<Post>, Boolean, Exception>
    fun deletePost(post: Post, onSuccess: () -> Unit, onError: (String) -> Unit)
    suspend fun getUserProfile(userId: String): DataOrException<IGUser, Boolean, Exception>
    suspend fun getUserPosts(userId: String): DataOrException<List<Post>, Boolean, Exception>
    suspend fun follow(userId: String, onSuccess: () -> Unit, onError: (String) -> Unit)
    suspend fun unFollow(userId: String, onSuccess: () -> Unit, onError: (String) -> Unit)
    suspend fun like(userId: String, timeStamp: Long, onSuccess: () -> Unit, onError: (String) -> Unit)
    suspend fun unLike(userId: String, timeStamp: Long, onSuccess: () -> Unit, onError: (String) -> Unit)
}