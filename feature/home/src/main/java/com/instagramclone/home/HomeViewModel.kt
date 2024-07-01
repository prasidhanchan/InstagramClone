package com.instagramclone.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.instagramclone.remote.repository.HomeRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepositoryImpl
) : ViewModel() {

    var uiState = MutableStateFlow(UiState())
        private set

    private val currentUser = FirebaseAuth.getInstance().currentUser

    /**
     * Function to get all the Posts from the followings.
     * @param following List of user Ids of the followings.
     */
    fun getPosts(following: List<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            val mResult = homeRepository.getPosts(
                following = following,
                currentUserId = currentUser?.uid!!
            )

            delay(1000L)
            mResult.distinctUntilChanged().collectLatest { result ->
                withContext(Dispatchers.Main) {
                    if (result.e == null && !result.isLoading!!) {
                        uiState.update {
                            it.copy(
                                posts = result.data!!,
                                isLoading = false
                            )
                        }
                    } else {
                        uiState.update {
                            it.copy(
                                error = result.e.toString(),
                                isLoading = false
                            )
                        }
                    }
                }
            }
        }
    }

    /**
     * Function to get all the Stories from the followings.
     * @param following List of user Ids of the followings.
     */
    fun getStories(following: List<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            val mResult = homeRepository.getStories(
                following = following,
                currentUserId = currentUser?.uid!!
            )

            delay(1000L)
            mResult.distinctUntilChanged().collectLatest { result ->
                withContext(Dispatchers.Main) {
                    if (result.e == null && !result.isLoading!!) {
                        uiState.update {
                            it.copy(userStories = result.data!!)
                        }
                    } else {
                        uiState.update {
                            it.copy(error = result.e?.message.toString())
                        }
                    }
                }
            }
        }
    }

    private fun clearUiState() {
        uiState.update { UiState() }
    }

    override fun onCleared() {
        super.onCleared()
        clearUiState()
    }
}