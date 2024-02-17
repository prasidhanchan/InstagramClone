package com.instagramclone.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.instagramclone.ui.R
import com.instagramclone.ui.components.EditTextBox
import com.instagramclone.ui.components.IGLoader
import com.instagramclone.ui.components.IGRegularAppBar
import com.instagramclone.util.constants.Utils

@Composable
fun EditProfileScreen(
    innerPadding: PaddingValues,
    uiState: UiState,
    onClickEditText: (String) -> Unit,
    onBackClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    if (!uiState.isLoading) {
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IGRegularAppBar(
                text = stringResource(id = R.string.edit_profile),
                onBackClick = onBackClick
            )

            Surface(
                modifier = Modifier
                    .padding(vertical = 20.dp)
                    .size(80.dp),
                shape = CircleShape,
                color = Utils.IgOffBlack
            ) {
                if (uiState.profileImage.isNotEmpty()) {
                    AsyncImage(
                        modifier = Modifier.fillMaxSize(),
                        model = uiState.profileImage,
                        contentDescription = stringResource(id = R.string.profile_image)
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.profile),
                        contentDescription = stringResource(id = R.string.profile_image)
                    )
                }
            }

            Text(
                modifier = Modifier.clickable(
                    indication = null,
                    interactionSource = interactionSource,
                    onClick = { /*TODO open gallery */ }
                ),
                text = stringResource(R.string.edit_profile_picture),
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Utils.IgBlue
                )
            )

            EditTextBox(
                text = stringResource(R.string.name),
                value = uiState.name,
                onClick = onClickEditText
            )
            EditTextBox(
                text = stringResource(id = R.string.username),
                value = uiState.username,
                onClick = onClickEditText
            )
            EditTextBox(
                text = stringResource(R.string.bio),
                value = uiState.bio,
                onClick = onClickEditText
            )
            EditTextBox(
                text = stringResource(R.string.links),
                value = uiState.links,
                onClick = onClickEditText
            )
            EditTextBox(
                text = stringResource(R.string.gender),
                value = uiState.gender,
                onClick = onClickEditText
            )

            Divider(
                modifier = Modifier.padding(vertical = 15.dp),
                thickness = 0.5.dp,
                color = Color.White.copy(alpha = 0.2f)
            )
            Text(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
                    .clickable(onClick = { /*TODO open gallery */ }),
                text = stringResource(R.string.personal_information_settings),
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = Utils.IgBlue
                )
            )
            Divider(
                modifier = Modifier.padding(vertical = 15.dp),
                thickness = 0.5.dp,
                color = Color.White.copy(alpha = 0.2f)
            )
        }
    } else {
        IGLoader()
    }
}

@Preview(
    apiLevel = 33,
    showBackground = true,
    backgroundColor = 0xFF000000
)
@Composable
fun EditProfileScreenPreview() {
    EditProfileScreen(
        innerPadding = PaddingValues(),
        uiState = UiState(
            name = "Prasidh Gopal Anchan",
            username = "pra_sidh_22",
            bio = "Android developer"
        ),
        onClickEditText = {  },
        onBackClick = {  }
    )
}