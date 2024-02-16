package com.instagramclone.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.instagramclone.firebase.repository.ProfileRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepositoryImpl
): ViewModel() {
    var uiState = MutableStateFlow(UiState())
        private set

    init {
        getUserData()
    }

    private fun getUserData() {
        uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            val result = profileRepository.getUserData()

            delay(1000L)
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
}