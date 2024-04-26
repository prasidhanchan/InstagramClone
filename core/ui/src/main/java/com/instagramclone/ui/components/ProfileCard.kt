package com.instagramclone.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
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
import com.instagramclone.util.constants.Utils.IgBlue
import com.instagramclone.util.constants.Utils.IgButtonColor
import com.instagramclone.util.constants.Utils.IgLinkBlue
import com.instagramclone.util.constants.Utils.IgOffBackground

@Composable
fun ProfileCard(
    myProfile: Boolean = true,
    profileImage: String,
    name: String,
    bio: String,
    links: String,
    posts: Int,
    followers: Int,
    following: Int,
    isFollowing: Boolean = false,
    onFollowClick: () -> Unit,
    onUnFollowClick: () -> Unit,
    onMessageClick: () -> Unit = { },
    onEditProfileClick: () -> Unit = { },
) {
    val uriHandler = LocalUriHandler.current
    val dynamicButtonColor = IgButtonColor
    var buttonColor: Color by remember(isFollowing) {
        mutableStateOf(if (isFollowing) dynamicButtonColor else IgBlue)
    }

    Column(
        modifier = Modifier
            .padding(vertical = 15.dp)
            .animateContentSize()
            .wrapContentHeight(Alignment.Top)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Surface(
                modifier = Modifier.size(80.dp),
                shape = CircleShape,
                color = IgOffBackground
            ) {
                if (profileImage.isNotEmpty()) {
                    AsyncImage(
                        modifier = Modifier.fillMaxSize(),
                        model = profileImage,
                        contentScale = ContentScale.Crop,
                        contentDescription = stringResource(id = R.string.profile_image)
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.profile),
                        contentDescription = stringResource(id = R.string.profile_image)
                    )
                }
            }

            /** Posts */
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = posts.toString(),
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )
                Text(
                    text = stringResource(R.string.posts),
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )
            }

            /** Followers */
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = followers.toString(),
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )
                Text(
                    text = stringResource(R.string.followers),
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )
            }

            /** Following */
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = following.toString(),
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )
                Text(
                    text = stringResource(R.string.following),
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )
            }
        }
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp, vertical = 5.dp),
            text = name,
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        if (bio.isNotEmpty()) {
            Text(
                modifier = Modifier
                    .padding(horizontal = 15.dp)
                    .fillMaxWidth(),
                text = bio,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Start
                ),
                maxLines = 4,
                overflow = TextOverflow.Ellipsis
            )
        }
        if (links.isNotEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    modifier = Modifier.padding(start = 15.dp),
                    painter = painterResource(id = R.drawable.link),
                    tint = IgLinkBlue,
                    contentDescription = stringResource(R.string.link)
                )
                Text(
                    modifier = Modifier
                        .padding(horizontal = 5.dp)
                        .clickable(
                            onClick = { uriHandler.openUri(links) }
                        ),
                    text = links.substringAfter("www."),
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = IgLinkBlue
                    ),
                    textAlign = TextAlign.Start
                )
            }
        }

        /** Buttons */
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (myProfile) {
                IGButton(
                    modifier = Modifier
                        .height(35.dp)
                        .weight(1f),
                    fontSize = 14,
                    color = IgButtonColor,
                    fontColorEnabled = MaterialTheme.colorScheme.onBackground,
                    fontColorDisabled = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                    text = stringResource(R.string.edit_profile),
                    isLoading = false,
                    onClick = onEditProfileClick
                )
            } else {
                IGButton(
                    modifier = Modifier
                        .padding(end = 4.dp)
                        .height(35.dp)
                        .weight(1f),
                    fontSize = 14,
                    color = buttonColor,
                    fontColorEnabled = if (isFollowing)
                        MaterialTheme.colorScheme.onBackground else Color.White,
                    fontColorDisabled = if (isFollowing)
                        MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f) else
                            Color.White.copy(alpha = 0.5f),
                    text = if (isFollowing) stringResource(R.string.following) else stringResource(R.string.follow),
                    isLoading = false,
                    onClick = {
                        buttonColor = dynamicButtonColor
                        if (isFollowing) onUnFollowClick() else onFollowClick()
                    }
                )
                IGButton(
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .height(35.dp)
                        .weight(1f),
                    fontSize = 14,
                    color = IgButtonColor,
                    fontColorEnabled = MaterialTheme.colorScheme.onBackground,
                    fontColorDisabled = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                    text = stringResource(R.string.message),
                    isLoading = false,
                    onClick = onMessageClick
                )
            }
        }
    }
}

@Preview(
    apiLevel = 33,
    showBackground = true
)
@Composable
private fun ProfileCardPreview() {
    ProfileCard(
        myProfile = true,
        profileImage = "",
        name = "Prasidh Gopal Anchan",
        bio = "Android developer",
        links = "linktr.ee/prasidhanchan",
        posts = 5,
        followers = 100,
        following = 80,
        isFollowing = false,
        onFollowClick = { },
        onUnFollowClick = { },
        onMessageClick = { },
    )
}