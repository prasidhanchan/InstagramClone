package com.instagramclone.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.instagramclone.ui.R
import com.instagramclone.util.constants.Utils
import com.instagramclone.util.constants.formatTimeStamp
import com.instagramclone.util.models.Post

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostCard(
    post: Post,
    currentUserId: String,
    onLikeClick: () -> Unit,
    onUnLikeClick: () -> Unit,
    onSendClick: () -> Unit,
    onSaveClick: () -> Unit,
    onUnfollowClick: () -> Unit,
    onDeletePostClick: (Post) -> Unit,
    onUsernameClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    val hasLiked = post.likes.any { postId -> postId == currentUserId }
    var likeState by remember { mutableStateOf(hasLiked) }

    var size by remember { mutableFloatStateOf(1.3f) }
    val scale by animateFloatAsState(
        animationSpec = spring(
            dampingRatio = 1f
        ),
        targetValue = size,
        finishedListener = { size = 1.3f },
        label = "like"
    )

    var moreSheetState by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    if (post.images.isNotEmpty()) {
        Surface(
            modifier = Modifier
                .animateContentSize(
                    animationSpec = tween(350)
                )
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
                            onClick = onUsernameClick
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Surface(
                        modifier = Modifier.size(30.dp),
                        shape = CircleShape,
                        color = Utils.IgOffBlack
                    ) {
                        if (post.profileImage.isNotEmpty()) {
                            AsyncImage(
                                modifier = Modifier.fillMaxSize(),
                                model = post.profileImage,
                                contentScale = ContentScale.Crop,
                                filterQuality = FilterQuality.Low,
                                contentDescription = post.username
                            )
                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.profile),
                                contentDescription = stringResource(id = R.string.profile_image)
                            )
                        }
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

                    // More icon
                    Box(
                        modifier = Modifier.weight(4f),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Icon(
                            modifier = Modifier
                                .clickable(
                                    indication = null,
                                    interactionSource = interactionSource,
                                    onClick = { moreSheetState = true }
                                ),
                            painter = painterResource(id = R.drawable.more2),
                            tint = Color.White,
                            contentDescription = stringResource(id = R.string.more)
                        )
                    }
                }

                AsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 200.dp),
                    model = post.images.first(), //TODO integrate Pager
                    contentScale = ContentScale.Crop,
                    filterQuality = FilterQuality.Low,
                    contentDescription = stringResource(R.string.post, post.username)
                )

                Row(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                        .wrapContentHeight(Alignment.CenterVertically),
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
                            if (likeState) {
                                Icon(
                                    modifier = Modifier
                                        .scale(scale)
                                        .padding(horizontal = 8.dp)
                                        .clickable(
                                            indication = null,
                                            interactionSource = interactionSource,
                                            onClick = {
                                                size = 1.6f
                                                likeState = !likeState
                                                onUnLikeClick()
                                            }
                                        ),
                                    painter = painterResource(id = R.drawable.heart_filled),
                                    tint = Utils.IgLikeRed,
                                    contentDescription = stringResource(R.string.like_unlike)
                                )
                            } else {
                                Icon(
                                    modifier = Modifier
                                        .scale(scale)
                                        .padding(horizontal = 8.dp)
                                        .clickable(
                                            indication = null,
                                            interactionSource = interactionSource,
                                            onClick = {
                                                size = 1.6f
                                                likeState = !likeState
                                                onLikeClick()
                                            }
                                        ),
                                    painter = painterResource(id = R.drawable.heart_outlined),
                                    tint = Color.White,
                                    contentDescription = stringResource(R.string.like_unlike)
                                )
                            }
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
                                contentDescription = stringResource(
                                    R.string.view_all_comments,
                                    post.comments.size
                                )
                            )
                            Icon(
                                modifier = Modifier
                                    .scale(1.1f)
                                    .padding(horizontal = 8.dp)
                                    .clickable(
                                        indication = null,
                                        interactionSource = interactionSource,
                                        onClick = onSendClick
                                    ),
                                painter = painterResource(id = R.drawable.send),
                                tint = Color.White,
                                contentDescription = stringResource(R.string.send)
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
                                onClick = onSaveClick
                            ),
                        painter = painterResource(id = R.drawable.save_outlined),
                        tint = Color.White,
                        contentDescription = stringResource(R.string.save)
                    )
                }
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp),
                    text = stringResource(R.string.likes, post.likes.size),
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
                        .wrapContentHeight(Alignment.CenterVertically)
                        .padding(vertical = 5.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    val annotatedString = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        ) {
                            append("${post.username} ")
                        }

                        withStyle(
                            style = SpanStyle(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Normal,
                                color = Color.White
                            )
                        ) {
                            append(post.caption)
                        }
                    }
                    Text(
                        modifier = Modifier
                            .padding(start = 15.dp, end = 5.dp),
                        text = annotatedString,
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
                        color = Color.White.copy(alpha = 0.6f),
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
                            onClick = { moreSheetState = true }
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Surface(
                        modifier = Modifier.size(30.dp),
                        shape = CircleShape,
                        color = Utils.IgOffBlack
                    ) {
                        if (post.profileImage.isNotEmpty()) {
                            AsyncImage(
                                modifier = Modifier.fillMaxSize(),
                                model = post.profileImage,
                                contentScale = ContentScale.Crop,
                                filterQuality = FilterQuality.Low,
                                contentDescription = post.username
                            )
                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.profile),
                                contentDescription = stringResource(id = R.string.profile_image)
                            )
                        }
                    }
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp),
                        text = stringResource(R.string.add_a_comment),
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.White.copy(alpha = 0.6f),
                            textAlign = TextAlign.Start
                        )
                    )
                }

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    text = post.timeStamp.formatTimeStamp(),
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White.copy(alpha = 0.5f),
                        textAlign = TextAlign.Start
                    )
                )
            }
        }
        IGBottomSheet(
            showSheet = moreSheetState,
            sheetState = sheetState,
            onDismiss = { moreSheetState = false }
        ) {
            if (post.userId != currentUserId) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 20.dp)
                        .clickable(
                            indication = null,
                            interactionSource = interactionSource,
                            onClick = onUnfollowClick
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        modifier = Modifier.size(18.dp),
                        painter = painterResource(id = R.drawable.unfollow),
                        tint = Color.White,
                        contentDescription = stringResource(id = R.string.unfollow)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = stringResource(id = R.string.unfollow),
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.White
                        )
                    )
                }
            }

            if (post.userId == currentUserId) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 20.dp)
                        .clickable(
                            indication = null,
                            interactionSource = interactionSource,
                            onClick = {
                                moreSheetState = false
                                onDeletePostClick(post)
                            }
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.delete),
                        tint = Utils.IgError,
                        contentDescription = stringResource(id = R.string.delete)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = stringResource(id = R.string.delete),
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            color = Utils.IgError
                        )
                    )
                }
            }
        }
    }
}

@Preview(
    apiLevel = 33,
    showBackground = true,
    backgroundColor = 0XFF000000
)
@Composable
fun PostCardPreview() {
    PostCard(
        post = Post(
            username = "pra_sidh_22",
            likes = listOf("1234", "4321"),
            caption = "Who you picking?",
            timeStamp = 5,
            images = listOf(""),
            isVerified = true
        ),
        currentUserId = "12345",
        onLikeClick = { },
        onUnLikeClick = { },
        onSendClick = { },
        onSaveClick = { },
        onUnfollowClick = { },
        onDeletePostClick = { },
        onUsernameClick = { },
    )
}