package com.instagramclone.remote.repository

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.instagramclone.remote.models.IGUser
import com.instagramclone.util.models.DataOrException
import com.instagramclone.util.models.Post
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val queryUser: Query,
    private val dbPostRef: DatabaseReference
) : ProfileRepository {
    private val dbUser = FirebaseFirestore.getInstance().collection("Users")

    private val storageRefPost = FirebaseStorage.getInstance().reference.child("Posts")

    override suspend fun getUserData(currentUser: FirebaseUser?): DataOrException<IGUser, Boolean, Exception> {
        val dataOrException: DataOrException<IGUser, Boolean, Exception> = DataOrException()

        if (currentUser != null) {
            dbUser.document(currentUser.uid).get().addOnSuccessListener { docSnap ->
                dataOrException.isLoading = true
                dataOrException.data = docSnap.toObject(IGUser::class.java)
                dataOrException.isLoading = false
            }.addOnFailureListener {
                dataOrException.e = it
                dataOrException.isLoading = false
            }.await()
        }
        return dataOrException
    }

    override suspend fun getAllUsernames(): DataOrException<List<String>, Boolean, Exception> {
        val dataOrException: DataOrException<List<String>, Boolean, Exception> = DataOrException()

        dataOrException.isLoading = true
        try {
            dataOrException.data = queryUser.get().await().documents.map { docSnap ->
                docSnap.toObject(IGUser::class.java)?.username!!
            }
            dataOrException.isLoading = false
        } catch (e: FirebaseFirestoreException) {
            dataOrException.e = e
            dataOrException.isLoading = false
        }
        return dataOrException
    }

    override suspend fun updateUserDetails(
        key: String,
        value: String,
        currentUser: FirebaseUser?,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (currentUser != null) {
            FirebaseFirestore.getInstance().collection("Users").document(currentUser.uid)
                .update(key, value)
                .addOnSuccessListener {
                    onSuccess()
                }
                .addOnFailureListener {
                    onError(it.message.toString())
                }.await()
        }
    }

    override suspend fun convertToUrl(
        newImage: Uri,
        currentUser: FirebaseUser?,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        if (currentUser != null) {
            val storageRef = FirebaseStorage.getInstance().reference.child("ProfileImage")
                .child(currentUser.uid + ".jpg")

            storageRef.putFile(newImage)
                .addOnSuccessListener {
                    storageRef.downloadUrl
                        .addOnSuccessListener { downloadUrl ->
                            onSuccess(downloadUrl.toString())
                        }
                        .addOnFailureListener {
                            onError(it.message.toString())
                        }
                }
                .addOnFailureListener {
                    onError(it.message.toString())
                }.await()
        }
    }

    override suspend fun changePassword(
        password: String,
        currentUser: FirebaseUser?,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            currentUser?.updatePassword(password)
                ?.addOnSuccessListener {
                    onSuccess()
                }
                ?.addOnFailureListener {
                    onError(it.message.toString())
                }
                ?.await()
        } catch (e: FirebaseAuthException) {
            onError(e.message.toString())
        }
    }

    override fun logOut() {
        FirebaseAuth.getInstance().signOut()
    }

    override fun deletePost(
        post: Post,
        currentUser: FirebaseUser?,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (currentUser != null) {
            dbPostRef.child("${post.userId}-${post.timeStamp}")
                .removeValue()
                .addOnSuccessListener {
                    storageRefPost.child("Posts").child("${post.userId}-${post.timeStamp}")
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
        }
    }

    override suspend fun getMyPosts(currentUser: FirebaseUser?): Flow<DataOrException<List<Post>, Boolean, Exception>> {
        val dataOrException: MutableStateFlow<DataOrException<List<Post>, Boolean, Exception>> =
            MutableStateFlow(DataOrException(isLoading = true))

        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                dataOrException.update {
                    it.copy(
                        data = snapshot.children.map { dataSnap ->
                            dataSnap.getValue<Post>()!!
                        }
                            .filter { post -> post.userId == currentUser?.uid }
                            .sortedByDescending { post -> post.timeStamp }
                    )
                }

                dataOrException.update { it.copy(isLoading = false) }
            }

            override fun onCancelled(error: DatabaseError) {
                dataOrException.update {
                    it.copy(
                        e = error.toException(),
                        isLoading = false
                    )
                }
            }
        }

        dbPostRef.addValueEventListener(valueEventListener)

        return dataOrException
    }

    override suspend fun getUserProfile(userId: String): DataOrException<IGUser, Boolean, Exception> {
        val dataOrException: DataOrException<IGUser, Boolean, Exception> = DataOrException()

        dataOrException.isLoading = true
        queryUser.get()
            .addOnSuccessListener { querySnap ->
                dataOrException.data = querySnap.documents.map { docSnap ->
                    docSnap.toObject(IGUser::class.java)
                }
                    .first { it?.userId == userId }
                dataOrException.isLoading = false
            }
            .addOnFailureListener {
                dataOrException.e = it
                dataOrException.isLoading = false
            }
            .await()
            .asFlow()

        return dataOrException
    }

    override suspend fun getUserPosts(userId: String): Flow<DataOrException<List<Post>, Boolean, Exception>> {
        val dataOrException: MutableStateFlow<DataOrException<List<Post>, Boolean, Exception>> =
            MutableStateFlow(DataOrException())

        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                dataOrException.update {
                    it.copy(
                        data = snapshot.children.map { dataSnap ->
                            dataSnap.getValue<Post>()!!
                        }
                            .filter { post -> post.userId == userId }
                            .sortedByDescending { post -> post.timeStamp },
                        isLoading = false
                    )
                }
            }

            override fun onCancelled(error: DatabaseError) {
                dataOrException.update {
                    it.copy(
                        e = error.toException(),
                        isLoading = false
                    )
                }
            }
        }

        dbPostRef.addValueEventListener(valueEventListener)

        return dataOrException
    }

    override suspend fun follow(
        userId: String,
        currentUser: FirebaseUser?,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        dbUser.document(userId)
            .update("followersList", FieldValue.arrayUnion(currentUser?.uid))
            .addOnSuccessListener {
                dbUser.document(currentUser?.uid!!)
                    .update("followingList", FieldValue.arrayUnion(userId))
                    .addOnSuccessListener {
                        onSuccess()
                    }
                    .addOnFailureListener {
                        onError(it.message.toString())
                    }
            }
            .addOnFailureListener {
                onError(it.message.toString())
            }
            .await()
    }

    override suspend fun unFollow(
        userId: String,
        currentUser: FirebaseUser?,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        dbUser.document(userId)
            .update("followersList", FieldValue.arrayRemove(currentUser?.uid))
            .addOnSuccessListener {
                dbUser.document(currentUser?.uid!!)
                    .update("followingList", FieldValue.arrayRemove(userId))
                    .addOnSuccessListener {
                        onSuccess()
                    }
                    .addOnFailureListener {
                        onError(it.message.toString())
                    }
            }
            .addOnFailureListener {
                onError(it.message.toString())
            }
            .await()
    }

    override suspend fun like(
        userId: String,
        timeStamp: Long,
        currentUser: FirebaseUser?,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (currentUser != null) {
            val likes = mutableListOf<String>()
            val ref = dbPostRef.child("${userId}-${timeStamp}")

            ref.get()
                .addOnSuccessListener {
                    val post = it.getValue<Post>()

                    post?.likes?.forEach { userId ->
                        likes.add(userId)
                    }
                    if (!likes.contains(currentUser.uid)) {
                        likes.add(currentUser.uid)
                        ref.updateChildren(mapOf("likes" to likes))
                            .addOnSuccessListener {
                                onSuccess()
                            }
                            .addOnFailureListener { error ->
                                onError(error.message.toString())
                            }
                    }
                }
                .addOnFailureListener { error ->
                    onError(error.message.toString())
                }
                .await()
        }
    }

    override suspend fun unLike(
        userId: String,
        timeStamp: Long,
        currentUser: FirebaseUser?,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (currentUser != null) {
            val likes = mutableListOf<String>()
            val ref = dbPostRef.child("$userId-$timeStamp")

            ref.get()
                .addOnSuccessListener {
                    val post = it.getValue<Post>()

                    post?.likes?.forEach { userId ->
                        likes.add(userId)
                    }

                    likes.remove(currentUser.uid)
                    ref.updateChildren(mapOf("likes" to likes))
                        .addOnSuccessListener {
                            onSuccess()
                        }
                        .addOnFailureListener { error ->
                            onError(error.message.toString())
                        }
                }
                .await()
        }
    }
}
