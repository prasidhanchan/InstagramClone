package com.instagramclone.auth.loginhelp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.instagramclone.auth.UiState
import com.instagramclone.ui.R
import com.instagramclone.ui.components.AccessAccountTopBar
import com.instagramclone.ui.components.IGDialog
import com.instagramclone.util.constants.Utils

@Composable
fun AccessAccountScreen(
    uiState: UiState,
    setDialog: (Boolean) -> Unit,
    popBack: () -> Unit,
    onSendEmailClicked: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Utils.IgBlack,
        topBar = { AccessAccountTopBar(popBack = popBack) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(15.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape),
                model = uiState.profileImage,
                contentScale = ContentScale.Crop,
                contentDescription = stringResource(id = R.string.profile_image)
            )
            Text(
                modifier = Modifier.padding(vertical = 10.dp, horizontal = 40.dp),
                text = uiState.username.toUpperCase(locale = Locale.current),
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            )
            HorizontalDivider(
                modifier = Modifier
                    .padding(top = 20.dp, end = 10.dp),
                thickness = 0.5.dp,
                color = Color.White.copy(alpha = 0.2f)
            )
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp),
                color = Utils.IgBlack
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onSendEmailClicked),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        modifier = Modifier.padding(horizontal = 5.dp),
                        painter = painterResource(id = R.drawable.email),
                        tint = Color.White,
                        contentDescription = stringResource(id = R.string.email)
                    )
                    Text(
                        modifier = Modifier.padding(horizontal = 5.dp),
                        text = stringResource(R.string.send_an_email),
                        style = TextStyle(
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                }
            }
            HorizontalDivider(
                modifier = Modifier
                    .padding(end = 10.dp),
                thickness = 0.5.dp,
                color = Color.White.copy(alpha = 0.2f)
            )
        }
        val subStringBefore = uiState.email.substringBefore(stringResource(R.string.gmail_com)).length
        IGDialog(
            modifier = Modifier.padding(horizontal = 20.dp),
            title = stringResource(R.string.email_sent),
            subTitle = stringResource(R.string.password_reset_message, uiState.email.substring(0, 1) + "*******" + uiState.email.substring(subStringBefore - 1, subStringBefore)),
            showDialog = uiState.showDialog,
            showBlueButton = true,
            showWhiteButton = false,
            button1Text = "",
            button2Text = stringResource(R.string.ok),
            onBlueClick = { setDialog(false) },
            onWhiteClick = {  }
        )
    }
}

@Preview(apiLevel = 33)
@Composable
fun AccessAccountScreenPreview() {
    AccessAccountScreen(
        uiState = UiState(email = "prasidh@gmail.com"),
        setDialog = {  },
        popBack = {  },
        onSendEmailClicked = {  }
    )
}