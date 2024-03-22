package com.instagramclone.firebase.repository

import androidx.core.net.toUri
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.instagramclone.util.models.Post
import kotlinx.coroutines.tasks.await

class UploadContentRepositoryImpl : UploadContentRepository {
    private val dbPost = FirebaseDatabase.getInstance().getReference("Posts")
    private val storageRefPost = FirebaseStorage.getInstance().reference.child("Posts")

    override suspend fun uploadPost(
        post: Post,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            storageRefPost.child("${post.userId}-${post.timeStamp}.jpg")
                .putFile(post.images.first().toUri())
                .addOnSuccessListener { taskSnap ->
                    taskSnap.storage.downloadUrl
                        .addOnSuccessListener { downloadUrl ->
                            dbPost.child("${post.userId}-${post.timeStamp}")
                                .setValue(
                                    post.apply {
                                        images = listOf(downloadUrl.toString())
                                    }
                                )
                                .addOnSuccessListener {
                                    onSuccess()
                                }
                                .addOnFailureListener { error ->
                                    onError(error.message.toString())
                                }
                        }
                        .addOnFailureListener { error ->
                            onError(error.message.toString())
                        }
                }
                .await()
        } catch (e: Exception) {
            onError(e.message.toString())
        }
    }
}