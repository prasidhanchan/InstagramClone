package com.instagramclone.profile.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.instagramclone.ui.R

@Composable
internal fun IGUserProfileAppBar(
    username: String,
    onMoreClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .height(50.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Icon(
            modifier = Modifier.clickable(
                indication = null,
                interactionSource = interactionSource,
                onClick = onBackClick
            ),
            painter = painterResource(id = R.drawable.back),
            tint = MaterialTheme.colorScheme.onBackground,
            contentDescription = stringResource(id = R.string.back)
        )
        Text(
            modifier = Modifier
                .padding(horizontal = 30.dp)
                .weight(6f),
            text = username,
            style = TextStyle(
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            ),
            textAlign = TextAlign.Start
        )
        Icon(
            modifier = Modifier.clickable(
                indication = null,
                interactionSource = interactionSource,
                onClick = onMoreClick
            ),
            painter = painterResource(id = R.drawable.more2),
            tint = MaterialTheme.colorScheme.onBackground,
            contentDescription = stringResource(id = R.string.more)
        )
    }
}

@Preview(
    showBackground = true
)
@Composable
private fun IGUserProfileAppBarPreview() {
    IGUserProfileAppBar(
        username = "pra_sidh_22",
        onMoreClick = {  },
        onBackClick = { }
    )
}