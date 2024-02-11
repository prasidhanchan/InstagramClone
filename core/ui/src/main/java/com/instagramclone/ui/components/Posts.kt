package com.instagramclone.ui.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.instagramclone.util.models.Post

@Composable
fun Posts(
    posts: List<Post>,
    currentUserId: String,
    onLikeClicked: () -> Unit,
    onUnLikeClicked: () -> Unit,
    onSendClicked: () -> Unit,
    onSaveClicked: () -> Unit,
    onUsernameClicked: () -> Unit,
    topContent: @Composable () -> Unit
) {
    LazyColumn(
        content = {
            item(key = "topContent") {
                topContent()
            }
            items(
                items = posts,
                key = { post -> post.username }
            ) { post ->
                PostCard(
                    post = post,
                    currentUserId = currentUserId,
                    onLikeClicked = onLikeClicked,
                    onUnLikeClicked = onUnLikeClicked,
                    onSendClicked = onSendClicked,
                    onSaveClicked = onSaveClicked,
                    onUsernameClicked = onUsernameClicked
                )
            }
        }
    )
}