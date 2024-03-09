package com.instagramclone.auth.signup

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.instagramclone.auth.UiState
import com.instagramclone.ui.R
import com.instagramclone.ui.components.IGButton
import com.instagramclone.ui.components.IGTextBox
import com.instagramclone.util.constants.Utils

@Composable
fun AddEmailScreen(
    uiState: UiState,
    clearEmail: () -> Unit,
    onEmailChange: (String) -> Unit,
    navigateToLogin: () -> Unit,
    onNextClicked: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Utils.IgBlack
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp, vertical = 20.dp)
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            )

            Icon(
                modifier = Modifier.scale(2f),
                painter = painterResource(id = R.drawable.profile_pic),
                tint = Color.White,
                contentDescription = stringResource(R.string.profile_pic)
            )

            Text(
                modifier = Modifier.padding(top = 55.dp, bottom = 10.dp),
                text = stringResource(R.string.email_caps),
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White
                ),
                textAlign = TextAlign.Center
            )
            HorizontalDivider(thickness = 2.dp, color = Color.White)

            IGTextBox(
                modifier = Modifier.padding(top = 15.dp),
                value = uiState.email,
                onValueChange = onEmailChange,
                placeHolder = stringResource(R.string.email),
                autoCorrect = false,
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Email,
                isTrailingIconEnabled = true,
                isErrorOrSuccess = uiState.errorOrSuccessEmail,
                clearText = clearEmail,
                onConfirm = {  }
            )

            AnimatedVisibility(
                visible = uiState.errorOrSuccessEmail.isNotEmpty() &&
                        uiState.errorOrSuccessEmail != stringResource(id = R.string.available)
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    text = uiState.errorOrSuccessEmail,
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        color = Utils.IgError
                    ),
                    textAlign = TextAlign.Start
                )
            }

            IGButton(
                modifier = Modifier.padding(vertical = 14.dp),
                enabled = uiState.email.trim().isNotEmpty(),
                text = stringResource(R.string.next),
                isLoading = uiState.isLoading,
                onClick = onNextClicked
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1.2f),
                contentAlignment = Alignment.BottomCenter
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 18.dp),
                        color = Color.White.copy(alpha = 0.2f)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                indication = null,
                                interactionSource = interactionSource,
                                onClick = navigateToLogin
                            ),
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(R.string.already_have_an_account),
                            style = TextStyle(
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Normal,
                                color = Color.White.copy(alpha = 0.5f)
                            )
                        )
                        Text(
                            text = stringResource(R.string.log_in),
                            style = TextStyle(
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                    }
                }
            }
        }
    }
}

@Preview(apiLevel = 33)
@Composable
private fun AddEmailScreenPreview() {
    AddEmailScreen(
        uiState = UiState(email = ""),
        clearEmail = { },
        onEmailChange = { },
        navigateToLogin = { },
        onNextClicked = { }
    )
}