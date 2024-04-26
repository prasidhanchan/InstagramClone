package com.instagramclone.auth.signup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.instagramclone.ui.components.IGWaitDialog
import com.instagramclone.util.constants.Utils.IgBackground

@Composable
fun WelcomeScreen(
    uiState: UiState,
    onCompleteSignUpClicked: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = IgBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp, vertical = 20.dp)
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(vertical = 5.dp),
                text = stringResource(
                    R.string.welcome_to_instagram,
                    uiState.username
                ),
                style = TextStyle(
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onBackground
                ),
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier.padding(vertical = 8.dp),
                text = stringResource(R.string.welcome_message),
                style = TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                ),
                textAlign = TextAlign.Center
            )

            IGButton(
                modifier = Modifier.padding(vertical = 12.dp),
                text = stringResource(R.string.complete_sign_up),
                isLoading = false,
                onClick = onCompleteSignUpClicked
            )
        }
        IGWaitDialog(
            text = stringResource(R.string.registering),
            showDialog = uiState.showDialog
        )
    }
}

@Preview(apiLevel = 33)
@Composable
fun WelcomeScreenPreview() {
    WelcomeScreen(
        uiState = UiState(username = "Prasidh"),
        onCompleteSignUpClicked = { }
    )
}