package com.instagramclone.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.instagramclone.ui.R
import com.instagramclone.util.models.Post

/**
 * Posts Image to display posts containing images.
 * @param post Requires a [Post].
 * @param interactionSource Requires the [MutableInteractionSource].
 * @param onMoreClick Event that needs to be triggered on More button click.
 * @param onUsernameClick Event that needs to be triggered on Username click.
 */
@Composable
fun PostImage(
    post: Post,
    interactionSource: MutableInteractionSource,
    onMoreClick: () -> Unit,
    onUsernameClick: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PostHeader(
            post = post,
            interactionSource = interactionSource,
            onMoreClick = onMoreClick,
            onUsernameClick = onUsernameClick
        )
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 200.dp),
            model = post.mediaList.first(), //TODO integrate Pager
            contentScale = ContentScale.Crop,
            filterQuality = FilterQuality.Low,
            contentDescription = stringResource(R.string.post, post.username)
        )
    }
}