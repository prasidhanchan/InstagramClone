package com.instagramclone.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.instagramclone.ui.R
import com.instagramclone.ui.components.EditTextBox
import com.instagramclone.ui.components.IGLoader
import com.instagramclone.ui.components.IGRadioButton
import com.instagramclone.util.constants.Utils

@Composable
fun EditTextScreen(
    innerPadding: PaddingValues,
    text: String,
    onValueChange: (String) -> Unit,
    uiState: UiState,
    isUsernameAvailable: Boolean,
    isUpdating: Boolean,
    onGenderSelected: (String) -> Unit,
    onCancelClick: () -> Unit,
    onDoneClick: () -> Unit
) {
    var radioButtonState by remember { mutableStateOf(uiState.gender) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EditTextScreenAppbar(
            text = text,
            buttonEnabled = when (text) {
                stringResource(id = R.string.name) -> uiState.textState.isNotEmpty()
                stringResource(id = R.string.username) -> uiState.textState.isNotEmpty() && isUsernameAvailable
                else -> true
            },
            isUpdating = isUpdating,
            onCancelClick = onCancelClick,
            onDoneClick = onDoneClick
        )

        if (!uiState.isLoading) {
            if (text != stringResource(id = R.string.gender)) {
                EditTextBox(
                    text = text,
                    value = uiState.textState,
                    onValueChange = onValueChange,
                    enabled = true,
                    onClick = { onDoneClick() }
                )
                AnimatedVisibility(
                    visible = uiState.error.isNotEmpty()
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        text = uiState.error,
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal,
                            color = Utils.IgError
                        ),
                        textAlign = TextAlign.Start
                    )
                }
                Text(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 5.dp),
                    text = when (text) {
                        stringResource(id = R.string.name) -> stringResource(R.string.edit_profile_message)
                        stringResource(id = R.string.username) -> stringResource(
                            id = R.string.edit_text_message_usn,
                            uiState.username
                        )

                        else -> ""
                    },
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White.copy(alpha = 0.5f)
                    )
                )
            } else {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 15.dp),
                    text = stringResource(id = R.string.edit_profile_message_gender),
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White.copy(alpha = 0.5f)
                    )
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.male),
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.White
                        )
                    )
                    IGRadioButton(
                        selected = radioButtonState == stringResource(id = R.string.male),
                        onClick = {
                            radioButtonState = context.getString(R.string.male)
                            onGenderSelected(context.getString(R.string.male))
                        }
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 30.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.female),
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.White
                        )
                    )
                    IGRadioButton(
                        selected = radioButtonState == stringResource(id = R.string.female),
                        onClick = {
                            radioButtonState = context.getString(R.string.female)
                            onGenderSelected(context.getString(R.string.female))
                        }
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Unknown",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.White
                        )
                    )
                    IGRadioButton(
                        selected = radioButtonState == stringResource(R.string.unknown),
                        onClick = {
                            radioButtonState = context.getString(R.string.unknown)
                            onGenderSelected(context.getString(R.string.unknown))
                        }
                    )
                }
            }
        } else {
            IGLoader()
        }
    }
}

@Composable
fun EditTextScreenAppbar(
    text: String,
    buttonEnabled: Boolean = true,
    isUpdating: Boolean,
    onCancelClick: () -> Unit,
    onDoneClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .height(50.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                modifier = Modifier
                    .scale(1.5f)
                    .weight(0.5f)
                    .clickable(
                        onClick = onCancelClick
                    ),
                painter = painterResource(id = R.drawable.cross),
                tint = Color.White,
                contentDescription = stringResource(id = R.string.cancel)
            )
            Text(
                modifier = Modifier
                    .padding(start = 20.dp),
                text = text,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                ),
                textAlign = TextAlign.End
            )
            Spacer(modifier = Modifier.weight(4f))
            if (!isUpdating) {
                Icon(
                    modifier = Modifier
                        .scale(1.5f)
                        .weight(0.5f)
                        .clickable(
                            enabled = buttonEnabled,
                            onClick = onDoneClick
                        ),
                    painter = painterResource(id = R.drawable.check),
                    tint = if (buttonEnabled) Utils.IgBlue else Utils.IgBlue.copy(alpha = 0.5f),
                    contentDescription = stringResource(R.string.done)
                )
            } else {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp,
                    color = Utils.IgBlue
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
fun EditTextScreenAppBarPreview() {
    EditTextScreenAppbar(
        text = "Name",
        isUpdating = true,
        onCancelClick = { },
        onDoneClick = { }
    )
}

@Preview(
    apiLevel = 33,
    showBackground = true,
    backgroundColor = 0XFF000000
)
@Composable
fun EditTextScreenPreview() {
    EditTextScreen(
        innerPadding = PaddingValues(),
        text = "Gender",
        onValueChange = { },
        uiState = UiState(textState = "Prasidh Gopal Anchan"),
        isUsernameAvailable = true,
        isUpdating = true,
        onGenderSelected = { },
        onCancelClick = { },
        onDoneClick = { }
    )
}
