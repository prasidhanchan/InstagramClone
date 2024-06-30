package com.instagramclone.ui.components

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import androidx.media3.ui.PlayerView.SHOW_BUFFERING_NEVER

/**
 * IGPlayer for playing Reels, created from ExoPlayer.
 * @param modifier Requires [Modifier].
 * @param exoPlayer Requires the ExoPlayer instance from ViewModel.
 * @param resize Resize mode of the player, requires [AspectRatioFrameLayout].
 * @param enableTouchResponse Enable touch responses, if false, tap , double tap, etc won't work.
 * @param onLongPress define what happens on Long press of the player.
 * @param onPress define what happens on Press of the player.
 * @param onDoubleTap define what happens on Double press of the player.
 */
@UnstableApi
@Composable
fun IGPlayer(
    modifier: Modifier = Modifier,
    exoPlayer: ExoPlayer,
    resize: Int = AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH,
    enableTouchResponse: Boolean = true,
    onLongPress: () -> Unit = { },
    onPress: () -> Unit = { },
    onDoubleTap: () -> Unit = { }
) {
    val lifecycleOwner by rememberUpdatedState(LocalLifecycleOwner.current)

    AndroidView(
        modifier = if (enableTouchResponse) {
            modifier
                .pointerInput(Unit) {
                    detectTapGestures(
                        onLongPress = { onLongPress() },
                        onPress = { onPress() },
                        onDoubleTap = { onDoubleTap() }
                    )
                }
        } else {
            modifier
        },
        factory = { context ->
            PlayerView(context).apply {
                useController = false
                controllerAutoShow = false
                setShowBuffering(SHOW_BUFFERING_NEVER)
                keepScreenOn = true
                layoutParams = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                resizeMode = resize
                player = exoPlayer
            }
        }
    )

    DisposableEffect(lifecycleOwner) {
        val lifecycleEventObserver = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> exoPlayer.play()
                Lifecycle.Event.ON_PAUSE -> exoPlayer.pause()
                Lifecycle.Event.ON_DESTROY -> {
                    exoPlayer.run {
                        stop()
                        release()
                    }
                }

                else -> Unit
            }
        }

        lifecycleOwner.lifecycle.addObserver(lifecycleEventObserver)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(lifecycleEventObserver)
        }
    }
}