package com.instagramclone.firebase.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.instagramclone.firebase.models.IGUser
import com.instagramclone.util.models.DataOrException
import com.instagramclone.util.models.Post
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val dbPosts: Query
) : HomeRepository {

    private val dbUser = FirebaseFirestore.getInstance().collection("Users")
    private val currentUser = FirebaseAuth.getInstance().currentUser
    override suspend fun getUserData(): DataOrException<IGUser, Boolean, Exception> {
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

    override suspend fun getAllPost(): DataOrException<List<Post>, Boolean, Exception> {
        val dataOrException: DataOrException<List<Post>, Boolean, Exception> = DataOrException()

        if (currentUser != null) {
            try {
                dataOrException.isLoading = true
                dbPosts.get()
                    .addOnSuccessListener { querySnap ->
                        dataOrException.data = querySnap.documents.map { docSnap ->
                            docSnap.toObject(Post::class.java)!!
                        }
                        dataOrException.isLoading = false
                    }
                    .addOnFailureListener {
                        dataOrException.e = it
                        dataOrException.isLoading = false
                    }
                    .await()
                    .asFlow()
                dataOrException.isLoading = false
            } catch (e: Exception) {
                dataOrException.e = e
                dataOrException.isLoading = false
            }
        }
        return dataOrException
    }
}