package com.instagramclone.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.instagramclone.util.models.Post

@Composable
fun Posts(
    innerPadding: PaddingValues = PaddingValues(),
    posts: List<Post>,
    currentUserId: String,
    enableHeader: Boolean = true,
    onLikeClick: () -> Unit,
    onUnLikeClick: () -> Unit,
    onSendClick: () -> Unit,
    onSaveClick: () -> Unit,
    onUnfollowClick: () -> Unit,
    onDeletePostClick: (Post) -> Unit,
    onUsernameClick: (String) -> Unit,
    scrollState: LazyListState,
    topContent: @Composable () -> Unit = {  }
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