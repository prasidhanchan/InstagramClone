package com.instagramclone.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.instagramclone.ui.R
import com.instagramclone.ui.components.AccountsCenter
import com.instagramclone.ui.components.IGRegularAppBar
import com.instagramclone.ui.components.MoreCard
import com.instagramclone.util.constants.Utils.IgError
import com.instagramclone.util.constants.Utils.IgOffColor

@Composable
fun SettingsAndPrivacyScreen(
    innerPadding: PaddingValues,
    uiState: UiState,
    onLogoutClick: () -> Unit,
    onPasswordChange: (String) -> Unit,
    onNewPasswordChange: (String) -> Unit,
    onRePasswordChange: (String) -> Unit,
    onChangePasswordClick: () -> Unit,
    onBackClick: () -> Unit
) {
    var showAccountCenter by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IGRegularAppBar(
            text = stringResource(com.instagramclone.profile.R.string.settings_and_privacy),
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(com.instagramclone.profile.R.string.your_account),
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = IgOffColor
                    ),
                    textAlign = TextAlign.Start
                )
                Icon(
                    painter = painterResource(id = R.drawable.meta),
                    tint = MaterialTheme.colorScheme.onBackground,
                    contentDescription = stringResource(R.string.meta)
                )
            }

            Row(
                modifier = Modifier
                    .padding(vertical = 20.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    modifier = Modifier.weight(1f),
                    painter = painterResource(id = R.drawable.profile_black),
                    tint = MaterialTheme.colorScheme.onBackground,
                    contentDescription = stringResource(R.string.accounts_center)
                )
                Column(
                    modifier = Modifier
                        .weight(8f)
                        .padding(horizontal = 15.dp)
                        .clickable(
                            onClick = { showAccountCenter = true }
                        )
                ) {
                    Text(
                        text = stringResource(R.string.accounts_center),
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            color = MaterialTheme.colorScheme.onBackground
                        ),
                        textAlign = TextAlign.Start
                    )
                    Text(
                        text = stringResource(R.string.accounts_center_message_settings),
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            color = IgOffColor
                        ),
                        textAlign = TextAlign.Start
                    )
                }
                Icon(
                    modifier = Modifier.weight(1f),
                    painter = painterResource(id = R.drawable.arrow_right),
                    tint = MaterialTheme.colorScheme.onBackground,
                    contentDescription = stringResource(R.string.open)
                )
            }

            Row(
                modifier = Modifier
                    .padding(vertical = 15.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.login),
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = IgOffColor
                    ),
                    textAlign = TextAlign.Start
                )
            }
            MoreCard(
                fontColor = IgError,
                title = stringResource(R.string.log_out),
                onClick = onLogoutClick
            )
        }
    }

    AccountsCenter(
        email = uiState.email,
        phone = uiState.phone,
        password = uiState.passwordState,
        newPassword = uiState.newPasswordState,
        rePassword = uiState.rePasswordState,
        error = uiState.error ?: "",
        visible = showAccountCenter,
        buttonLoading = uiState.isUpdating,
        onPasswordChange = onPasswordChange,
        onNewPasswordChange = onNewPasswordChange,
        onRePasswordChange = onRePasswordChange,
        onChangePasswordClick = onChangePasswordClick,
        onDismiss = { showAccountCenter = false }
    )
}

@Preview(
    apiLevel = 33,
    showBackground = true
)
@Composable
fun SettingsAndPrivacyScreenPreview() {
    SettingsAndPrivacyScreen(
        innerPadding = PaddingValues(),
        uiState = UiState(),
        onLogoutClick = { },
        onBackClick = { },
        onChangePasswordClick = { },
        onPasswordChange = { },
        onNewPasswordChange = { },
        onRePasswordChange = { },
    )
}