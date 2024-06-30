package com.instagramclone.ui.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.instagramclone.util.constants.Utils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IGBottomSheet(
    modifier: Modifier = Modifier,
    showSheet: Boolean,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    content: @Composable (ColumnScope.() -> Unit)
) {
    if (showSheet) {
        ModalBottomSheet(
            modifier = modifier,
            sheetState = sheetState,
            onDismissRequest = onDismiss,
            dragHandle = {
                BottomSheetDefaults.DragHandle(
                    width = 35.dp,
                    color = Utils.IgOffColor
                )
            },
            shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
            containerColor = Utils.IgOffBackground,
            content = {
                HorizontalDivider(
                    modifier = Modifier.padding(top = 35.dp),
                    thickness = 0.2.dp,
                    color = Utils.IgOffColor.copy(alpha = 0.2f)
                )
                content()
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(
    showBackground = true,
    backgroundColor = 0XFF000000
)
@Composable
private fun IGBottomSheetPreview() {
    IGBottomSheet(
        showSheet = true,
        sheetState = rememberModalBottomSheetState(),
        onDismiss = {  },
        content = {  }
    )
}