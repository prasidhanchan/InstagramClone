package com.instagramclone.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.instagramclone.firebase.repository.ProfileRepositoryImpl
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
): ViewModel() {

    private val _usernames: MutableStateFlow<List<String>?> = MutableStateFlow(null)
    val usernames = _usernames.asStateFlow()
    var uiState = MutableStateFlow(UiState())
        private set

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
                            "https://i.pinimg.com/564x/98/58/74/9858745cd157f2797065e639c5b3bf23.jpg",
                            "https://wallpaperaccess.in/public/uploads/preview/oshi-no-ko-yoasobi-anime-girl-wallpaper-s.jpg",
                            "https://cdn.hero.page/pfp/5bb14a97-d70c-4fa3-b462-3a8183481905-cool-one-piece-luffy-pfp-cool-anime-pfp-1.png",
                            "https://www.animeinformer.com/wp-content/uploads/2022/08/demon-slayer-pfp.png.webp"
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
        viewModelScope.launch(Dispatchers.IO) {
            val result = profileRepository.getAllUsernames()

            delay(1000L)
            if (result.e == null) {
                _usernames.update { result.data }
            } else {
                uiState.update { it.copy(error = result.e?.message.toString()) }
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
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            profileRepository.updateUserDetails(
                key = when (text) {
                    "Name" -> "name"
                    "Username" -> "username"
                    "Bio" -> "bio"
                    else -> "links"
                },
                value = value,
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
}