package com.instagramclone.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.instagramclone.util.constants.Utils

@Composable
fun IGRadioButton(
    selected: Boolean,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Surface(
        modifier = Modifier
            .size(24.dp)
            .clickable(
                indication = null,
                interactionSource = interactionSource,
                onClick = onClick
            ),
        shape = CircleShape,
        color = Color.Transparent,
        border = BorderStroke(
            width = if (selected) 8.dp else 2.dp,
            color = if (selected) Utils.IgBlue else Utils.IgOffColor
        ),
        content = {  }
    )
}

@Preview
@Composable
fun IGRadioButtonPreview() {
    IGRadioButton(
        selected = true,
        onClick = {  }
    )
}