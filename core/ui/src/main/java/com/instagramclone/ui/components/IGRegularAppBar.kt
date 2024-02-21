package com.instagramclone.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.instagramclone.ui.R
import com.instagramclone.util.constants.Utils

@Composable
fun IGRegularAppBar(
    text: String,
    leadingIcon: Int = R.drawable.back,
    fonSize: Int = 20,
    onBackClick: () -> Unit,
    contentPadding: PaddingValues = PaddingValues(horizontal = 20.dp),
    trailingIcon: @Composable () -> Unit = {  }
) {
    val interactionSource = remember { MutableInteractionSource() }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        color = Utils.IgBlack
    ) {
        Row(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                modifier = Modifier.clickable(
                    indication = null,
                    interactionSource = interactionSource,
                    onClick = onBackClick
                ),
                painter = painterResource(id = leadingIcon),
                tint = Color.White,
                contentDescription = stringResource(id = R.string.back)
            )
            Spacer(modifier = Modifier.width(30.dp))
            Text(
                modifier = Modifier.weight(2f),
                text = text,
                style = TextStyle(
                    fontSize = fonSize.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.CenterEnd,
                content = { trailingIcon() }
            )
        }
    }
}

@Preview
@Composable
fun IGRegularAppBarPreview() {
    IGRegularAppBar(
        text = "Edit profile",
        onBackClick = {  }
    )
}