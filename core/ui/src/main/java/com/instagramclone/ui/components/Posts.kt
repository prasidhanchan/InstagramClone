package com.instagramclone.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.instagramclone.util.constants.getCurrentlyPlayingPost
import com.instagramclone.util.models.Post
import com.instagramclone.util.test.TestPlayer

/**
 * List of posts (LazyColumn)
 * @param innerPadding Requires [PaddingValues]
 * @param posts Requires list of [Posts]
 * @param currentUserId Requires current user ID
 * @param exoPlayer Requires [ExoPlayer]
 * @param onWatchAgainClick restart the player
 * @param enableHeader Enable TopContent if any
 * @param onLikeClick on like click of a [Post]
 * @param onUnLikeClick on unlike click of a [Post]
 * @param onSendClick on send click of a [Post]
 * @param onSaveClick on save click of a [Post]
 * @param onUnfollowClick on unfollow click of a [Post]
 * @param onDeletePostClick on delete click of a [Post], only shown for personal posts
 * @param onUsernameClick on username click of a [Post]
 * @param state requires a LazyListState
 * @param topContent Top content composable to be shown if any
 */
@UnstableApi
@Composable
fun Posts(
    innerPadding: PaddingValues = PaddingValues(),
    posts: List<Post>,
    currentUserId: String,
    exoPlayer: ExoPlayer,
    currentPosition: Long,
    duration: Long,
    onWatchAgainClick: (String) -> Unit,
    enableHeader: Boolean = true,
    onLikeClick: (Post) -> Unit,
    onUnLikeClick: (Post) -> Unit,
    onSendClick: () -> Unit,
    onSaveClick: () -> Unit,
    onUnfollowClick: () -> Unit,
    onDeletePostClick: (Post) -> Unit,
    onUsernameClick: (String) -> Unit,
    state: LazyListState,
    topContent: @Composable () -> Unit = { }
) {
    val currentlyPlayingPost =
        if (posts.isNotEmpty()) getCurrentlyPlayingPost(state = state, posts = posts) else null

    LazyColumn(
        contentPadding = innerPadding,
        state = state,
        content = {
            if (enableHeader) {
                item(key = "topContent") {
                    Column {
                        topContent()
                    }
                }
            }

            if (posts.isNotEmpty()) {
                items(
                    items = posts,
                    key = { post -> "${post.userId}${post.timeStamp}" }
                ) { post ->
                    PostCard(
                        post = post,
                        currentUserId = currentUserId,
                        exoPlayer = exoPlayer,
                        currentPosition = currentPosition,
                        duration = duration,
                        isPlaying = MediaItem.fromUri(post.mediaList.first()) ==
                                MediaItem.fromUri(currentlyPlayingPost?.mediaList?.first() ?: ""),
                        onWatchAgainClick = onWatchAgainClick,
                        onLikeClick = onLikeClick,
                        onUnLikeClick = onUnLikeClick,
                        onSendClick = onSendClick,
                        onSaveClick = onSaveClick,
                        onUnfollowClick = onUnfollowClick,
                        onDeletePostClick = onDeletePostClick,
                        onUsernameClick = onUsernameClick
                    )
                }
            }
        }
    )

    LaunchedEffect(key1 = currentlyPlayingPost) {
        if (currentlyPlayingPost != null) {
            if (MediaItem.fromUri(currentlyPlayingPost.mediaList.first()) == exoPlayer.currentMediaItem &&
                currentPosition < duration
            ) {
                exoPlayer.playWhenReady = true
            } else {
                exoPlayer.apply {
                    setMediaItem(MediaItem.fromUri(currentlyPlayingPost.mediaList.first()))
                    playWhenReady = true
                }
            }
        } else {
            exoPlayer.pause()
        }
    }
}

@UnstableApi
@Preview
@Composable
private fun PostsPreview() {
    Posts(
        posts = listOf(
            Post(
                mediaList = listOf("a"),
                username = "pra_sidh_22",
                isVerified = true
            ),
            Post(
                mediaList = listOf("b"),
                username = "kawaki",
                isVerified = true
            ),
        ),
        currentUserId = "",
        exoPlayer = TestPlayer(),
        currentPosition = 1000L,
        duration = 800L,
        onWatchAgainClick = { },
        onLikeClick = { },
        onUnLikeClick = { },
        onSendClick = { },
        onSaveClick = { },
        onUnfollowClick = { },
        onDeletePostClick = { },
        onUsernameClick = { },
        state = rememberLazyListState()
    )
}