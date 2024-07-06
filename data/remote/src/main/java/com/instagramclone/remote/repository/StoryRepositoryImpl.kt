package com.instagramclone.remote.repository

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.getValue
import com.google.firebase.storage.StorageReference
import com.instagramclone.util.models.Story
import kotlinx.coroutines.tasks.await

class StoryRepositoryImpl(
    private val dbStory: DatabaseReference,
    private val storageRefStory: StorageReference
) : StoryRepository {

    override suspend fun deleteStory(
        story: Story,
        currentUserId: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val storyRef = dbStory.child(currentUserId).child("stories")

        val stories = mutableListOf<Story?>()

        storyRef.get()
            .addOnSuccessListener { dataSnap ->
                dataSnap.children.mapNotNull { snap ->
                    val mStory = snap.getValue<Story>()

                    stories.add(mStory) // Retrieve all stories
                }

                if (stories.size == 1) {
                    dbStory.child(currentUserId).removeValue() // If only 1 story left remove whole doc
                } else {
                    stories.remove(story) // Else remove selected story

                    dbStory.child(currentUserId)
                        .updateChildren(mapOf("stories" to stories)) // Update all other stories back
                }
                storageRefStory.child(
                    "${currentUserId}-${story.timeStamp}.${story.mimeType.substringAfter("/")}"
                ) // Remove Stored image from storage
                    .delete()
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
            .await()
    }

    override suspend fun updateStoryViews(
        story: Story,
        currentUserId: String,
        onError: (String) -> Unit
    ) {
        if (story.userId != currentUserId && !story.views.contains(currentUserId)) {
            val storyRef = dbStory.child(story.userId).child("stories")

            var views: MutableList<String>

            storyRef.get()
                .addOnSuccessListener { dataSnap ->
                    for (snapChild in dataSnap.children) {
                        val mStory = snapChild.getValue<Story>()

                        if (story.timeStamp == mStory?.timeStamp) {
                            storyRef.child(snapChild.key!!).get()
                                .addOnSuccessListener { snap ->
                                    views = snap.getValue<Story>()?.views?.toMutableList()
                                        ?: mutableListOf() // Retrieve existing views
                                    views.add(currentUserId) // Add current user to view

                                    storyRef.child(snapChild.key!!)
                                        .updateChildren(mapOf("views" to views)) // Update views
                                }
                                .addOnFailureListener { error ->
                                    onError(error.message.toString())
                                }
                        }

                    }
                }
                .addOnFailureListener { error ->
                    onError(error.message.toString())
                }
                .await()
        }
    }
}