package com.instagramclone.remote.repository

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.storage.FirebaseStorage
import com.instagramclone.remote.models.IGUser
import com.instagramclone.util.models.DataOrException
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl : AuthRepository {

    private val dbUser = FirebaseFirestore.getInstance().collection("Users")
    override fun signInUser(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                onSuccess()
            }.addOnFailureListener {
                onError(it.message.toString())
            }
    }

    override fun loginUser(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                onSuccess()
            }.addOnFailureListener {
                onError(it.message.toString())
            }
    }

    override suspend fun loginUserWithUsername(
        username: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        dbUser.get().addOnSuccessListener { querySnap ->
            val usersList = querySnap.documents.map { docSnap ->
                docSnap.toObject(IGUser::class.java)
            }
            val user = usersList.filter { it?.username == username }

            if (user.isNotEmpty()) {
                if (password == user.first()?.password) {
                    loginUser(
                        email = user.first()?.email!!,
                        password = password,
                        onSuccess = onSuccess,
                        onError = onError
                    )
                } else if (user.first()?.password == "Facebook Login") {
                    onError("Password not set, please login through facebook.")
                } else {
                    onError("Password Incorrect")
                }
            } else {
                onError("Username not found")
            }
        }.addOnFailureListener {
            onError(it.message.toString())
        }
            .await()
            .asFlow()
    }

    override fun sendPasswordResetEmail(
        email: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it.message.toString()) }
    }

    override fun addUserToDB(
        igUser: IGUser,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            dbUser.document(currentUser.uid).set(igUser).addOnSuccessListener {
                onSuccess()
            }.addOnFailureListener {
                onError(it.message.toString())
            }
        }
    }

    override suspend fun convertToUrl(uri: Uri?): DataOrException<Uri?, Boolean, Exception> {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val dataOrException: DataOrException<Uri?, Boolean, Exception> = DataOrException()
        if (currentUser != null) {
            dataOrException.isLoading = true
            val storageRef = FirebaseStorage.getInstance().reference
                .child("ProfileImage")
                .child(currentUser.uid + ".jpg")

            storageRef.putFile(uri!!).addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener {
                    dataOrException.data = it
                    dataOrException.isLoading = false
                }.addOnFailureListener {
                    dataOrException.e = it
                    dataOrException.isLoading = false
                }
            }.await()
        }
        return dataOrException
    }

    override fun updateProfileImage(
        profileImage: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            dbUser.document(currentUser.uid)
                .update(mapOf("profileImage" to profileImage))
                .addOnSuccessListener {
                    onSuccess()
                }.addOnFailureListener {
                    onError(it.message.toString())
                }
        }
    }

    override suspend fun getAllEmails(): DataOrException<List<String>, Boolean, Exception> {
        val dataOrException: DataOrException<List<String>, Boolean, Exception> = DataOrException()
        try {
            dataOrException.isLoading = true
            dbUser.get().addOnSuccessListener { documents ->
                dataOrException.data = documents.documents.map { it.data?.get("email").toString() }
            }
                .await()
                .asFlow()
            dataOrException.isLoading = false
        } catch (e: FirebaseFirestoreException) {
            dataOrException.e = e
        }
        return dataOrException
    }

    override suspend fun getAllUsernames(): DataOrException<List<String>, Boolean, Exception> {
        val dataOrException: DataOrException<List<String>, Boolean, Exception> = DataOrException()
        dataOrException.isLoading = true
        dbUser.get().addOnSuccessListener { querySnap ->
            dataOrException.data = querySnap.documents.map { it.data?.get("username").toString() }
        }.addOnFailureListener {
            dataOrException.e = it
        }
            .await()
            .asFlow()
        dataOrException.isLoading = false
        return dataOrException
    }

    override suspend fun getAllUsers(): DataOrException<List<IGUser>, Boolean, Exception> {
        val dataOrException: DataOrException<List<IGUser>, Boolean, Exception> = DataOrException()
        dataOrException.isLoading = true
        dbUser.get().addOnSuccessListener { querySnap ->
            dataOrException.data = querySnap.documents.map { docSnap ->
                docSnap.toObject(IGUser::class.java)!!
            }
            dataOrException.isLoading = false
        }.addOnFailureListener {
            dataOrException.e = it
            dataOrException.isLoading = false
        }
            .await()
            .asFlow()
        return dataOrException
    }
}