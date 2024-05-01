package com.instagramclone.upload

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.instagramclone.ui.R
import com.instagramclone.ui.components.IGButton
import com.instagramclone.ui.components.IGRegularAppBar
import com.instagramclone.util.constants.Utils
import com.instagramclone.util.constants.Utils.IgBackground

@Composable
fun AddCaptionScreen(
    innerPadding: PaddingValues,
    uiState: UiState,
    onCaptionChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onShareClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = IgBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(vertical = 5.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IGRegularAppBar(
                text = stringResource(id = R.string.new_post),
                onBackClick = onBackClick
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(6f)
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Column {
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.5f),
                        model = uiState.selectedMedia?.data,
                        contentScale = ContentScale.Crop,
                        contentDescription = uiState.selectedMedia?.name
                    )
                    BasicTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .padding(vertical = 20.dp),
                        value = uiState.caption,
                        onValueChange = onCaptionChange,
                        textStyle = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.White
                        ),
                        decorationBox = { innerTextField ->
                            if (uiState.caption.isEmpty()) {
                                Text(
                                    modifier = Modifier.offset(x = (4).dp),
                                    text = stringResource(R.string.write_a_caption),
                                    style = TextStyle(
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Normal,
                                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                                    )
                                )
                            }
                            innerTextField()
                        },
                        cursorBrush = SolidColor(Utils.IgOffColor),
                        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
                    )
                }
            }
            IGButton(
                modifier = Modifier.padding(horizontal = 20.dp),
                text = stringResource(R.string.share),
                isLoading = uiState.isUploading,
                onClick = onShareClick
            )
        }
    }
}

@Preview(
    apiLevel = 33,
    showBackground = true
)
@Composable
private fun AddCaptionScreenPreview() {
    AddCaptionScreen(
        innerPadding = PaddingValues(),
        uiState = UiState(),
        onCaptionChange = {  },
        onShareClick = {  },
        onBackClick = {  }
    )
}