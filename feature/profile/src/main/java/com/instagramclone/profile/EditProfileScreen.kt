package com.instagramclone.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.instagramclone.ui.R
import com.instagramclone.ui.components.AccountsCenter
import com.instagramclone.ui.components.EditTextBox
import com.instagramclone.profile.components.IGBottomSheetProfile
import com.instagramclone.ui.components.IGLoader
import com.instagramclone.ui.components.IGRegularAppBar
import com.instagramclone.ui.components.IGWaitDialog
import com.instagramclone.util.constants.Utils

@Composable
fun EditProfileScreen(
    innerPadding: PaddingValues,
    uiState: UiState,
    onClickEditText: (String) -> Unit,
    onDeleteProfileClick: () -> Unit,
    setNewImage: (Uri) -> Unit,
    onUploadClicked: () -> Unit,
    onPasswordChange: (String) -> Unit,
    onNewPasswordChange: (String) -> Unit,
    onRePasswordChange: (String) -> Unit,
    onChangePasswordClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    var gallerySheetState by remember { mutableStateOf(false) }
    var showUpdateScreen by remember { mutableStateOf(false) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                showUpdateScreen = true
                gallerySheetState = false
                setNewImage(uri)
            }
        }
    )

    var sheetState by remember { mutableStateOf(false) }

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
                color = Utils.IgOffBackground
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
                    onClick = { gallerySheetState = true }
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

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 15.dp),
                thickness = 0.5.dp,
                color = Color.White.copy(alpha = 0.2f)
            )
            Text(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
                    .clickable(
                        indication = null,
                        interactionSource = interactionSource,
                        onClick = { sheetState = true }
                    ),
                text = stringResource(R.string.personal_information_settings),
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = Utils.IgBlue
                )
            )
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 15.dp),
                thickness = 0.5.dp,
                color = Color.White.copy(alpha = 0.2f)
            )
        }

        IGBottomSheetProfile(
            profileImage = uiState.profileImage,
            profileDescription = uiState.name,
            showSheet = gallerySheetState,
            onNewProfileClick = {
                galleryLauncher.launch(
                    PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
            },
            onDeleteProfileClick = {
                gallerySheetState = false
                onDeleteProfileClick()
            },
            onDismiss = { gallerySheetState = !gallerySheetState }
        )
        AnimatedVisibility(
            visible = showUpdateScreen,
            enter = slideInVertically(
                animationSpec = tween(durationMillis = 250),
                initialOffsetY = { it }
            ),
            exit = slideOutVertically(
                animationSpec = tween(durationMillis = 250),
                targetOffsetY = { it }
            )
        ) {
            UpdateProfileImageScreen(
                innerPadding = innerPadding,
                uiState = uiState,
                onNextClick = {
                    showUpdateScreen = false
                    onUploadClicked()
                },
                onCancelClick = { showUpdateScreen = false }
            )
        }

        IGWaitDialog(
            text = stringResource(id = R.string.loading),
            showDialog = uiState.isUpdating
        )

        AccountsCenter(
            email = uiState.email,
            phone = uiState.phone,
            password = uiState.passwordState,
            newPassword = uiState.newPasswordState,
            rePassword = uiState.rePasswordState,
            error = uiState.error ?: "",
            visible = sheetState,
            buttonLoading = uiState.isUpdating,
            onPasswordChange = onPasswordChange,
            onNewPasswordChange = onNewPasswordChange,
            onRePasswordChange = onRePasswordChange,
            onChangePasswordClick = onChangePasswordClick,
            onDismiss = { sheetState = false },
        )
    } else {
        IGLoader()
    }
}

@Preview(
    apiLevel = 33,
    showBackground = true
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
        onClickEditText = { },
        onDeleteProfileClick = { },
        setNewImage = { },
        onUploadClicked = { },
        onBackClick = { },
        onChangePasswordClick = { },
        onPasswordChange = { },
        onNewPasswordChange = { },
        onRePasswordChange = { },
    )
}