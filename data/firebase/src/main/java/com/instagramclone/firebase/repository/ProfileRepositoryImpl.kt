package com.instagramclone.firebase.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.instagramclone.firebase.models.IGUser
import com.instagramclone.util.models.DataOrException
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val query: Query
): ProfileRepository {
    private val currentUser = FirebaseAuth.getInstance().currentUser
    private val dbUser = FirebaseFirestore.getInstance().collection("Users")
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
            dataOrException.data = query.get().await().documents.map { docSnap ->
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
}