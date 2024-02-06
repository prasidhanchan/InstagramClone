package com.instagramclone.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.instagramclone.ui.R
import com.instagramclone.util.constants.Utils
import com.instagramclone.util.models.Post

@Composable
fun PostCard(
    post: Post,
    onLikeClicked: () -> Unit,
    onSendClicked: () -> Unit,
    onSaveClicked: () -> Unit,
    onUsernameClicked: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    var imageSize by remember { mutableFloatStateOf(0f) }
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(Alignment.CenterVertically)
            .padding(bottom = 15.dp),
        color = Utils.IgBlack
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth()
                    .height(50.dp)
                    .clickable(
                        indication = null,
                        interactionSource = interactionSource,
                        onClick = onUsernameClicked
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Surface(
                    modifier = Modifier.size(30.dp),
                    shape = CircleShape,
                    color = Utils.IgOffBlack
                ) {
                    AsyncImage(
                        modifier = Modifier.fillMaxSize(),
                        model = post.profileImage,
                        contentDescription = post.username
                    )
                }
                Text(
                    modifier = Modifier.padding(start = 10.dp, end = 5.dp),
                    text = post.username,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (post.isVerified) {
                    Icon(
                        painter = painterResource(id = R.drawable.verify),
                        tint = Utils.IgBlue,
                        contentDescription = stringResource(R.string.verified)
                    )
                }
            }


            AsyncImage(
                modifier = Modifier.defaultMinSize(minHeight = 200.dp),
                model = post.images.first(), //TODO integrate Pager
                contentScale = ContentScale.Fit,
                onLoading = { imageSize = it.painter?.intrinsicSize?.height ?: 0f },
                contentDescription = stringResource(R.string.post, post.username)
            )

            Row(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
                    .height(25.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(5f),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Icon(
                            modifier = Modifier
                                .scale(1.3f)
                                .padding(horizontal = 8.dp)
                                .clickable(
                                    indication = null,
                                    interactionSource = interactionSource,
                                    onClick = onLikeClicked
                                ),
                            painter = painterResource(id = R.drawable.heart_outlined),
                            tint = Color.White,
                            contentDescription = stringResource(R.string.like_unlike)
                        )
                        Icon(
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .clickable(
                                    indication = null,
                                    interactionSource = interactionSource,
                                    onClick = { }
                                ),
                            painter = painterResource(id = R.drawable.comment),
                            tint = Color.White,
                            contentDescription = stringResource(R.string.like_unlike)
                        )
                        Icon(
                            modifier = Modifier
                                .scale(1.1f)
                                .padding(horizontal = 8.dp)
                                .clickable(
                                    indication = null,
                                    interactionSource = interactionSource,
                                    onClick = onSendClicked
                                ),
                            painter = painterResource(id = R.drawable.send),
                            tint = Color.White,
                            contentDescription = stringResource(R.string.like_unlike)
                        )
                    }
                }
                Icon(
                    modifier = Modifier
                        .scale(1.3f)
                        .weight(0.6f)
                        .clickable(
                            indication = null,
                            interactionSource = interactionSource,
                            onClick = onSaveClicked
                        ),
                    painter = painterResource(id = R.drawable.save_outlined),
                    tint = Color.White,
                    contentDescription = stringResource(R.string.like_unlike)
                )
            }
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp),
                text = stringResource(R.string.likes, post.likes),
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    textAlign = TextAlign.Start
                )
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp)
                    .padding(vertical = 5.dp)
            ) {
                Text(
                    modifier = Modifier
                        .padding(start = 15.dp, end = 5.dp),
                    text = post.username,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        textAlign = TextAlign.Start
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    modifier = Modifier,
                    text = post.caption,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White,
                        textAlign = TextAlign.Start
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp)
                    .clickable(
                        indication = null,
                        interactionSource = interactionSource,
                        onClick = { }
                    ),
                text = stringResource(R.string.view_all_comments, post.comments.size),
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White.copy(alpha = 0.5f),
                    textAlign = TextAlign.Start
                )
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 8.dp)
                    .clickable(
                        indication = null,
                        interactionSource = interactionSource,
                        onClick = { }
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Surface(
                    modifier = Modifier.size(30.dp),
                    shape = CircleShape,
                    color = Utils.IgOffBlack
                ) {
                    AsyncImage(
                        modifier = Modifier.fillMaxSize(),
                        model = post.profileImage,
                        contentDescription = post.username
                    )
                }
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    text = stringResource(R.string.add_a_comment),
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White.copy(alpha = 0.5f),
                        textAlign = TextAlign.Start
                    )
                )
            }

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                text = "${post.timeStamp} days ago",
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White.copy(alpha = 0.5f),
                    textAlign = TextAlign.Start
                )
            )
        }
    }
}

@Preview(apiLevel = 33)
@Composable
fun PostCardPreview() {
    PostCard(
        post = Post(
            username = "pra_sidh_22",
            likes = 52456,
            caption = "Who you picking?",
            timeStamp = 5,
            images = listOf(""),
            isVerified = true
        ),
        onLikeClicked = {  },
        onSendClicked = {  },
        onSaveClicked = {  },
        onUsernameClicked = {  },
    )
}