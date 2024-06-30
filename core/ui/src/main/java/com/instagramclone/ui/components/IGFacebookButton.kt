package com.instagramclone.ui.components

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.facebook.CallbackManager
import com.facebook.login.LoginManager
import com.instagramclone.ui.R
import com.instagramclone.util.constants.FacebookLogin
import com.instagramclone.util.constants.Utils

@Composable
fun IGFacebookButton(
    modifier: Modifier = Modifier,
    text: String,
    cornerRadius: Dp = 8.dp,
    onSuccess: () -> Unit,
    onError: (Exception) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val context = LocalContext.current
    val loginManager = LoginManager.getInstance()
    val callBack = remember { CallbackManager.Factory.create() }
    val launcher = rememberLauncherForActivityResult(
        contract = loginManager.createLogInActivityResultContract(callBack),
        onResult = {  }
    )
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp),
        color = Utils.IgButtonColor,
        shape = RoundedCornerShape(cornerRadius)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    indication = null,
                    interactionSource = interactionSource,
                    onClick = {
                        launcher.launch(listOf("email", "public_profile"))
                    }
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                modifier = Modifier.scale(0.7f),
                painter = painterResource(id = R.drawable.facebook),
                tint = Color.White,
                contentDescription = stringResource(R.string.facebook_login)
            )
            Text(
                text = text,
                style = TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            )
        }
    }

    FacebookLogin(
        loginManager = loginManager,
        callBack = callBack,
        context = context,
        onSuccess = onSuccess,
        onError = onError
    )

}

@Preview
@Composable
fun IGFacebookButtonPreview() {
    IGFacebookButton(
        text = "Log in with Facebook",
        onSuccess = { },
        onError = { }
    )
}