package com.instagramclone.profile.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.instagramclone.ui.R
import com.instagramclone.util.constants.Utils.IgLikeRed
import com.instagramclone.util.constants.Utils.IgOffBackground
import com.instagramclone.util.constants.Utils.IgOffColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IGBottomSheetProfile(
    profileImage: String,
    profileDescription: String,
    showSheet: Boolean,
    onNewProfileClick: () -> Unit,
    onDeleteProfileClick: () -> Unit,
    onDismiss: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            shape = RoundedCornerShape(
                topStart = 10.dp, topEnd = 10.dp
            ),
            containerColor = IgOffBackground,
            dragHandle = {
                BottomSheetDefaults.DragHandle(
                    width = 40.dp,
                    color = IgOffColor
                )
            }
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                AsyncImage(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape),
                    model = profileImage,
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center,
                    contentDescription = profileDescription
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(top = 10.dp, bottom = 15.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.onBackground
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .clickable(
                        indication = null,
                        interactionSource = interactionSource,
                        onClick = onNewProfileClick
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 20.dp),
                    painter = painterResource(id = R.drawable.gallery),
                    tint = MaterialTheme.colorScheme.onBackground,
                    contentDescription = stringResource(id = R.string.new_profile_picture)
                )
                Text(
                    text = stringResource(id = R.string.new_profile_picture),
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )
            }

            if (profileImage.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                        .clickable(
                            indication = null,
                            interactionSource = interactionSource,
                            onClick = onDeleteProfileClick
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 20.dp),
                        painter = painterResource(id = R.drawable.delete),
                        tint = IgLikeRed,
                        contentDescription = stringResource(R.string.remove_current_picture)
                    )
                    Text(
                        text = stringResource(id = R.string.remove_current_picture),
                        style = TextStyle(
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Normal,
                            color = IgLikeRed
                        )
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun IGBottomSheetProfilePreview() {
    IGBottomSheetProfile(
        profileImage = "",
        profileDescription = "",
        showSheet = true,
        onNewProfileClick = {  },
        onDeleteProfileClick = {  },
        onDismiss = {  }
    )
}