package com.instagramclone.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.instagramclone.ui.R
import com.instagramclone.util.constants.Utils.IgBackground

@Composable
internal fun IGHomeAppBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(65.dp)
            .background(color = IgBackground),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            modifier = Modifier
                .fillMaxWidth()
                .size(32.dp)
                .weight(0.5f),
            painter = painterResource(id = R.drawable.instagram_logo),
            tint = MaterialTheme.colorScheme.onBackground,
            contentDescription = stringResource(id = R.string.instagram_logo)
        )
        
        Row(
            modifier = Modifier
                .padding(horizontal = 15.dp)
                .fillMaxWidth()
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Icon(
                modifier = Modifier.size(26.dp),
                painter = painterResource(id = R.drawable.heart_outlined),
                tint = MaterialTheme.colorScheme.onBackground,
                contentDescription = stringResource(R.string.notifications)
            )
            Spacer(modifier = Modifier.width(25.dp))
            Icon(
                modifier = Modifier.size(28.dp),
                painter = painterResource(id = R.drawable.messenger),
                tint = MaterialTheme.colorScheme.onBackground,
                contentDescription = stringResource(R.string.messenger)
            )
        }
    }
}

@Preview
@Composable
fun IGHomeAppBArPreview() {
    IGHomeAppBar()
}