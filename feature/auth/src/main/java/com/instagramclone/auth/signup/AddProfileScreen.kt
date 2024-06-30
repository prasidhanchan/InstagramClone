package com.instagramclone.auth.signup

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.instagramclone.auth.UiState
import com.instagramclone.ui.R
import com.instagramclone.ui.components.IGButton
import com.instagramclone.ui.components.IGDialog
import com.instagramclone.util.constants.Utils.IgBackground
import com.instagramclone.util.constants.Utils.IgBlue

@Composable
fun AddProfileScreen(
    uiState: UiState,
    setProfileImage: (Uri) -> Unit,
    setDialog: (Boolean) -> Unit,
    navigateToProfileAddedScreen: () -> Unit,
    onSkipClicked: () -> Unit
) {
    val activityLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                setProfileImage(uri)
                navigateToProfileAddedScreen()
            }
        }
    )
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
            Icon(
                painter = painterResource(id = R.drawable.add_photo),
                tint = MaterialTheme.colorScheme.onBackground,
                contentDescription = stringResource(R.string.add_profile)
            )
            Text(
                modifier = Modifier.padding(top = 40.dp, bottom = 5.dp),
                text = stringResource(id = R.string.add_profile_photo),
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                ),
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth(0.9f),
                text = stringResource(R.string.add_profile_photo_message),
                style = TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                ),
                textAlign = TextAlign.Center
            )

            IGButton(
                modifier = Modifier.padding(top = 40.dp, bottom = 12.dp),
                text = stringResource(R.string.add_a_photo),
                isLoading = false,
                onClick = { setDialog(true) }
            )
            Text(
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .clickable(onClick = onSkipClicked),
                text = stringResource(R.string.skip),
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = IgBlue
                )
            )
        }
        IGDialog(
            title = stringResource(R.string.change_profile_photo),
            showDialog = uiState.showDialog,
            showBlueOrRedButton = true,
            button1Text = stringResource(R.string.cancel),
            button2Text = stringResource(R.string.choose_from_library),
            onBlueOrRedClick = {
                activityLauncher.launch(
                    PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
            },
            onWhiteClick = { setDialog(false) }
        )
    }
}

@Preview
@Composable
fun AddProfileScreenPreview() {
    AddProfileScreen(
        uiState = UiState(emailOrUsername = ""),
        setProfileImage = { },
        setDialog = { },
        navigateToProfileAddedScreen = { },
        onSkipClicked = { }
    )
}