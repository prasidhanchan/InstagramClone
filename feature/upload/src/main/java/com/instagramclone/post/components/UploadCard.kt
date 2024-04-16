package com.instagramclone.post.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.instagramclone.ui.R
import com.instagramclone.util.constants.Utils.IgBlack

/**
 * Upload card composable for displaying Text in a row ex: POST, STORY.
 * @param modifier Requires [Modifier].
 * @param selectedText The text that is currently selected.
 * @param onClick On Click triggered when the text is clicked, passes the current text selected.
 */
@Composable
fun UploadCard(
    modifier: Modifier = Modifier,
    selectedText: String,
    onClick: (String) -> Unit
) {
    Surface(
        modifier = modifier
            .wrapContentSize(Alignment.Center)
            .padding(bottom = 25.dp),
        shape = RoundedCornerShape(25.dp),
        color = IgBlack.copy(alpha = 0.75f)
    ) {
        Row(
            modifier = Modifier.wrapContentSize(Alignment.Center),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            UploadCardText(
                text = stringResource(R.string.post_caps),
                selectedText = selectedText,
                onClick = onClick
            )

            UploadCardText(
                text = stringResource(R.string.story_caps),
                selectedText = selectedText,
                onClick = onClick
            )
        }
    }
}

/**
 * Upload card text composable for displaying Text ex: POST or STORY, etc.
 * @param selectedText The text that is currently selected.
 * @param onClick On Click triggered when the text is clicked, passes the current text.
 */
@Composable
fun UploadCardText(
    text: String,
    selectedText:String,
    onClick: (String) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Text(
        modifier = Modifier
            .clickable(
                indication = null,
                interactionSource = interactionSource,
                onClick = { onClick(text) }
            )
            .padding(horizontal = 15.dp, vertical = 12.dp),
        text = text,
        style = TextStyle(
            fontSize = 15.sp,
            fontWeight = if (selectedText == text) FontWeight.Bold else FontWeight.Normal,
            letterSpacing = TextUnit(2f, TextUnitType.Sp),
            color = MaterialTheme.colorScheme.onBackground
        )
    )
}

@Preview(
    heightDp = 200,
    widthDp = 200,
    showBackground = true
)
@Composable
private fun UploadCardPreview() {
    UploadCard(
        selectedText = "POST",
        onClick = { }
    )
}