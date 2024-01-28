package com.instagramclone.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.instagramclone.util.constants.Utils

/**
 * Instagram Button custom composable which uses [TextButton]
 * @param modifier Requires [Modifier]
 * @param text Button text to be displayed
 * @param enabled Enable or Disable button
 * @param fontSize Size of the text within the Button
 * @param onClick Onclick lambda which triggers when the button is clicked
 */
@Composable
fun IGButton(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true,
    fontSize: Int = 15,
    cornerRadius: Dp = 8.dp,
    isLoading: Boolean,
    onClick: () -> Unit
) {
    TextButton(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp),
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = Utils.IgBlue,
            disabledContainerColor = Utils.IgBlue.copy(alpha = 0.4f)
        ),
        shape = RoundedCornerShape(cornerRadius)
    ) {
        if (!isLoading) {
            Text(
                text = text,
                style = TextStyle(
                    fontSize = fontSize.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (enabled) Color.White else Color.White.copy(alpha = 0.5f)
                )
            )
        } else {
            CircularProgressIndicator(
                modifier = Modifier.size(30.dp),
                strokeWidth = 3.dp,
                color = Color.White
            )
        }
    }
}

@Preview
@Composable
fun IGButtonPreview() {
    IGButton(
        enabled = false,
        text = "Log in",
        isLoading = true,
        onClick = {  }
    )
}