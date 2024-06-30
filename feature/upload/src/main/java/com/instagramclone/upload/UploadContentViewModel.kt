package com.instagramclone.upload

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.instagramclone.remote.repository.UploadContentRepositoryImpl
import com.instagramclone.upload.util.ContentResolver
import com.instagramclone.util.models.Media
import com.instagramclone.util.models.Post
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UploadContentViewModel @Inject constructor(
    private val contentResolver: ContentResolver,
    private val shareContentRepository: UploadContentRepositoryImpl
) : ViewModel() {
    var uiState = MutableStateFlow(UiState())
        private set

    fun getMedia() {
        viewModelScope.launch(Dispatchers.IO) {
            contentResolver.getMedia().distinctUntilChanged().collectLatest { mediaList ->
                uiState.update {
                    it.copy(
                        mediaList = mediaList,
                        selectedMedia = mediaList.firstOrNull() // Default image
                    )
                }
            }
        }
    }

    fun getImages() {
        viewModelScope.launch(Dispatchers.IO) {
            contentResolver.getImages().distinctUntilChanged().collectLatest { imageList ->
                uiState.update {
                    it.copy(
                        mediaList = imageList
                    )
                }
            }
        }
    }

    fun getVideos() {
        viewModelScope.launch(Dispatchers.IO) {
            contentResolver.getVideos().distinctUntilChanged().collectLatest { videoList ->
                uiState.update {
                    it.copy(
                        mediaList = videoList
                    )
                }
            }
        }
    }

    fun uploadPost(
        post: Post,
        onSuccess: () -> Unit
    ) {
        uiState.update { it.copy(isUploading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            shareContentRepository.uploadPost(
                post = post,
                onSuccess = {
                    onSuccess()
                    uiState.update { it.copy(isUploading = false) }
                },
                onError = { error ->
                    uiState.update {
                        it.copy(
                            error = error,
                            isUploading = false
                        )
                    }
                }
            )
        }
    }

    fun setMedia(media: Media?) {
        uiState.update { it.copy(selectedMedia = media) }
    }

    fun setCaption(caption: String) {
        uiState.update { it.copy(caption = caption) }
    }

    private fun clearUiState() {
        uiState.update { UiState() }
    }

    override fun onCleared() {
        super.onCleared()
        clearUiState()
    }
}
