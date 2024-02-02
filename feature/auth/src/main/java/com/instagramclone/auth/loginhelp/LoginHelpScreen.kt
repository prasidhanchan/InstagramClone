package com.instagramclone.auth.loginhelp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.instagramclone.auth.UiState
import com.instagramclone.ui.R
import com.instagramclone.ui.components.IGButton
import com.instagramclone.ui.components.IGFacebookButton
import com.instagramclone.ui.components.IGTextBox
import com.instagramclone.ui.components.IGWaitDialog
import com.instagramclone.util.constants.Utils

@Composable
fun LoginHelpScreen(
    uiState: UiState,
    onValueChange: (String) -> Unit,
    onSuccess: () -> Unit,
    onError: (Exception) -> Unit,
    onNextClicked: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Utils.IgBlack,
        topBar = {
            Row(modifier = Modifier.padding(top = 40.dp, start = 20.dp, bottom = 20.dp)) {
                Text(
                    text = stringResource(R.string.login_help),
                    style = TextStyle(
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(30.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(top = 30.dp),
                text = stringResource(R.string.find_your_account),
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp),
                text = stringResource(R.string.enter_your_username_or_email),
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White.copy(alpha = 0.5f),
                    textAlign = TextAlign.Center
                )
            )

            IGTextBox(
                modifier = Modifier.padding(top = 40.dp),
                value = uiState.emailOrUsername,
                onValueChange = onValueChange,
                placeHolder = stringResource(R.string.username_or_email),
                imeAction = ImeAction.Go,
                autoCorrect = false,
                onConfirm = { onNextClicked() }
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
                modifier = Modifier.padding(vertical = 20.dp),
                text = stringResource(id = R.string.next),
                isLoading = uiState.isLoading,
                enabled = uiState.emailOrUsername.isNotEmpty() && !uiState.isLoading,
                onClick = onNextClicked
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Divider(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 10.dp),
                    thickness = 0.5.dp,
                    color = Color.White.copy(alpha = 0.2f)
                )
                Text(
                    text = stringResource(id = R.string.or),
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White.copy(alpha = 0.5f)
                    )
                )
                Divider(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 10.dp),
                    thickness = 0.5.dp,
                    color = Color.White.copy(alpha = 0.2f)
                )
            }
            IGFacebookButton(
                modifier = Modifier.padding(vertical = 10.dp),
                text = stringResource(id = R.string.log_in_with_facebook),
                onSuccess = onSuccess,
                onError = onError
            )
        }
        IGWaitDialog(
            text = stringResource(id = R.string.loading),
            showDialog = uiState.showDialog
        )
    }
}

@Preview(apiLevel = 33)
@Composable
fun LoginHelpScreenPreview() {
    LoginHelpScreen(
        uiState = UiState(),
        onValueChange = {  },
        onSuccess = {  },
        onError = {  },
        onNextClicked = {  }
    )
}