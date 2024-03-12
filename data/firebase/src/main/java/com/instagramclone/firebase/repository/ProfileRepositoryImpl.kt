package com.instagramclone.firebase.repository

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.instagramclone.firebase.models.IGUser
import com.instagramclone.util.models.DataOrException
import com.instagramclone.util.models.Post
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val queryUser: Query,
    private val queryPost: Query
) : ProfileRepository {
    private val currentUser = FirebaseAuth.getInstance().currentUser
    private val dbUser = FirebaseFirestore.getInstance().collection("Users")
    private val dbPost = FirebaseFirestore.getInstance().collection("Posts")

    private val storageRefPost = FirebaseStorage.getInstance().reference.child("Posts")

    override suspend fun getUserData(): DataOrException<IGUser, Boolean, Exception> {
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
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        dbPost.document("${currentUser?.uid}-${post.timeStamp}").delete()
            .addOnSuccessListener {
                storageRefPost.child("${currentUser?.uid}-${post.timeStamp}.jpg").delete()
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
    }

    override suspend fun getMyPosts(): DataOrException<List<Post>, Boolean, Exception> {
        val dataOrException: DataOrException<List<Post>, Boolean, Exception> = DataOrException()

        try {
            dataOrException.isLoading = true
            queryPost.get()
                .addOnSuccessListener { querySnap ->
                    dataOrException.data = querySnap.documents.map { docSnap ->
                        docSnap.toObject(Post::class.java)!!
                    }
                        .filter { it.userId == currentUser?.uid }
                    dataOrException.isLoading = false
                }
                .addOnFailureListener {
                    dataOrException.e = it
                    dataOrException.isLoading = false
                }
                .await()
                .asFlow()
        } catch (e: Exception) {
            dataOrException.e = e
        }
        return dataOrException
    }

    override suspend fun getUserProfile(userId: String): DataOrException<IGUser, Boolean, Exception> {
        val dataOrException: DataOrException<IGUser, Boolean, Exception> = DataOrException()

        try {
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

        } catch (e: Exception) {
            dataOrException.e = e
            dataOrException.isLoading = false
        }

        return dataOrException
    }

    override suspend fun getUserPosts(userId: String): DataOrException<List<Post>, Boolean, Exception> {
        val dataOrException: DataOrException<List<Post>, Boolean, Exception> = DataOrException()

        try {
            dataOrException.isLoading = true
            queryPost.get()
                .addOnSuccessListener { querySnap ->
                    dataOrException.data = querySnap.documents.map { docSnap ->
                        docSnap.toObject(Post::class.java)!!
                    }
                        .filter { it.userId == userId }
                    dataOrException.isLoading = false
                }
                .addOnFailureListener {
                    dataOrException.e = it
                    dataOrException.isLoading = false
                }
        } catch (e: Exception) {
            dataOrException.e = e
            dataOrException.isLoading = false
        }

        return dataOrException
    }

    override suspend fun follow(
        userId: String,
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
}