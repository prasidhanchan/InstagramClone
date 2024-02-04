package com.instagramclone.firebase.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.instagramclone.firebase.models.IGUser
import com.instagramclone.util.models.DataOrException
import kotlinx.coroutines.tasks.await

class HomeRepositoryImpl : HomeRepository {

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
}