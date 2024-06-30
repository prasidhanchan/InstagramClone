package com.instagramclone.auth.signup

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.instagramclone.auth.UiState
import com.instagramclone.ui.R
import com.instagramclone.ui.components.IGButton
import com.instagramclone.ui.components.IGWaitDialog
import com.instagramclone.util.constants.Utils.IgBackground
import com.instagramclone.util.constants.Utils.IgBlue

@Composable
fun ProfileAddedScreen(
    uiState: UiState,
    navigateToHomeScreen: () -> Unit,
    onChangePhotoClicked: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = IgBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 45.dp, vertical = 40.dp)
                .padding(innerPadding),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier.size(100.dp),
                shape = CircleShape,
                border = BorderStroke(width = 2.dp, color = Color.White)
            ) {
                AsyncImage(
                    modifier = Modifier.fillMaxSize(),
                    model = uiState.profileImage,
                    contentDescription = stringResource(id = R.string.profile_image)
                )
            }
            Text(
                modifier = Modifier.padding(top = 40.dp, bottom = 5.dp),
                text = stringResource(id = R.string.profile_photo_added),
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                ),
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .clickable(onClick = onChangePhotoClicked),
                text = stringResource(R.string.change_photo),
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = IgBlue
                )
            )

            IGButton(
                modifier = Modifier.padding(top = 40.dp, bottom = 12.dp),
                text = stringResource(R.string.next),
                isLoading = false,
                onClick = navigateToHomeScreen
            )
        }
        IGWaitDialog(
            text = stringResource(R.string.please_wait),
            showDialog = uiState.showDialog
        )
    }
}

@Preview
@Composable
fun ProfileAddedScreenPreview() {
    ProfileAddedScreen(
        uiState = UiState(emailOrUsername = ""),
        navigateToHomeScreen = { },
        onChangePhotoClicked = { }
    )
}