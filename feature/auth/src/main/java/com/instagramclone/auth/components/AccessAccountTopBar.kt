package com.instagramclone.auth.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.instagramclone.ui.R

@Composable
fun AccessAccountTopBar(popBack: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = Modifier.padding(top = 40.dp, start = 20.dp, bottom = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.clickable(
                indication = null,
                interactionSource = interactionSource,
                onClick = popBack
            ),
            painter = painterResource(id = R.drawable.arrow_back),
            tint = MaterialTheme.colorScheme.onBackground,
            contentDescription = stringResource(R.string.back)
        )
        Text(
            modifier = Modifier.padding(horizontal = 30.dp),
            text = stringResource(R.string.access_your_account),
            style = TextStyle(
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
        )
    }
}
