package com.instagramclone.profile

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.instagramclone.firebase.repository.ProfileRepositoryImpl
import com.instagramclone.ui.R
import com.instagramclone.util.models.Post
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepositoryImpl
) : ViewModel() {

    private val _usernames: MutableStateFlow<List<String>?> = MutableStateFlow(null)
    val usernames = _usernames.asStateFlow()
    var uiState = MutableStateFlow(UiState())
        private set

    init {
        getUserData()
    }

    /**
     * Function to get current user data from Firestore
     */
    fun getUserData() {
        uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            val result = profileRepository.getUserData()

            delay(1500L)
            if (result.data != null) {
                uiState.update { uiState ->
                    uiState.copy(
                        profileImage = result.data?.profileImage!!,
                        username = result.data?.username!!,
                        name = result.data?.name!!,
                        bio = result.data?.bio!!,
                        links = result.data?.links!!,
                        gender = result.data?.gender!!,
                        followers = result.data?.followersList!!,
                        following = result.data?.followingsList!!,
                        isLoading = false,
                        //TODO remove
                        myPosts = listOf(
                            Post(
                                profileImage = "https://firebasestorage.googleapis.com/v0/b/instagram-clone-3eeaf.appspot.com/o/ProfileImage%2FvJdDRERlD9VVleMOJDIoYTXSYu53.jpg?alt=media&token=e1831424-81db-44ac-baaa-b8e4dced084b",
                                username = "pra_sidh_22",
                                isVerified = true,
                                images = listOf(
                                    "https://i.pinimg.com/564x/98/58/74/9858745cd157f2797065e639c5b3bf23.jpg"
                                )
                            ),
                            Post(
                                profileImage = "https://firebasestorage.googleapis.com/v0/b/instagram-clone-3eeaf.appspot.com/o/ProfileImage%2FvJdDRERlD9VVleMOJDIoYTXSYu53.jpg?alt=media&token=e1831424-81db-44ac-baaa-b8e4dced084b",
                                username = "pra_sidh_22",
                                isVerified = true,
                                images = listOf(
                                    "https://wallpaperaccess.in/public/uploads/preview/oshi-no-ko-yoasobi-anime-girl-wallpaper-s.jpg",
                                )
                            ),
                            Post(
                                profileImage = "https://firebasestorage.googleapis.com/v0/b/instagram-clone-3eeaf.appspot.com/o/ProfileImage%2FvJdDRERlD9VVleMOJDIoYTXSYu53.jpg?alt=media&token=e1831424-81db-44ac-baaa-b8e4dced084b",
                                username = "pra_sidh_22",
                                isVerified = true,
                                images = listOf(
                                    "https://cdn.hero.page/pfp/5bb14a97-d70c-4fa3-b462-3a8183481905-cool-one-piece-luffy-pfp-cool-anime-pfp-1.png"
                                )
                            ),
                            Post(
                                profileImage = "https://firebasestorage.googleapis.com/v0/b/instagram-clone-3eeaf.appspot.com/o/ProfileImage%2FvJdDRERlD9VVleMOJDIoYTXSYu53.jpg?alt=media&token=e1831424-81db-44ac-baaa-b8e4dced084b",
                                username = "pra_sidh_22",
                                isVerified = true,
                                images = listOf(
                                    "https://www.animeinformer.com/wp-content/uploads/2022/08/demon-slayer-pfp.png.webp"
                                )
                            )
                        )
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
                    else -> "links"
                },
                value = value,
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

    fun setShowPostScreen(value: Boolean, postIndex: Int) {
        uiState.update {
            it.copy(
                showPostScreen = value,
                postIndex = postIndex
            )
        }
    }

    fun setNewImage(newImage: Uri?) {
        uiState.update { it.copy(newProfileImage = newImage) }
    }
}