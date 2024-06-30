package com.instagramclone.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.instagramclone.ui.R
import com.instagramclone.util.constants.Utils.IgError
import com.instagramclone.util.constants.Utils.IgOffBackground

@Composable
fun IGTextBox(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeHolder: String,
    cornerRadius: Dp = 5.dp,
    imeAction: ImeAction = ImeAction.Next,
    enabled: Boolean = true,
    isErrorOrSuccess: String = "",
    keyboardType: KeyboardType = KeyboardType.Text,
    autoCorrect: Boolean,
    isTrailingIconEnabled: Boolean = false,
    clearText: () -> Unit = { },
    onConfirm: KeyboardActionScope.() -> Unit
) {
    val focusRequester = FocusRequester()
    val interactionSource = remember { MutableInteractionSource() }

    TextField(
        modifier = modifier
            .height(50.dp)
            .fillMaxWidth()
            .focusRequester(focusRequester),
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeHolder,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                )
            )
        },
        textStyle = TextStyle(
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.onBackground
        ),
        shape = RoundedCornerShape(cornerRadius),
        enabled = enabled,
        visualTransformation = VisualTransformation.None,
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.None,
            keyboardType = keyboardType,
            autoCorrect = autoCorrect,
            imeAction = imeAction
        ),
        keyboardActions = when (imeAction) {
            ImeAction.Next -> KeyboardActions(onNext = onConfirm)
            ImeAction.Done -> KeyboardActions(onGo = onConfirm)
            else -> KeyboardActions(onSend = onConfirm)
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = IgOffBackground,
            unfocusedContainerColor = IgOffBackground,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedTextColor = Color.White.copy(alpha = 0.5f),
            unfocusedTextColor = Color.White.copy(alpha = 0.5f)
        ),
        trailingIcon = {
            if (isTrailingIconEnabled) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .clickable(
                            indication = null,
                            interactionSource = interactionSource,
                            onClick = clearText
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        modifier = if (
                            isErrorOrSuccess == stringResource(id = R.string.username_already_exists) ||
                            isErrorOrSuccess == stringResource(id = R.string.email_already_exits)
                        ) {
                            Modifier.clickable(
                                indication = null,
                                interactionSource = interactionSource,
                                onClick = clearText
                            )
                        } else {
                            Modifier
                        },
                        painter = painterResource(
                            id = when (isErrorOrSuccess) {
                                stringResource(R.string.username_already_exists) -> R.drawable.cross
                                stringResource(R.string.email_already_exits) -> R.drawable.cross
                                stringResource(R.string.available) -> R.drawable.check
                                else -> return@TextField
                            },
                        ),
                        tint = if (isErrorOrSuccess != stringResource(id = R.string.available)) IgError else Color.Green,
                        contentDescription = stringResource(R.string.tick_cross)
                    )
                }
            } else {
                return@TextField
            }
        }
    )

    LaunchedEffect(key1 = Unit) {
        focusRequester.requestFocus()
    }
}

@Preview
@Composable
fun IGTextBoxPreview() {
    IGTextBox(
        value = "Naruto",
        onValueChange = { },
        isErrorOrSuccess = "Available",
        placeHolder = "Username",
        cornerRadius = 12.dp,
        autoCorrect = false,
        isTrailingIconEnabled = true,
        onConfirm = { }
    )
}