package com.instagramclone.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.instagramclone.firebase.repository.HomeRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepositoryImpl
): ViewModel() {

    var uiState = MutableStateFlow(UiState())
        private set

    init {
        getUserData()
    }
    private fun getUserData() {
        uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            val result = homeRepository.getUserData()

            delay(1000L)
            if (result.e == null && !result.isLoading!!) {
                withContext(Dispatchers.Main) {
                    uiState.update { uiState ->
                        uiState.copy(
                            username = result.data?.username!!,
                            profileImage = result.data?.profileImage!!,
                            isLoading = false
                        )
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
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