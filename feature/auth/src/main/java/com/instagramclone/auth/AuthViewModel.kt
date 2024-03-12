package com.instagramclone.auth

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.instagramclone.firebase.models.IGUser
import com.instagramclone.firebase.repository.AuthRepositoryImpl
import com.instagramclone.ui.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepositoryImpl
) : ViewModel() {

    var uiState = MutableStateFlow(UiState())
        private set

    private var _emailList = MutableStateFlow<List<String>?>(null)
    var emailList = _emailList.asStateFlow()


    private val _usernameList = MutableStateFlow<List<String>?>(null)
    val usernameList = _usernameList.asStateFlow()

    private val _users = MutableStateFlow<List<IGUser>?>(null)

    init {
        getAllEmails()
        getAllUsernames()
    }

    /** Function to signUp to Firebase
     * @param email requires User email
     * @param password requires User password
     * @param onSuccess on Successful lambda triggered on signup
     * */
    fun signInUser(
        email: String,
        password: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                uiState.update { it.copy(showDialog = true) }
            }
            authRepository.signInUser(
                email = email,
                password = password,
                onSuccess = {
                    uiState.update { it.copy(showDialog = false) }
                    onSuccess()
                },
                onError = { error ->
                    uiState.update {
                        it.copy(
                            errorOrSuccess = error,
                            showDialog = false
                        )
                    }
                }
            )
        }
    }

    /** Function to login to Firebase
     * @param email requires User email
     * @param password requires User password
     * @param onSuccess on Successful lambda triggered on login
     * */
    fun loginUser(
        email: String,
        password: String,
        context: Context,
        onSuccess: () -> Unit
    ) {
        uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.loginUser(
                email = email,
                password = password,
                onSuccess = {
                    uiState.update { it.copy(isLoading = false) }
                    onSuccess()
                },
                onError = { error ->
                    if (error.contains(context.getString(R.string.credential_expired))) {
                        uiState.update {
                            it.copy(
                                showDialog = true,
                                isLoading = false,
                                errorTitle = context.getString(R.string.incorrect_username_or_password),
                                errorSubTitle = context.getString(R.string.incorrect_username_or_password_message)
                            )
                        }
                    }
                }
            )
        }
    }

    /**
     * Function to login a user using username and password
     * @param username Requires username of the User
     * @param password Requires password of the User
     * @param onSuccess on success lambda triggered when the login is successful
     */
    fun loginWithUsername(
        username: String,
        password: String,
        context: Context,
        onSuccess: () -> Unit
    ) {
        uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.loginUserWithUsername(
                username = username,
                password = password,
                onSuccess = {
                    onSuccess()
                    uiState.update { it.copy(isLoading = false) }
                },
                onError = { error ->
                    uiState.update {
                        when (error) {
                            "Password Incorrect" -> {
                                it.copy(
                                    isLoading = false,
                                    errorTitle = context.getString(R.string.incorrect_password),
                                    errorSubTitle = context.getString(R.string.incorrect_passowrd_message),
                                    showDialog = true
                                )
                            }

                            "Username not found" -> {
                                it.copy(
                                    isLoading = false,
                                    errorTitle = context.getString(R.string.incorrect_username_or_password),
                                    errorSubTitle = context.getString(R.string.incorrect_username_or_password_message),
                                    showDialog = true,
                                    errorOrSuccess = error
                                )
                            }

                            else -> {
                                it.copy(
                                    isLoading = false,
                                    errorOrSuccess = error
                                )
                            }
                        }
                    }
                }
            )
        }
    }

    fun sendPasswordResetEmail(email: String) {
        uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.sendPasswordResetEmail(
                email = email,
                onSuccess = {
                    uiState.update {
                        it.copy(
                            isLoading = false,
                            showDialog = true
                        )
                    }
                },
                onError = { error ->
                    uiState.update {
                        it.copy(
                            errorOrSuccess = error,
                            isLoading = false
                        )
                    }
                }
            )
        }
    }

    /** Function to log out of Firebase Account */
    fun logOut() = authRepository.logOut()


    /** Function to add user data to Firestore
     * @param igUser requires [IGUser] model class
     * @param onSuccess on Success lambda triggered on successful insertion of data
     */
    fun addUserToDB(
        igUser: IGUser,
        onSuccess: () -> Unit
    ) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                uiState.update { it.copy(showDialog = true) }
            }
            delay(1500L)
            authRepository.addUserToDB(
                igUser = igUser.copy(userId = currentUser?.uid ?: ""),
                onSuccess = {
                    uiState.update { it.copy(showDialog = false) }
                    onSuccess()
                },
                onError = { error ->
                    uiState.update {
                        it.copy(
                            errorOrSuccess = error,
                            showDialog = false
                        )
                    }
                }
            )
        }
    }

    /** Function to convert Gallery image Uri to url using Firebase Storage
     * @param uri Requires the uri of an image from gallery
     * @param onSuccess on Success lambda triggered when download url is retrieved from Firebase Storage
     */
    fun convertToUrl(
        uri: Uri?,
        onSuccess: () -> Unit
    ) {
        uiState.update { it.copy(showDialog = true) }
        viewModelScope.launch(Dispatchers.IO) {
            val result = authRepository.convertToUrl(uri = uri)
            delay(1000L)
            withContext(Dispatchers.Main) {
                if (result.data != null && result.e == null) {
                    uiState.update {
                        it.copy(
                            profileImage = result.data
                        )
                    }
                    onSuccess()
                    uiState.update { it.copy(showDialog = false) }
                } else {
                    uiState.update {
                        it.copy(
                            errorOrSuccess = result.e?.message.toString(),
                            showDialog = false
                        )
                    }
                }
            }
        }
    }

    /** Function to update user Profile image in Firestore, gets profile image from ViewModel uiState
     * @param onSuccess on Success lambda triggered when image is updated successfully
     */
    fun updateProfileImage(
        onSuccess: () -> Unit
    ) {
        uiState.update { it.copy(showDialog = true) }
        viewModelScope.launch(Dispatchers.IO) {
            delay(1000L)
            authRepository.updateProfileImage(
                profileImage = uiState.value.profileImage.toString(),
                onSuccess = {
                    uiState.update { it.copy(showDialog = false) }
                    onSuccess()
                },
                onError = { error ->
                    uiState.update {
                        it.copy(
                            errorOrSuccess = error,
                            showDialog = false
                        )
                    }
                }
            )
        }
    }

    /** Function to get all the emails from Firebase firestore */
    private fun getAllEmails() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = authRepository.getAllEmails()
            delay(500L)
            if (!result.isLoading!!) _emailList.update { result.data }
        }
    }

    private fun getAllUsernames() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = authRepository.getAllUsernames()
            delay(500L)
            if (!result.isLoading!!) _usernameList.update { result.data }
        }
    }

    /** Function to get all users from Firebase Firestore */
    fun getAllUsers() {
        uiState.update { it.copy(showDialog = true) }
        viewModelScope.launch(Dispatchers.IO) {
            val result = authRepository.getAllUsers()

            delay(1000L)
            withContext(Dispatchers.Main) {
                if (result.data != null && result.e == null) {
                    _users.update { result.data }
                    uiState.update { it.copy(showDialog = false) }
                } else {
                    uiState.update {
                        it.copy(
                            errorOrSuccess = result.e?.message.toString(),
                            showDialog = false
                        )
                    }
                }
            }
        }
    }

    /** Function to check if a with the entered Email or Username exists or not
     * @param onSuccess on Success lambda triggered when a user is found
     * @param onError on Error lambda triggered wen a user is not found
     */
    fun filterUser(
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        viewModelScope.launch(Dispatchers.Main) {
            uiState.update { it.copy(isLoading = true) }
            val users =
                if (uiState.value.emailOrUsername.matches(Regex("^[a-zA-Z0-9_.]+|[a-zA-Z]+\$\n"))) {
                    _users.value?.filter { it.username == uiState.value.emailOrUsername }
                } else {
                    _users.value?.filter { it.email == uiState.value.emailOrUsername }
                }
            delay(1000L)
            if (!users.isNullOrEmpty()) {
                uiState.update { uiState ->
                    uiState.copy(
                        profileImage = users.first().profileImage.toUri(),
                        email = users.first().email,
                        username = users.first().username,
                        isLoading = false
                    )
                }
                onSuccess()
            } else {
                onError()
                uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun setEmailOrUsername(emailOrUsername: String) {
        uiState.update { it.copy(emailOrUsername = emailOrUsername) }
    }

    fun setEmail(email: String) {
        uiState.update { it.copy(email = email) }
    }

    fun setUsername(username: String) {
        uiState.update { it.copy(username = username) }
    }

    fun setPassword(password: String) {
        uiState.update { it.copy(password = password) }
    }

    fun setProfileImage(profileImage: Uri?) {
        uiState.update { it.copy(profileImage = profileImage) }
    }

    fun setDialog(value: Boolean) {
        uiState.update { it.copy(showDialog = value) }
    }

    fun setErrorOrSuccess(errorOrSuccess: String) {
        uiState.update { it.copy(errorOrSuccess = errorOrSuccess) }
    }

    fun setErrorOrSuccessEmail(errorOrSuccessEmail: String) {
        uiState.update { it.copy(errorOrSuccessEmail = errorOrSuccessEmail) }
    }

    fun setErrorOrSuccessUsername(errorOrSuccessUsername: String) {
        uiState.update { it.copy(errorOrSuccessUsername = errorOrSuccessUsername) }
    }

    fun clearEmail() {
        uiState.update { it.copy(email = "") }
    }

    fun clearUsername() {
        uiState.update { it.copy(username = "") }
    }

    fun clearErrorOrSuccess() {
        uiState.update { it.copy(errorOrSuccess = "") }
    }

    fun clearUiState() {
        uiState.update { uiState ->
            uiState.copy(
                emailOrUsername = "",
                password = "",
                profileImage = null,
                errorOrSuccess = "",
                errorOrSuccessEmail = "",
                errorOrSuccessUsername = "",
                errorTitle = "",
                errorSubTitle = ""
            )
        }
    }
}