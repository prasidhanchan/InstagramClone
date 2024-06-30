package com.instagramclone.upload.components.story

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.instagramclone.ui.R
import com.instagramclone.util.constants.Utils.IgOffBackground

/**
 * Media card composable to display the media to be displayed, i.e photos or videos.
 * @param modifier The Modifier to be applied on the Card.
 * @param text The text to be displayed on the Card.
 * @param icon The icon to be displayed on the Card.
 * @param onClick The callback to be invoked when the Card is clicked.
 */
@Composable
fun MediaSelectionCard(
    modifier: Modifier = Modifier,
    text: String,
    icon: Int,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    var size by remember { mutableFloatStateOf(1f) }
    val scale by animateFloatAsState(
        targetValue = size,
        label = "cardScale",
        finishedListener = { size = 1f }
    )

    Surface(
        modifier = modifier
            .height(80.dp)
            .width(100.dp)
            .padding(horizontal = 4.dp)
            .scale(scale)
            .clickable(
                indication = null,
                interactionSource = interactionSource,
                onClick = {
                    size = 0.8f
                    onClick()
                }
            ),
        shape = RoundedCornerShape(12.dp),
        color = IgOffBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = icon),
                tint = MaterialTheme.colorScheme.onBackground,
                contentDescription = text
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = text,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    }
}

@Preview
@Composable
private fun MediaSelectionCardPreview() {
    MediaSelectionCard(
        text = "Photos",
        icon = R.drawable.photos,
        onClick = { }
    )
}