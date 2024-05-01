package com.instagramclone.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.instagramclone.ui.R
import com.instagramclone.util.constants.Utils.IgOffBackground

@Composable
fun WatchAgainComp(
    modifier: Modifier = Modifier,
    visible: Boolean,
    onClick: () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(
            animationSpec = tween(durationMillis = 1000)
        ),
        exit = fadeOut(
            animationSpec = tween(durationMillis = 1000)
        )
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(IgOffBackground.copy(alpha = 0.6f)),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier
                    .width(120.dp)
                    .height(50.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable(
                        onClick = onClick
                    ),
                shape = RoundedCornerShape(12.dp),
                color = Color.White
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.reels),
                        contentDescription = stringResource(id = R.string.reels)
                    )
                    Text(
                        text = "Watch again",
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.Black
                        )
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun WatchAgainCompPreview() {
    WatchAgainComp(
        visible = true,
        onClick = { }
    )
}