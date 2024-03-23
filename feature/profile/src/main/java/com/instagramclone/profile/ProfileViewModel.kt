package com.instagramclone.profile

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.instagramclone.firebase.models.IGUser
import com.instagramclone.firebase.repository.ProfileRepositoryImpl
import com.instagramclone.ui.R
import com.instagramclone.util.models.Post
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepositoryImpl
) : ViewModel() {

    private val _usernames: MutableStateFlow<List<String>?> = MutableStateFlow(null)
    val usernames = _usernames.asStateFlow()

    private val currentUser = FirebaseAuth.getInstance().currentUser

    var uiState = MutableStateFlow(UiState())
        private set

    init {
        getMyData()
        getMyPosts()
    }

    /**
     * Function to get current user data from Firestore
     */
    fun getMyData() {
        uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            val result = profileRepository.getUserData(currentUser = currentUser)

            delay(1500L)
            if (result.data != null) {
                uiState.update { uiState ->
                    uiState.copy(
                        profileImage = result.data?.profileImage!!,
                        username = result.data?.username!!,
                        name = result.data?.name!!,
                        email = result.data?.email!!,
                        phone = result.data?.phone!!,
                        password = result.data?.password!!,
                        bio = result.data?.bio!!,
                        links = result.data?.links!!,
                        gender = result.data?.gender!!,
                        followers = result.data?.followersList!!,
                        following = result.data?.followingList!!,
                        isLoading = false
                    )
                }
            } else {
                uiState.update {
                    it.copy(
                        error = result.e?.message.toString(),
                        isLoading = false
                    )
                }
            }
        }
    }

    /**
     * Function to get all usernames from Firestore
     */
    fun getAllUsernames() {
        uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            val result = profileRepository.getAllUsernames()

            delay(1500L)
            if (result.e == null) {
                _usernames.update { result.data }
                uiState.update { it.copy(isLoading = false) }
            } else {
                uiState.update {
                    it.copy(
                        error = result.e?.message.toString(),
                        isLoading = false
                    )
                }
            }
        }
    }

    /**
     * Function to update the user details such as, name, username, etc
     * @param text Requires the current field which is being updated
     * @param value Requires the new value which is to be updated
     * @param onSuccess On success lambda which is triggered when the update task is completed
     */
    fun updateUserDetails(
        text: String,
        value: String,
        context: Context,
        onSuccess: () -> Unit
    ) {
        uiState.update { it.copy(isUpdating = true) }
        viewModelScope.launch(Dispatchers.IO) {
            delay(1000L)
            profileRepository.updateUserDetails(
                key = when (text) {
                    context.getString(R.string.name) -> "name"
                    context.getString(R.string.username) -> "username"
                    context.getString(R.string.bio) -> "bio"
                    context.getString(R.string.profileimage) -> "profileImage"
                    context.getString(R.string.gender) -> "gender"
                    context.getString(R.string.password) -> "password"
                    else -> "links"
                },
                value = value,
                currentUser = currentUser,
                onSuccess = {
                    onSuccess()
                    uiState.update { it.copy(isUpdating = false) }
                },
                onError = { error ->
                    uiState.update {
                        it.copy(
                            error = error,
                            isUpdating = false
                        )
                    }
                }
            )
        }
    }

    /** Function to convert Gallery image Uri to url using Firebase Storage
     * @param newImage Requires the uri of an image from gallery
     * @param onSuccess on Success lambda triggered when download url is retrieved from Firebase Storage
     */
    fun convertToUrl(
        newImage: Uri,
        onSuccess: (String) -> Unit
    ) {
        uiState.update { it.copy(isUpdating = true) }
        viewModelScope.launch(Dispatchers.IO) {
            profileRepository.convertToUrl(
                newImage = newImage,
                currentUser = currentUser,
                onSuccess = { downloadUrl ->
                    uiState.update { it.copy(isUpdating = false) }
                    onSuccess(downloadUrl)
                },
                onError = { error ->
                    uiState.update {
                        it.copy(
                            error = error,
                            isUpdating = false
                        )
                    }
                }
            )
        }
    }

    /**
     * Function to change the account password,
     * password is only updated if the current password matches with
     * the entered password and new passwords matches the password criteria.
     * @param currentPassword Current active password
     * @param passwordState Entered current password
     * @param newPasswordState New password password
     * @param rePasswordState New password re-typed
     * @param onSuccess On Success lambda triggered when entered passwords are correct
     */
    fun changePassword(
        currentPassword: String,
        passwordState: String,
        newPasswordState: String,
        rePasswordState: String,
        context: Context,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            if (passwordState == currentPassword && currentPassword.isNotEmpty()) {
                if (newPasswordState == rePasswordState) {
                    if (newPasswordState.length > 6) {
                        profileRepository.changePassword(
                            password = newPasswordState,
                            currentUser = currentUser,
                            onSuccess = onSuccess,
                            onError = { error ->
                                uiState.update { it.copy(error = error) }
                            }
                        )
                    } else {
                        uiState.update { it.copy(error = context.getString(R.string.your_password_must_be)) }
                    }
                } else {
                    uiState.update { it.copy(error = context.getString(R.string.new_password_does_not_match)) }
                }
            } else {
                uiState.update { it.copy(error = context.getString(R.string.current_password_is_incorrect)) }
            }
        }
    }

    /** Function to log out of firebase account */
    fun logOut() = viewModelScope.launch(Dispatchers.IO) { profileRepository.logOut() }

    /**
     * Function to get all personal posts on profile
     */
    fun getMyPosts() {
        uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            val result = profileRepository.getMyPosts(currentUser = currentUser)

            delay(800L)
            withContext(Dispatchers.Main) {
                result.distinctUntilChanged().collectLatest { result ->
                    if (result.e == null && !result.isLoading!!) {
                        uiState.update {
                            it.copy(
                                myPosts = result.data!!,
                                isLoading = false
                            )
                        }
                    } else {
                        uiState.update {
                            it.copy(
                                error = result.e?.message.toString(),
                                isLoading = false
                            )
                        }
                    }
                }
            }
        }
    }

    /**
     * Function to delete Post Note : The post will be deleted from FireStore as well as Firebase Storage
     * @param post requires the actual [Post]
     * @param onSuccess onSuccess lambda triggered when the deletion task is completed
     */
    fun deletePost(
        post: Post,
        onSuccess: () -> Unit
    ) {
        uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            profileRepository.deletePost(
                post = post,
                currentUser = currentUser,
                onSuccess = {
                    onSuccess()
                    uiState.update { it.copy(isLoading = false) }
                },
                onError = { error ->
                    uiState.update {
                        it.copy(
                            error = error,
                            isLoading = false
                        )
                    }
                }
            )
        }
    }

    /**
     * Function to get the Profile of the selected user / whose profile is visited
     * @param userId User Id of the selected user
     */
    fun getUserProfile(userId: String) {
        uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            val currentUser = FirebaseAuth.getInstance().currentUser
            val result = profileRepository.getUserProfile(userId = userId)

            delay(1000L)
            withContext(Dispatchers.Main) {
                if (result.e == null && !result.isLoading!!) {
                    uiState.update { uiState ->
                        uiState.copy(
                            selectedUserProfile = result.data!!,
                            isFollowing = result.data!!.followersList.any { it == currentUser?.uid }
                        )
                    }
                    delay(500L) // Artificial delay to make isFollowing load properly
                    uiState.update { it.copy(isLoading = false) }
                } else {
                    uiState.update {
                        it.copy(
                            error = result.e?.message.toString(),
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    /**
     * Function to retrieve all the posts of the selected user / whose profile is visited
     * @param userId User Id of the selected user
     */
    fun getUserPosts(userId: String) {
        uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            val result = profileRepository.getUserPosts(userId = userId)

            delay(1500L)
            withContext(Dispatchers.Main) {
                result.distinctUntilChanged().collectLatest { result ->
                    if (result.e == null && !result.isLoading!!) {
                        uiState.update {
                            it.copy(
                                selectedUserPosts = result.data!!,
                                isLoading = false
                            )
                        }
                    } else {
                        uiState.update {
                            it.copy(
                                error = result.e?.message.toString(),
                                isLoading = false
                            )
                        }
                    }
                }
            }
        }
    }

    /**
     * Function to follow a user
     * @param userId User Id of the selected user
     * @param onSuccess On Success lambda triggered when the user is successfully followed
     */
    fun follow(
        userId: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            profileRepository.follow(
                userId = userId,
                currentUser = currentUser,
                onSuccess = onSuccess,
                onError = { error ->
                    uiState.update { it.copy(error = error) }
                }
            )
        }
    }

    /**
     * Function to unfollow a user
     * @param userId User Id of the selected user
     * @param onSuccess On Success lambda triggered when the user is successfully unfollowed
     */
    fun unFollow(
        userId: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            profileRepository.unFollow(
                userId = userId,
                currentUser = currentUser,
                onSuccess = onSuccess,
                onError = { error ->
                    uiState.update { it.copy(error = error) }
                }
            )
        }
    }

    /**
     * Function to like a user [Post]
     * @param userId Requires user of the the selected post
     * @param timeStamp Requires the timeStamp of the selected post
     * @param onSuccess onSuccess lambda triggered when the post is liked successfully
     */
    fun like(
        userId: String,
        timeStamp: Long,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            profileRepository.like(
                userId = userId,
                timeStamp = timeStamp,
                currentUser = currentUser,
                onSuccess = onSuccess,
                onError = { error ->
                    uiState.update { it.copy(error = error) }
                }
            )
        }
    }

    /**
     * Function to unlike a user [Post]
     * @param userId Requires user of the the selected post
     * @param timeStamp Requires the timeStamp of the selected post
     * @param onSuccess onSuccess lambda triggered when the post is unliked successfully
     */
    fun unLike(
        userId: String,
        timeStamp: Long,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            profileRepository.unLike(
                userId = userId,
                timeStamp = timeStamp,
                currentUser = currentUser,
                onSuccess = onSuccess,
                onError = { error ->
                    uiState.update { it.copy(error = error) }
                }
            )
        }
    }

    fun setText(text: String) {
        uiState.update { it.copy(textState = text) }
    }

    fun clearText() {
        uiState.update { it.copy(textState = "") }
    }

    fun setError(error: String) {
        uiState.update { it.copy(error = error) }
    }

    fun clearError() {
        uiState.update { it.copy(error = "") }
    }

    fun setIsUserDetailChanged(value: Boolean) {
        uiState.update { it.copy(isUserDetailChanged = value) }
    }

    private fun clearUiState() {
        uiState.update { UiState() }
    }

    fun setNewImage(newImage: Uri?) {
        uiState.update { it.copy(newProfileImage = newImage) }
    }

    fun setGender(gender: String) {
        uiState.update { it.copy(gender = gender) }
    }

    fun setPassword(value: String) {
        uiState.update { it.copy(passwordState = value) }
    }

    fun setNewPassword(value: String) {
        uiState.update { it.copy(newPasswordState = value) }
    }

    fun setRePassword(value: String) {
        uiState.update { it.copy(rePasswordState = value) }
    }

    fun setSelectedPost(post: Post) {
        uiState.update { it.copy(selectedPost = post) }
    }

    fun setIsFollowing(isFollowing: Boolean) {
        uiState.update { it.copy(isFollowing = isFollowing) }
    }

    fun clearUserProfile() {
        viewModelScope.launch {
            delay(500L) //Delaying so that Ui is not cleared before popping backstack
            uiState.update {
                it.copy(
                    selectedUserProfile = IGUser(),
                    selectedUserPosts = emptyList()
                )
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        clearUiState()
    }
}