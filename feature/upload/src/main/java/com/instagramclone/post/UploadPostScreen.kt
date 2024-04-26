package com.instagramclone.post

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.instagramclone.ui.R
import com.instagramclone.ui.components.IGRegularAppBar
import com.instagramclone.ui.components.ImageCards
import com.instagramclone.util.constants.Utils
import com.instagramclone.util.constants.Utils.IgBackground
import com.instagramclone.util.models.Image

@Composable
fun UploadPostScreen(
    visible: Boolean,
    innerPadding: PaddingValues,
    uiState: UiState,
    onImageSelected: (Image) -> Unit,
    onNextClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    AnimatedVisibility(
        visible = visible,
        enter = slideInHorizontally(
            animationSpec = tween(durationMillis = 350),
            initialOffsetX = { it }
        ),
        exit = slideOutHorizontally(
            animationSpec = tween(delayMillis = 200, durationMillis = 350),
            targetOffsetX = { it }
        )
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = IgBackground
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IGRegularAppBar(
                    text = stringResource(R.string.new_post),
                    leadingIcon = R.drawable.cross_30dp,
                    trailingIcon = {
                        Text(
                            modifier = Modifier.clickable(
                                enabled = uiState.selectedImage?.data != null,
                                indication = null,
                                interactionSource = interactionSource,
                                onClick = onNextClick
                            ),
                            text = stringResource(id = R.string.next),
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Utils.IgBlue
                            )
                        )
                    },
                    onBackClick = onBackClick
                )

                ImageCards(
                    images = uiState.images,
                    selectedImage = uiState.selectedImage,
                    onImageSelected = onImageSelected
                )
            }
        }
    }
}

@Preview(
    apiLevel = 33,
    showBackground = true
)
@Composable
private fun UploadPostScreenPreview() {
    UploadPostScreen(
        visible = true,
        innerPadding = PaddingValues(),
        uiState = UiState(),
        onImageSelected = { },
        onNextClick = { },
        onBackClick = { }
    )
}