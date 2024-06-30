package com.instagramclone.remote.repository

import android.net.Uri
import com.google.firebase.auth.FirebaseUser
import com.instagramclone.remote.models.IGUser
import com.instagramclone.util.models.DataOrException
import com.instagramclone.util.models.Post
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    suspend fun getUserData(currentUser: FirebaseUser?): DataOrException<IGUser, Boolean, Exception>
    suspend fun getAllUsernames(): DataOrException<List<String>, Boolean, Exception>
    suspend fun updateUserDetails(key: String, value: String, currentUser: FirebaseUser?, onSuccess: () -> Unit, onError: (String) -> Unit)
    suspend fun convertToUrl(newImage: Uri, currentUser: FirebaseUser?, onSuccess: (String) -> Unit, onError: (String) -> Unit)
    suspend fun changePassword(password: String, currentUser: FirebaseUser?, onSuccess: () -> Unit, onError: (String) -> Unit)
    fun logOut()
    suspend fun getMyPosts(currentUser: FirebaseUser?): Flow<DataOrException<List<Post>, Boolean, Exception>>
    fun deletePost(post: Post, currentUser: FirebaseUser?, onSuccess: () -> Unit, onError: (String) -> Unit)
    suspend fun getUserProfile(userId: String): DataOrException<IGUser, Boolean, Exception>
    suspend fun getUserPosts(userId: String): Flow<DataOrException<List<Post>, Boolean, Exception>>
    suspend fun follow(userId: String, currentUser: FirebaseUser?, onSuccess: () -> Unit, onError: (String) -> Unit)
    suspend fun unFollow(userId: String, currentUser: FirebaseUser?, onSuccess: () -> Unit, onError: (String) -> Unit)
    suspend fun like(userId: String, timeStamp: Long, currentUser: FirebaseUser?, onSuccess: () -> Unit, onError: (String) -> Unit)
    suspend fun unLike(userId: String, timeStamp: Long, currentUser: FirebaseUser?, onSuccess: () -> Unit, onError: (String) -> Unit)
}