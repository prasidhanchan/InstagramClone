package com.instagramclone.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.instagramclone.util.models.Post

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Posts(
    innerPadding: PaddingValues = PaddingValues(),
    posts: List<Post>,
    currentUserId: String,
    enableHeader: Boolean = true,
    onLikeClicked: () -> Unit,
    onUnLikeClicked: () -> Unit,
    onSendClicked: () -> Unit,
    onSaveClicked: () -> Unit,
    onUsernameClicked: () -> Unit,
    scrollState: LazyListState = LazyListState(),
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
                        onLikeClicked = onLikeClicked,
                        onUnLikeClicked = onUnLikeClicked,
                        onSendClicked = onSendClicked,
                        onSaveClicked = onSaveClicked,
                        onUsernameClicked = onUsernameClicked
                    )
                }
            }
        }
    )
}