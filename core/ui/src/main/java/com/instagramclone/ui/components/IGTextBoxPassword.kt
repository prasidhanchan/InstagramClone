package com.instagramclone.ui.components

import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.instagramclone.ui.R
import com.instagramclone.util.constants.Utils

@Composable
fun IGTextBoxPassword(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeHolder: String,
    cornerRadius: Dp = 5.dp,
    imeAction: ImeAction = ImeAction.Next,
    enabled: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    autoCorrect: Boolean,
    focusRequester: FocusRequester = FocusRequester(),
    isTrailingIconEnabled: Boolean = false,
    onConfirm: KeyboardActionScope.() -> Unit
) {
    var showPassword by remember { mutableStateOf(false) }
    var visualTransformation by remember { mutableStateOf(VisualTransformation.None) }
    visualTransformation = when (showPassword) {
        true -> VisualTransformation.None
        false -> PasswordVisualTransformation()
    }

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
                    color = Color.White.copy(alpha = 0.5f)
                )
            )
        },
        textStyle = TextStyle(
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = Color.White
        ),
        shape = RoundedCornerShape(cornerRadius),
        enabled = enabled,
        visualTransformation = visualTransformation,
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
            focusedContainerColor = Utils.IgOffBlack,
            unfocusedContainerColor = Utils.IgOffBlack,
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
                        .clickable { showPassword = !showPassword },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(
                            id = if (showPassword) R.drawable.eye_crossed else R.drawable.eye,
                        ),
                        tint = if (showPassword) Utils.IgBlue else Color.White.copy(alpha = 0.5f),
                        contentDescription = "Show/Hide password"
                    )
                }
            } else {
                return@TextField
            }
        }
    )
}