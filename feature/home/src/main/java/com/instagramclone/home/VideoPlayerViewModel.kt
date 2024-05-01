package com.instagramclone.home

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoPlayerViewModel @Inject constructor(
    private val _exoPlayer: ExoPlayer
) : ViewModel() {
    val exoPlayer = _exoPlayer

    private val _currentPosition = MutableStateFlow(0L)
    val currentPosition = _currentPosition.asStateFlow()

    private val _duration = MutableStateFlow(0L)
    val duration = _duration.asStateFlow()

    private val job: Job? = null

    init {
        _exoPlayer.prepare()
        trackPositions()
    }

    private fun trackPositions() {
        viewModelScope.launch(Dispatchers.Main) {
            while (true) {
                job.run {
                    _currentPosition.update { _exoPlayer.currentPosition.coerceAtLeast(0L) }
                    _duration.update { _exoPlayer.duration.coerceAtLeast(0L) }
                }
                delay(1000L)
            }
        }
    }

    fun startPlayer(url: String) {
        _exoPlayer.apply {
            setMediaItem(MediaItem.fromUri(Uri.parse(url)))
            playWhenReady = true
        }
    }
}