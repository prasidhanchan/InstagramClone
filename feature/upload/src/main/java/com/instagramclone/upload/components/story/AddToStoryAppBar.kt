package com.instagramclone.upload.components.story

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.instagramclone.util.constants.Utils.IgOffBackground

/**
 * AppBar composable for AddStoryScreen.
 * @param modifier The Modifier to be applied to the AppBar.
 * @param onBackClick On Click triggered when the back button is clicked.
 */
@Composable
fun AddToStoryAppBar(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit
) {
    Row(
        modifier = modifier
            .padding(horizontal = 15.dp)
            .fillMaxWidth()
            .height(60.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Surface(
            modifier = Modifier
                .size(40.dp)
                .clickable(
                    onClick = onBackClick
                ),
            shape = CircleShape,
            color = IgOffBackground
        ) {
            Icon(
                painter = painterResource(id = R.drawable.arrow_left),
                modifier = Modifier.padding(5.dp),
                tint = MaterialTheme.colorScheme.onBackground,
                contentDescription = stringResource(id = R.string.back)
            )
        }

        Text(
            text = stringResource(id = R.string.add_story),
            modifier = Modifier.weight(2f),
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            ),
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
private fun AddStoryAppBarPreview() {
    AddToStoryAppBar(
        onBackClick = { }
    )
}