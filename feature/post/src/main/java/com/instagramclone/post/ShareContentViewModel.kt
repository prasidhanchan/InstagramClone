package com.instagramclone.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.instagramclone.firebase.repository.ShareContentRepositoryImpl
import com.instagramclone.util.models.Image
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
class ShareContentViewModel @Inject constructor(
    private val contentResolver: ContentResolver,
    private val shareContentRepository: ShareContentRepositoryImpl
) : ViewModel() {
    var uiState = MutableStateFlow(UiState())
        private set

    init {
        getImages()
    }

    private fun getImages() {
        viewModelScope.launch(Dispatchers.IO) {
            contentResolver.getImages().distinctUntilChanged().collectLatest { imageList ->
                uiState.update {
                    it.copy(
                        images = imageList,
                        selectedImage = imageList.firstOrNull() // Default image
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

    fun setImage(image: Image?) {
        uiState.update { it.copy(selectedImage = image) }
    }

    fun setCaption(caption: String) {
        uiState.update { it.copy(caption = caption) }
    }
}
