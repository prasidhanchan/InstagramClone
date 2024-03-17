package com.instagramclone.firebase.repository

import androidx.core.net.toUri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.instagramclone.util.models.Post

class UploadContentRepositoryImpl : UploadContentRepository {
    private val dbPost = FirebaseFirestore.getInstance().collection("Posts")
    private val currentUser = FirebaseAuth.getInstance().currentUser
    override suspend fun uploadPost(
        post: Post,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            val storageRef = FirebaseStorage.getInstance().reference
                .child("Posts")
                .child("${currentUser?.uid}-${post.timeStamp}.jpg")

            storageRef.putFile(post.images.first().toUri())
                .addOnSuccessListener {
//                    val downloadUrl = it.storage.downloadUrl
                    storageRef.downloadUrl
                        .addOnSuccessListener { downloadUrl ->
                            dbPost.document("${currentUser?.uid}-${post.timeStamp}")
                                .set(
                                    post.apply {
                                        images = listOf(downloadUrl.toString())
                                    }
                                ).addOnSuccessListener {
                                    onSuccess()
                                }
                                .addOnFailureListener {
                                    onError(it.message.toString())
                                }
                        }.addOnFailureListener {
                            onError(it.message.toString())
                        }
                }
        } catch (e: Exception) {
            onError(e.message.toString())
        }
    }
}