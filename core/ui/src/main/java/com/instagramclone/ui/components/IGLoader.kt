package com.instagramclone.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.instagramclone.util.constants.Utils.IgOffBackground

@Composable
fun IGLoader() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(35.dp),
            strokeWidth = 1.dp,
            color = MaterialTheme.colorScheme.onBackground,
            trackColor = IgOffBackground
        )
    }
}

@Preview
@Composable
private fun IGLoaderPreview() {
    IGLoader()
}