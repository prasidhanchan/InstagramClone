package com.instagramclone.auth.login

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
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
import com.instagramclone.ui.components.IGDialog
import com.instagramclone.ui.components.IGTextBox
import com.instagramclone.ui.components.IGTextBoxPassword
import com.instagramclone.util.constants.Utils.IgBackground
import com.instagramclone.util.constants.Utils.IgBlue

@Composable
fun LoginScreen(
    uiState: UiState,
    navigateToSignUp: () -> Unit,
    onForgotPasswordClicked: () -> Unit,
    onFaceBookClicked: () -> Unit,
    onEmailOrUserNameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    onLogin: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val focusRequesterPassword = FocusRequester()
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
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            )

            Icon(
                modifier = Modifier.padding(vertical = 20.dp),
                painter = painterResource(id = R.drawable.instagram_logo),
                tint = MaterialTheme.colorScheme.onBackground,
                contentDescription = stringResource(R.string.logo)
            )
            IGTextBox(
                modifier = Modifier.padding(vertical = 8.dp),
                value = uiState.emailOrUsername,
                onValueChange = onEmailOrUserNameChange,
                placeHolder = stringResource(R.string.email_or_username),
                autoCorrect = false,
                onConfirm = { focusRequesterPassword.requestFocus() }
            )
            IGTextBoxPassword(
                modifier = Modifier.padding(vertical = 8.dp),
                value = uiState.password,
                onValueChange = onPasswordChange,
                placeHolder = stringResource(R.string.password),
                autoCorrect = false,
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done,
                focusRequester = focusRequesterPassword,
                isTrailingIconEnabled = true,
                onConfirm = {
                    focusRequesterPassword.freeFocus()
                    keyBoard?.hide()
                }
            )

            IGButton(
                modifier = Modifier.padding(vertical = 8.dp),
                enabled = uiState.emailOrUsername.isNotEmpty() &&
                        uiState.password.isNotEmpty() &&
                        !uiState.isLoading,
                text = stringResource(R.string.log_in_placeholder),
                isLoading = uiState.isLoading,
                onClick = onLogin
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
                    .clickable(
                        indication = null,
                        interactionSource = interactionSource,
                        onClick = onForgotPasswordClicked
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.forgot_your_login_details),
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                    )
                )
                Text(
                    text = stringResource(R.string.get_help_logging_in),
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )
            }
            /** Divider OR section */
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)
                )
                Text(
                    modifier = Modifier.weight(0.2f),
                    text = stringResource(R.string.or),
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    ),
                    textAlign = TextAlign.Center
                )
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)
                )
            }

            /** Facebook Login section */
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    modifier = Modifier.padding(horizontal = 5.dp),
                    painter = painterResource(id = R.drawable.facebook),
                    tint = IgBlue,
                    contentDescription = stringResource(id = R.string.facebook_login)
                )
                Text(
                    modifier = Modifier.clickable(onClick = onFaceBookClicked),
                    text = stringResource(R.string.log_in_with_facebook),
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = IgBlue
                    ),
                    textAlign = TextAlign.Center
                )
            }

            /** Bottom section */
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.BottomCenter
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 18.dp),
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                indication = null,
                                interactionSource = interactionSource,
                                onClick = navigateToSignUp
                            ),
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(R.string.don_t_have_an_account),
                            style = TextStyle(
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Normal,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                            )
                        )
                        Text(
                            text = stringResource(R.string.sign_up),
                            style = TextStyle(
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        )
                    }
                }
            }
        }

        if (uiState.errorTitle == stringResource(id = R.string.incorrect_password)) {
            IGDialog(
                title = uiState.errorTitle,
                subTitle = uiState.errorSubTitle,
                showDialog = uiState.showDialog,
                showBlueOrRedButton = false,
                button1Text = stringResource(id = R.string.ok),
                onBlueOrRedClick = { },
                onWhiteClick = onDismiss
            )
        } else {
            IGDialog(
                title = uiState.errorTitle,
                subTitle = uiState.errorSubTitle,
                showDialog = uiState.showDialog,
                showBlueOrRedButton = true,
                button1Text = stringResource(id = R.string.sign_up_dialog),
                button2Text = stringResource(R.string.try_again),
                onBlueOrRedClick = onDismiss,
                onWhiteClick = onConfirm
            )
        }
        if (uiState.showDialog) keyBoard?.hide()
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    LoginScreen(
        uiState = UiState(
            emailOrUsername = "Prasidh",
            password = "12345"
        ),
        navigateToSignUp = { },
        onForgotPasswordClicked = { },
        onFaceBookClicked = { },
        onEmailOrUserNameChange = { },
        onPasswordChange = { },
        onConfirm = { },
        onDismiss = { },
        onLogin = { }
    )
}