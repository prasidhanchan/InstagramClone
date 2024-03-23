package com.instagramclone.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.instagramclone.ui.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Animated like composable for Posts and Reels
 * @param modifier Requires [Modifier]
 * @param iconSize Size of the Like icon
 * @param onDoubleTap on Double tap lambda triggered when a post or a reel is liked
 * @param content Content to be wrapped with in the like animation i.e an Image or the Video
 */
@Composable
fun AnimatedLike(
    modifier: Modifier = Modifier,
    iconSize: Dp = 80.dp,
    onDoubleTap: () -> Unit,
    content: @Composable (BoxScope.() -> Unit)
) {
    var showLike by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = {
                        scope.launch {
                            showLike = true
                            delay(1500L)
                            showLike = false
                        }
                        onDoubleTap()
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            content()
            AnimatedVisibility(
                visible = showLike,
                enter = scaleIn(
                    initialScale = 0f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMediumLow
                    )
                ),
                exit = scaleOut(
                    targetScale = 0f,
                    animationSpec = tween()
                )
            ) {
                Icon(
                    modifier = modifier
                        .size(iconSize),
                    painter = painterResource(id = R.drawable.like),
                    tint = Color.White,
                    contentDescription = stringResource(id = R.string.like)
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0XFF000000
)
@Composable
private fun AnimatedLikePreview() {
    AnimatedLike(
        onDoubleTap = { },
        content = { }
    )
}