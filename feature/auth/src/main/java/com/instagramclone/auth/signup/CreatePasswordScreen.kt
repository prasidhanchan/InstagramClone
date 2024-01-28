package com.instagramclone.auth.signup

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.instagramclone.auth.UiState
import com.instagramclone.ui.R
import com.instagramclone.ui.components.IGButton
import com.instagramclone.ui.components.IGTextBoxPassword
import com.instagramclone.util.constants.Utils

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CreatePasswordScreen(
    uiState: UiState,
    onPasswordChange: (String) -> Unit,
    onNextClicked: () -> Unit
) {
    val keyBoard = LocalSoftwareKeyboardController.current
    val focusRequester = FocusRequester()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Utils.IgBlack
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp, vertical = 20.dp)
                .padding(innerPadding),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(vertical = 5.dp),
                text = stringResource(R.string.create_password),
                style = TextStyle(
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White
                )
            )
            Text(
                modifier = Modifier.padding(vertical = 8.dp),
                text = stringResource(R.string.your_password_must_be),
                style = TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White.copy(alpha = 0.5f)
                ),
                textAlign = TextAlign.Center
            )
            IGTextBoxPassword(
                modifier = Modifier.padding(top = 8.dp),
                value = uiState.password,
                onValueChange = onPasswordChange,
                keyboardType = KeyboardType.Password,
                focusRequester = focusRequester,
                placeHolder = stringResource(R.string.password),
                autoCorrect = false,
                onConfirm = {
                    keyBoard?.hide()
                    onNextClicked()
                }
            )

            AnimatedVisibility(
                visible = uiState.errorOrSuccess.isNotEmpty() &&
                        uiState.errorOrSuccess != stringResource(id = R.string.available)
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    text = uiState.errorOrSuccess,
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        color = Utils.IgError
                    ),
                    textAlign = TextAlign.Start
                )
            }

            IGButton(
                modifier = Modifier.padding(vertical = 12.dp),
                enabled = uiState.password.length >= 6 && !uiState.isLoading,
                text = stringResource(R.string.next),
                isLoading = uiState.isLoading,
                onClick = onNextClicked
            )
        }
    }
    LaunchedEffect(key1 = Unit) {
        focusRequester.requestFocus()
    }
}

@Preview(apiLevel = 33)
@Composable
fun CreatePasswordPreview() {
    CreatePasswordScreen(
        uiState = UiState(),
        onPasswordChange = { },
        onNextClicked = { }
    )
}