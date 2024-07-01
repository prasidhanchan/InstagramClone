package com.instagramclone.remote.repository

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.firestore.FirebaseFirestore
import com.instagramclone.remote.models.IGUser
import com.instagramclone.util.models.DataOrException
import com.instagramclone.util.models.Post
import com.instagramclone.util.models.UserStory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val dbPosts: Query,
    private val dbStories: Query,
) : HomeRepository {

    private val dbUser = FirebaseFirestore.getInstance().collection("Users")

    override suspend fun getUserData(currentUser: FirebaseUser?): DataOrException<IGUser, Boolean, Exception> {
        val dataOrException: DataOrException<IGUser, Boolean, Exception> = DataOrException()

        if (currentUser != null) {
            dataOrException.isLoading = true
            dbUser.document(currentUser.uid).get().addOnSuccessListener { docSnap ->
                dataOrException.data = docSnap.toObject(IGUser::class.java)
                dataOrException.isLoading = false
            }
                .addOnFailureListener {
                    dataOrException.e = it
                    dataOrException.isLoading = false
                }.await()
        }
        return dataOrException
    }

    override suspend fun getPosts(
        following: List<String>,
        currentUserId: String,
    ): Flow<DataOrException<List<Post>, Boolean, Exception>> {
        val dataOrException: MutableStateFlow<DataOrException<List<Post>, Boolean, Exception>> =
            MutableStateFlow(DataOrException(isLoading = true))

        try {
            val valueEventListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    dataOrException.update {
                        it.copy(
                            data = snapshot.children.map { dataSnap ->
                                dataSnap.getValue<Post>()!!
                            }
                                .filter { post -> following.contains(post.userId) || post.userId == currentUserId }
                                .sortedByDescending { post -> post.timeStamp }
                        )
                    }

                    dataOrException.update { it.copy(isLoading = false) }
                }

                override fun onCancelled(error: DatabaseError) {
                    throw error.toException()
                }
            }

            dbPosts.addValueEventListener(valueEventListener)
        } catch (e: Exception) {
            dataOrException.update {
                it.copy(
                    e = e,
                    isLoading = false
                )
            }
        }

        return dataOrException.asStateFlow()
    }

    override suspend fun getStories(
        following: List<String>,
        currentUserId: String
    ): Flow<DataOrException<List<UserStory>, Boolean, Exception>> {
        val dataOrException: MutableStateFlow<DataOrException<List<UserStory>, Boolean, Exception>> =
            MutableStateFlow(DataOrException(isLoading = true))

        try {
            val valueEventListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    dataOrException.update {
                        it.copy(
                            data = snapshot.children.map { dataSnap ->
                                dataSnap.getValue<UserStory>()!!
                            }
                                .filter { story -> following.contains(story.userId) || story.userId == currentUserId }
                                .sortedByDescending { userStory ->
                                    userStory.stories.minByOrNull { story ->
                                        story.timeStamp // Arranging stories in ascending, i.e old stories first
                                    }?.timeStamp // Arranging single user stories
                                }
                        )
                    }

                    dataOrException.update { it.copy(isLoading = false) }
                }

                override fun onCancelled(error: DatabaseError) {
                    throw error.toException()
                }
            }

            dbStories.addValueEventListener(valueEventListener)
        } catch (e: Exception) {
            dataOrException.update {
                it.copy(
                    e = e,
                    isLoading = false
                )
            }
        }

        return dataOrException
    }
}