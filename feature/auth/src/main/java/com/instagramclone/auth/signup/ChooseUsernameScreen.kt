package com.instagramclone.auth.signup

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
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
import com.instagramclone.ui.components.IGTextBox
import com.instagramclone.util.constants.Utils.IgBackground
import com.instagramclone.util.constants.Utils.IgError

@Composable
fun ChooseUsernameScreen(
    uiState: UiState,
    onUserNameChange: (String) -> Unit,
    clearUsername: () -> Unit,
    onNextClicked: () -> Unit
) {
    val keyBoard = LocalSoftwareKeyboardController.current
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = IgBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 25.dp, vertical = 20.dp)
                .padding(innerPadding),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(vertical = 5.dp),
                text = stringResource(R.string.choose_username),
                style = TextStyle(
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
            Text(
                modifier = Modifier.padding(vertical = 8.dp),
                text = stringResource(R.string.you_can_change_it_later),
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                )
            )
            IGTextBox(
                modifier = Modifier.padding(top = 8.dp),
                value = uiState.username,
                onValueChange = onUserNameChange,
                placeHolder = stringResource(R.string.username),
                autoCorrect = false,
                isTrailingIconEnabled = true,
                isErrorOrSuccess = uiState.errorOrSuccessUsername,
                clearText = clearUsername,
                onConfirm = {
                    keyBoard?.hide()
                    onNextClicked()
                }
            )

            AnimatedVisibility(
                visible = uiState.errorOrSuccessUsername.isNotEmpty() &&
                        uiState.errorOrSuccessUsername != stringResource(id = R.string.available)
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    text = uiState.errorOrSuccessUsername,
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        color = IgError
                    ),
                    textAlign = TextAlign.Start
                )
            }

            IGButton(
                modifier = Modifier.padding(vertical = 14.dp),
                enabled = uiState.username.trim().isNotEmpty() && !uiState.isLoading,
                text = stringResource(R.string.next),
                isLoading = uiState.isLoading,
                onClick = onNextClicked
            )
        }
    }
}

@Preview
@Composable
fun ChooseUsernameScreenPreview() {
    ChooseUsernameScreen(
        uiState = UiState(errorOrSuccess = "A user with that username already exists."),
        onUserNameChange = { },
        clearUsername = { },
        onNextClicked = { }
    )
}