package com.instagramclone.remote.repository

import androidx.core.net.toUri
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.getValue
import com.google.firebase.storage.FirebaseStorage
import com.instagramclone.util.models.Post
import com.instagramclone.util.models.UserStory
import kotlinx.coroutines.tasks.await

class UploadContentRepositoryImpl : UploadContentRepository {
    private val dbPost = FirebaseDatabase.getInstance().getReference("Posts")
    private val storageRefPost = FirebaseStorage.getInstance().reference.child("Posts")
    private val dbStory = FirebaseDatabase.getInstance().getReference("Stories")
    private val storageRefStory = FirebaseStorage.getInstance().reference.child("Stories")

    override suspend fun uploadPost(
        post: Post,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            storageRefPost
                .child("${post.userId}-${post.timeStamp}.${post.mimeType.substringAfter("/")}") // image.jpeg
                .putFile(post.mediaList.first().toUri())
                .addOnSuccessListener { taskSnap ->
                    taskSnap.storage.downloadUrl
                        .addOnSuccessListener { downloadUrl ->
                            dbPost.child("${post.userId}-${post.timeStamp}")
                                .setValue(
                                    post.apply {
                                        mediaList = listOf(downloadUrl.toString())
                                    }
                                )
                                .addOnSuccessListener {
                                    onSuccess()
                                }
                                .addOnFailureListener { error ->
                                    throw error
                                }
                        }
                        .addOnFailureListener { error ->
                            throw error
                        }
                }
                .addOnFailureListener { error ->
                    throw error
                }
                .await()
        } catch (e: Exception) {
            onError(e.message.toString())
        }
    }

    override suspend fun uploadStory(
        userStory: UserStory,
        currentUserId: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            dbStory.child(currentUserId).get()
                .addOnSuccessListener { dataSnap ->
                    val myStories = dataSnap.getValue<UserStory>() // Current user stories

                    storageRefStory
                        .child(
                            "${userStory.userId}-${userStory.stories.first().timeStamp}" + // userId-timeStamp.jpeg
                                    ".${userStory.stories.first().mimeType.substringAfter("/")}" // mimeType = "/jpeg" or "/mp4"
                        )
                        .putFile(userStory.stories.first().image.toUri())
                        .addOnSuccessListener { taskSnap ->
                            taskSnap.storage.downloadUrl
                                .addOnSuccessListener { downloadUrl ->
                                    dbStory.child(userStory.userId)
                                        .updateChildren(
                                            if (myStories != null) {
                                                userStory.apply {
                                                    stories =
                                                        myStories.stories.plus( // Previous stories
                                                            userStory.stories.first() // New story
                                                                .apply {
                                                                    image = downloadUrl.toString()
                                                                }
                                                        )
                                                }
                                                    .convertToMap()
                                            } else {
                                                userStory.apply {
                                                    stories.first().apply {
                                                        image = downloadUrl.toString()
                                                    } // no previous story so add the new one
                                                }
                                                    .convertToMap()
                                            }
                                        )
                                        .addOnSuccessListener {
                                            onSuccess()
                                        }
                                        .addOnFailureListener { error ->
                                            throw error
                                        }
                                }
                                .addOnFailureListener { error ->
                                    throw error
                                }
                        }
                        .addOnFailureListener { error ->
                            throw error
                        }
                }
                .addOnFailureListener { error ->
                    throw error
                }
                .await()
        } catch (e: Exception) {
            onError(e.message.toString())
        }
    }
}