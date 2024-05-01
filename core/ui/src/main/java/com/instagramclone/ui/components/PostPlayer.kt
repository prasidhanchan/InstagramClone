package com.instagramclone.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.media3.common.C.VOLUME_FLAG_PLAY_SOUND
import androidx.media3.common.C.VOLUME_FLAG_REMOVE_SOUND_AND_VIBRATE
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import coil.compose.AsyncImage
import com.instagramclone.ui.R
import com.instagramclone.util.constants.Utils.IgBlack
import com.instagramclone.util.models.Post
import com.instagramclone.util.test.TestPlayer

/**
 * Posts Player to play posts containing videos.
 * @param exoPlayer Requires [ExoPlayer] instance.
 * @param post Requires a [Post].
 * @param currentPosition Requires the current player position.
 * @param duration Requires the total duration of the video.
 * @param isPlaying If the current video in the viewport is being played or not. note: It is different from exoplayer.isPlaying.
 * @param interactionSource Requires the [MutableInteractionSource].
 * @param onMoreClick Event that needs to be triggered on More button click.
 * @param onUsernameClick Event that needs to be triggered on Username click.
 * @param onWatchAgainClick Event that needs to be triggered on Watch again click.
 */
@UnstableApi
@Composable
fun PostPlayer(
    exoPlayer: ExoPlayer,
    post: Post,
    currentPosition: Long,
    duration: Long,
    isPlaying: Boolean,
    interactionSource: MutableInteractionSource,
    onMoreClick: () -> Unit,
    onUsernameClick: (String) -> Unit,
    onWatchAgainClick: (String) -> Unit
) {
    var isMuted by rememberSaveable(exoPlayer.isDeviceMuted) { mutableStateOf(exoPlayer.isDeviceMuted) }
    var showWatchAgain by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            if (isPlaying && exoPlayer.isPlaying) {
                IGPlayer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(600.dp),
                    exoPlayer = exoPlayer,
                    enableTouchResponse = false
                )
            } else {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(600.dp),
                    model = post.mediaList.firstOrNull(),
                    contentScale = ContentScale.Crop,
                    contentDescription = post.caption
                )
            }

            WatchAgainComp(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(600.dp),
                visible = showWatchAgain,
                onClick = {
                    onWatchAgainClick(post.mediaList.first())
                }
            )
            PostHeader(
                post = post,
                color = Color.Transparent,
                interactionSource = interactionSource,
                onMoreClick = onMoreClick,
                onUsernameClick = onUsernameClick
            )
        }

        Surface(
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 20.dp)
                .wrapContentSize(Alignment.Center)
                .clickable(
                    indication = null,
                    interactionSource = interactionSource,
                    onClick = {
                        if (isMuted) {
                            isMuted = false
                            exoPlayer.setDeviceMuted(false, VOLUME_FLAG_PLAY_SOUND)
                        } else {
                            isMuted = true
                            exoPlayer.setDeviceMuted(true, VOLUME_FLAG_REMOVE_SOUND_AND_VIBRATE)
                        }
                    }
                ),
            shape = CircleShape,
            color = IgBlack.copy(alpha = 0.8f)
        ) {
            Icon(
                modifier = Modifier.padding(all = 6.dp),
                painter = painterResource(
                    id = if (isMuted) R.drawable.speaker_muted else R.drawable.speaker
                ),
                tint = Color.White,
                contentDescription = stringResource(R.string.mute_unmute)
            )
        }
    }

    LaunchedEffect(key1 = currentPosition) {
        showWatchAgain = isPlaying && currentPosition != 0L && currentPosition >= duration
    }
}

@UnstableApi
@Preview
@Composable
private fun PostPlayerPreview() {
    PostPlayer(
        exoPlayer = TestPlayer(),
        post = Post(
            mediaList = listOf(
                "",
                ""
            )
        ),
        currentPosition = 800L,
        duration = 1000L,
        isPlaying = false,
        interactionSource = remember { MutableInteractionSource() },
        onMoreClick = { },
        onUsernameClick = { }
    ) { }
}