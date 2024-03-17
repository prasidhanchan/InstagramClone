package com.instagramclone.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.instagramclone.util.models.Post

/**
 * List of posts (LazyColumn)
 * @param innerPadding Requires [PaddingValues]
 * @param posts Requires list of [Posts]
 * @param currentUserId Requires current user ID
 * @param enableHeader Enable TopContent if any
 * @param onLikeClick on like click of a [Post]
 * @param onUnLikeClick on unlike click of a [Post]
 * @param onSendClick on send click of a [Post]
 * @param onSaveClick on save click of a [Post]
 * @param onUnfollowClick on unfollow click of a [Post]
 * @param onDeletePostClick on delete click of a [Post], only shown for personal posts
 * @param onUsernameClick on username click of a [Post]
 * @param scrollState requires a LazyListState
 * @param topContent Top content composable to be shown if any
 */
@Composable
fun Posts(
    innerPadding: PaddingValues = PaddingValues(),
    posts: List<Post>,
    currentUserId: String,
    enableHeader: Boolean = true,
    onLikeClick: (Post) -> Unit,
    onUnLikeClick: (Post) -> Unit,
    onSendClick: () -> Unit,
    onSaveClick: () -> Unit,
    onUnfollowClick: () -> Unit,
    onDeletePostClick: (Post) -> Unit,
    onUsernameClick: (String) -> Unit,
    scrollState: LazyListState,
    topContent: @Composable () -> Unit = { }
) {
    LazyColumn(
        contentPadding = innerPadding,
        state = scrollState,
        content = {
            if (enableHeader) {
                item(key = "topContent") {
                    topContent()
                }
            }
            items(
                items = posts,
                key = { post -> post.images }
            ) { post ->
                if (posts.isNotEmpty()) {
                    PostCard(
                        post = post,
                        currentUserId = currentUserId,
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
}