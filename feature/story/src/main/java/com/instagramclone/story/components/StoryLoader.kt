package com.instagramclone.story.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.instagramclone.util.constants.Utils.IgOffBackground

/**
 * Story Loader composable for StoryScreen
 * @param loading Boolean to show loader.
 * @param modifier The Modifier to be applied to the loader.
 */
@Composable
fun StoryLoader(
    loading: Boolean,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = loading,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Surface(
            modifier = modifier
                .padding(vertical = 30.dp)
                .fillMaxSize(),
            shape = RoundedCornerShape(12.dp),
            color = IgOffBackground
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .border(
                            width = 1.dp,
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                        )
                )
            }
        }
    }

}

@Preview
@Composable
private fun StoryLoaderPreview() {
    StoryLoader(loading = true)
}