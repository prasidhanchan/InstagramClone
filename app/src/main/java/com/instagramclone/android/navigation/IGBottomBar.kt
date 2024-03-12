package com.instagramclone.android.navigation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.instagramclone.ui.R
import com.instagramclone.util.constants.Utils

@Composable
fun IGBottomBar(
    profileImage: String?,
    navigateToRoute: (NavScreens) -> Unit,
    navHostController: NavHostController
) {
    val items = NavScreens.Items.list
    val interactionSource = remember { MutableInteractionSource() }
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentScreen = navBackStackEntry?.destination

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(65.dp),
        color = Utils.IgBlack
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.Top
        ) {
            items.forEach { item ->
                IGBottomBarItem(
                    isSelected = currentScreen?.hierarchy?.any { it.route == item.route } == true,
                    item = item,
                    onClick = navigateToRoute
                )
            }
            Surface(
                modifier = Modifier
                    .size(26.dp)
                    .clickable(
                        indication = null,
                        interactionSource = interactionSource,
                        onClick = { navigateToRoute(NavScreens.MyProfileScreen) }
                    ),
                shape = CircleShape,
                color = Utils.IgOffBlack,
                border = BorderStroke(
                    color = if (currentScreen?.hierarchy?.any { it.route == NavScreens.MyProfileScreen.route } == true) {
                        Color.White
                    } else {
                        Color.Transparent
                    },
                    width = 1.5.dp)
            ) {
                if (profileImage?.isNotEmpty() == true) {
                    AsyncImage(
                        modifier = Modifier.fillMaxSize(),
                        model = profileImage,
                        contentScale = ContentScale.Crop,
                        contentDescription = stringResource(id = R.string.profile_image)
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.profile),
                        contentDescription = stringResource(id = R.string.profile_image)
                    )
                }
            }
        }
    }
}

@Composable
fun IGBottomBarItem(
    isSelected: Boolean,
    item: NavScreens,
    onClick: (NavScreens) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        modifier = Modifier.clickable(
            indication = null,
            interactionSource = interactionSource,
            onClick = {
                onClick(item)
            }
        ),
        contentAlignment = Alignment.Center,
        content = {
            Icon(
                modifier = Modifier.clickable(
                    indication = null,
                    interactionSource = interactionSource,
                    onClick = { onClick(item) }
                ),
                painter = painterResource(id = if (isSelected) item.iconFilled!! else item.iconOutlined!!),
                tint = Color.White,
                contentDescription = item.name
            )
        }
    )
}

@Preview(apiLevel = 33)
@Composable
fun IGBottomBarPreview() {
    IGBottomBar(
        profileImage = "",
        navigateToRoute = { },
        navHostController = rememberNavController()
    )
}