package com.instagramclone.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.instagramclone.ui.R

@Composable
fun IGProfileAppBar(
    username: String,
    onMoreClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .height(50.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = username,
            style = TextStyle(
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        )
        
        Icon(
            modifier = Modifier.clickable(
                indication = null,
                interactionSource = interactionSource,
                onClick = onMoreClick
            ),
            painter = painterResource(id = R.drawable.more),
            tint = Color.White,
            contentDescription = "More"
        )
    }
}

@Preview
@Composable
fun IGProfileAppbarPreview() {
    IGProfileAppBar(
        username = "pra_sidh_22",
        onMoreClick = {  }
    )
}