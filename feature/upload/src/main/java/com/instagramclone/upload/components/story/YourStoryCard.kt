package com.instagramclone.upload.components.story

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.instagramclone.ui.R
import com.instagramclone.util.constants.Utils.IgBackground
import com.instagramclone.util.constants.Utils.IgOffBackground

/**
 * Your Story card composable card to display a Your Story button.
 * @param profileImage The profile image of the user.
 * @param modifier The modifier to be applied to the card.
 * @param onYourStoryClick The callback function to be invoked when the card is clicked.
 */
@Composable
fun YourStoryCard(
    profileImage: String,
    modifier: Modifier = Modifier,
    onYourStoryClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .padding(all = 20.dp)
            .height(50.dp)
            .fillMaxWidth()
            .clickable(
                onClick = onYourStoryClick
            ),
        shape = RoundedCornerShape(25.dp),
        color = IgOffBackground
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            ProfileImage(profileImage = profileImage)

            Text(
                text = stringResource(id = R.string.your_story),
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    }
}

@Composable
fun ProfileImage(
    profileImage: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .padding(horizontal = 10.dp)
            .size(30.dp),
        shape = CircleShape,
        border = BorderStroke(width = 2.dp, color = MaterialTheme.colorScheme.onBackground),
        color = IgBackground
    ) {
        AsyncImage(
            model = profileImage,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            contentDescription = stringResource(id = R.string.profileimage)
        )
    }
}

@Preview
@Composable
private fun YourStoryCardPreview() {
    YourStoryCard(
        profileImage = "",
        onYourStoryClick = { }
    )
}