package com.instagramclone.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.instagramclone.ui.R
import com.instagramclone.util.constants.Utils

enum class AccountsCenterState {
    MAIN,
    PASSWORD_AND_SECURITY,
    PERSONAL_DETAILS,
    CHANGE_PASSWORD
}

@Composable
fun AccountsCenter(
    email: String,
    phone: String,
    password: String,
    newPassword: String,
    rePassword: String,
    error: String,
    visible: Boolean,
    buttonLoading: Boolean,
    onPasswordChange: (String) -> Unit,
    onNewPasswordChange: (String) -> Unit,
    onRePasswordChange: (String) -> Unit,
    onChangePasswordClick: () -> Unit,
    onDismiss: () -> Unit
) {
    var startDestination by remember { mutableStateOf(AccountsCenterState.MAIN) }

    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            animationSpec = tween(durationMillis = 300),
            initialOffsetY = { it }
        ),
        exit = slideOutVertically(
            animationSpec = tween(durationMillis = 300),
            targetOffsetY = { it }
        )
    ) {
        Surface(
            modifier = Modifier
                .padding(top = 40.dp, bottom = 20.dp, start = 5.dp, end = 5.dp)
                .fillMaxSize(),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF383131),
                                Color(0xFF261B38),
                                Color(0xFF261B38),
                                Color(0xFF261B38),
                                Color(0xFF383131)
                            )
                        )
                    )
                    .padding(all = 15.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                MainScreenContent(
                    visible = startDestination == AccountsCenterState.MAIN,
                    onClick = { startDestination = it },
                    onDismiss = onDismiss
                )
                PasswordAndSecurityContent(
                    visible = startDestination == AccountsCenterState.PASSWORD_AND_SECURITY,
                    onPasswordChangeClick = { startDestination = AccountsCenterState.CHANGE_PASSWORD },
                    onDismiss = {
                        startDestination = AccountsCenterState.MAIN
                    }
                )
                PersonalDetailsContent(
                    visible = startDestination == AccountsCenterState.PERSONAL_DETAILS,
                    email = email,
                    phone = phone,
                    onDismiss = {
                        startDestination = AccountsCenterState.MAIN
                    }
                )

                ChangePasswordContent(
                    password = password,
                    newPassword = newPassword,
                    rePassword = rePassword,
                    error = error,
                    visible = startDestination == AccountsCenterState.CHANGE_PASSWORD,
                    buttonLoading  = buttonLoading,
                    onPasswordChange = onPasswordChange,
                    onNewPasswordChange = onNewPasswordChange,
                    onRePasswordChange = onRePasswordChange,
                    onButtonClick = onChangePasswordClick,
                    onDismiss = { startDestination = AccountsCenterState.PASSWORD_AND_SECURITY }
                )
            }
        }
    }
    BackHandler(
        enabled = visible,
        onBack = onDismiss
    )
}

@Composable
fun MainScreenContent(
    visible: Boolean,
    onClick: (AccountsCenterState) -> Unit,
    onDismiss: () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInHorizontally(
            animationSpec = tween(durationMillis = 300),
            initialOffsetX = { -it }
        ),
        exit = slideOutHorizontally(
            animationSpec = tween(durationMillis = 300),
            targetOffsetX = { -it }
        )
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            DragHandle(
                icon = painterResource(id = R.drawable.cross),
                onDismiss = onDismiss
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.accounts_center),
                style = TextStyle(
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                ),
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .fillMaxWidth(),
                text = stringResource(R.string.accounts_center_message),
                style = TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White
                ),
                textAlign = TextAlign.Center
            )

            Text(
                modifier = Modifier
                    .padding(vertical = 15.dp)
                    .fillMaxWidth(),
                text = stringResource(R.string.account_settings),
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
            AccountCenterCard(
                cardText = stringResource(R.string.password_and_security),
                icon = painterResource(id = R.drawable.shield),
                cornerRadius = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
                onClick = { onClick(AccountsCenterState.PASSWORD_AND_SECURITY) }
            )
            AccountCenterCard(
                cardText = stringResource(R.string.personal_details),
                icon = painterResource(id = R.drawable.personal_details),
                cornerRadius = RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp),
                onClick = { onClick(AccountsCenterState.PERSONAL_DETAILS) }
            )
        }
    }
}

@Composable
fun PersonalDetailsContent(
    visible: Boolean,
    email: String,
    phone: String,
    onDismiss: () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInHorizontally(
            animationSpec = tween(durationMillis = 300),
            initialOffsetX = { it }
        ),
        exit = slideOutHorizontally(
            animationSpec = tween(durationMillis = 300),
            targetOffsetX = { it }
        )
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            DragHandle(
                modifier = Modifier.scale(0.6f),
                showMetaLogo = false,
                icon = painterResource(id = R.drawable.arrow_back),
                onDismiss = onDismiss
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.personal_details),
                style = TextStyle(
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                ),
                textAlign = TextAlign.Start
            )
            Text(
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .fillMaxWidth(),
                text = stringResource(R.string.personal_details_message),
                style = TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White
                ),
                textAlign = TextAlign.Start
            )

            AccountCenterCard(
                modifier = Modifier.padding(top = 10.dp),
                cardText = email,
                icon = painterResource(id = R.drawable.email),
                trailingIcon = false,
                cornerRadius = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
                onClick = { }
            )
            AccountCenterCard(
                cardText = phone.ifEmpty { stringResource(R.string.no_phone_number_added) },
                icon = painterResource(id = R.drawable.phone),
                trailingIcon = false,
                cornerRadius = RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp),
                onClick = { }
            )
        }
    }
}

@Composable
fun PasswordAndSecurityContent(
    visible: Boolean,
    onPasswordChangeClick: () -> Unit,
    onDismiss: () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInHorizontally(
            animationSpec = tween(durationMillis = 300),
            initialOffsetX = { -it }
        ),
        exit = slideOutHorizontally(
            animationSpec = tween(durationMillis = 300),
            targetOffsetX = { -it }
        )
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            DragHandle(
                modifier = Modifier.scale(0.6f),
                showMetaLogo = false,
                icon = painterResource(id = R.drawable.arrow_back),
                onDismiss = onDismiss
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.password_and_security),
                style = TextStyle(
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                ),
                textAlign = TextAlign.Start
            )
            Text(
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .fillMaxWidth(),
                text = stringResource(R.string.password_and_security_message),
                style = TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White
                ),
                textAlign = TextAlign.Start
            )

            AccountCenterCard(
                modifier = Modifier.padding(vertical = 10.dp),
                cardText = stringResource(R.string.change_password),
                icon = painterResource(id = R.drawable.lock),
                cornerRadius = RoundedCornerShape(10.dp),
                onClick = onPasswordChangeClick
            )
        }
    }
}

@Composable
fun ChangePasswordContent(
    password: String,
    newPassword: String,
    rePassword: String,
    error: String,
    visible: Boolean,
    buttonLoading: Boolean,
    onPasswordChange: (String) -> Unit,
    onNewPasswordChange: (String) -> Unit,
    onRePasswordChange: (String) -> Unit,
    onButtonClick: () -> Unit,
    onDismiss: () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInHorizontally(
            animationSpec = tween(durationMillis = 300),
            initialOffsetX = { it }
        ),
        exit = slideOutHorizontally(
            animationSpec = tween(durationMillis = 300),
            targetOffsetX = { it }
        )
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            DragHandle(
                modifier = Modifier.scale(0.6f),
                showMetaLogo = false,
                icon = painterResource(id = R.drawable.arrow_back),
                onDismiss = onDismiss
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.change_password),
                style = TextStyle(
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                ),
                textAlign = TextAlign.Start
            )
            Text(
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .fillMaxWidth(),
                text = stringResource(R.string.change_password_message),
                style = TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White
                ),
                textAlign = TextAlign.Start
            )

            IGTextBoxPassword(
                modifier = Modifier.padding(vertical = 10.dp),
                value = password,
                isTrailingIconEnabled = true,
                onValueChange = onPasswordChange,
                placeHolder = stringResource(R.string.current_password),
                autoCorrect = false,
                onConfirm = { }
            )
            IGTextBoxPassword(
                value = newPassword,
                isTrailingIconEnabled = true,
                onValueChange = onNewPasswordChange,
                placeHolder = stringResource(R.string.new_password),
                autoCorrect = false,
                onConfirm = { }
            )
            IGTextBoxPassword(
                modifier = Modifier.padding(vertical = 10.dp),
                value = rePassword,
                isTrailingIconEnabled = true,
                onValueChange = onRePasswordChange,
                placeHolder = stringResource(R.string.re_type_new_password),
                autoCorrect = false,
                onConfirm = { onButtonClick() }
            )

            AnimatedVisibility(visible = error.isNotEmpty()) {
                Text(
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                        .fillMaxWidth(),
                    text = error,
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        color = Utils.IgError
                    ),
                    textAlign = TextAlign.Start
                )
            }

            IGButton(
                enabled = error.isEmpty(),
                text = stringResource(R.string.change_password),
                isLoading = buttonLoading,
                onClick = onButtonClick
            )
        }
    }
}

@Composable
fun DragHandle(
    modifier: Modifier = Modifier,
    icon: Painter,
    showMetaLogo: Boolean = true,
    onDismiss: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = Modifier
            .padding(bottom = 20.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Icon(
            modifier = modifier
                .scale(1.4f)
                .padding(top = 10.dp)
                .clickable(
                    indication = null,
                    interactionSource = interactionSource,
                    onClick = onDismiss
                ),
            painter = icon,
            tint = Utils.IgOffWhite,
            contentDescription = stringResource(R.string.close)
        )
        if (showMetaLogo) {
            Icon(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 20.dp),
                painter = painterResource(id = R.drawable.meta),
                tint = Utils.IgOffWhite,
                contentDescription = stringResource(R.string.meta)
            )
            Spacer(modifier = Modifier.weight(0.1f))
        }
    }
}

@Composable
fun AccountCenterCard(
    modifier: Modifier = Modifier,
    cardText: String,
    icon: Painter,
    trailingIcon: Boolean = true,
    cornerRadius: RoundedCornerShape = RoundedCornerShape(0.dp),
    onClick: () -> Unit
) {
    var size by remember { mutableFloatStateOf(1f) }
    val scale by animateFloatAsState(
        targetValue = size,
        animationSpec = tween(durationMillis = 250),
        finishedListener = { size = 1f },
        label = "accountCenterScale"
    )
    val interactionSource = remember { MutableInteractionSource() }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(Alignment.Top),
        color = Color(0x92383131),
        shape = cornerRadius
    ) {
        Row(
            modifier = Modifier
                .scale(scale)
                .padding(horizontal = 15.dp, vertical = 15.dp)
                .fillMaxWidth()
                .clickable(
                    indication = null,
                    interactionSource = interactionSource,
                    onClick = {
                        size = 0.96f
                        onClick()
                    }
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                modifier = Modifier.weight(1f),
                painter = icon,
                tint = Color.White,
                contentDescription = cardText
            )
            Text(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .weight(8f),
                text = cardText,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White
                ),
                textAlign = TextAlign.Start
            )
            if (trailingIcon) {
                Icon(
                    modifier = Modifier.weight(1f),
                    painter = painterResource(id = R.drawable.arrow_right),
                    tint = Color.White,
                    contentDescription = stringResource(R.string.open)
                )
            }
        }
    }
}

@Preview(
    apiLevel = 33,
    showBackground = true,
    backgroundColor = 0XFF000000
)
@Composable
fun AccountCenterCardPreview() {
    AccountCenterCard(
        cardText = "Personal details",
        icon = painterResource(id = R.drawable.personal_details),
        onClick = { }
    )
}

@Preview(
    apiLevel = 33,
    showBackground = true,
    backgroundColor = 0XFF000000
)
@Composable
fun AccountCenterPreview() {
    AccountsCenter(
        email = "prasidh@gmail.com",
        phone = "987654321",
        password = "",
        newPassword = "",
        rePassword = "",
        error = "",
        visible = true,
        buttonLoading  = false,
        onChangePasswordClick = { },
        onPasswordChange = { },
        onNewPasswordChange = { },
        onRePasswordChange = { },
        onDismiss = { }
    )
}

@Preview(
    apiLevel = 33,
    showBackground = true,
    backgroundColor = 0XFF000000
)
@Composable
fun ChangePasswordPreview() {
    ChangePasswordContent(
        password = "",
        newPassword = "",
        rePassword = "",
        error = "",
        visible = true,
        buttonLoading = false,
        onPasswordChange = { },
        onNewPasswordChange = { },
        onRePasswordChange = { },
        onButtonClick = { },
        onDismiss = { }
    )
}