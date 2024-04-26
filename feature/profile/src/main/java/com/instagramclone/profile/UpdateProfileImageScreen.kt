package com.instagramclone.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
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
import com.instagramclone.ui.components.IGRegularAppBar
import com.instagramclone.util.constants.Utils

@Composable
fun UpdateProfileImageScreen(
    innerPadding: PaddingValues,
    uiState: UiState,
    onNextClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
            .background(color = Utils.IgBackground)
    ) {
        IGRegularAppBar(
            text = "Update profile picture",
            leadingIcon = R.drawable.cross,
            fonSize = 18,
            trailingIcon = {
                Text(
                    modifier = Modifier.clickable(onClick = onNextClick),
                    text = stringResource(id = R.string.next),
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Utils.IgBlue
                    )
                )
            },
            onBackClick = onCancelClick
        )

        AsyncImage(
            modifier = Modifier
                .padding(vertical = 10.dp)
                .wrapContentSize(Alignment.TopCenter)
                .fillMaxHeight(0.6f),
            model = uiState.newProfileImage,
            contentScale = ContentScale.Crop,
            contentDescription = stringResource(id = R.string.profile_image)
        )
    }
}

@Preview(
    apiLevel = 33,
    showBackground = true,
    backgroundColor = 0XFF000000
)
@Composable
fun UpdateProfileImageScreenPreview() {
    UpdateProfileImageScreen(
        innerPadding = PaddingValues(),
        uiState = UiState(
            isUpdating = true
        ),
        onNextClick = { },
        onCancelClick = { }
    )
}