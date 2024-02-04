package com.instagramclone.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.instagramclone.ui.components.IGHomeAppBar
import com.instagramclone.ui.components.Stories
import com.instagramclone.util.constants.Utils
import com.instagramclone.util.models.Story

@Composable
fun HomeScreen(
    uiState: UiState
) {
    val stories = listOf(
        Story(
            username = "pra_sidh_22"
        ),
        Story(
            username = "youtubeindia"
        ),
        Story(
            username = "virat.kohli"
        ),
        Story(
            username = "mustang"
        ),
        Story(
            username = "googlefordevs",
            isViewed = true
        )
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Utils.IgBlack
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IGHomeAppBar()

            Stories(
                profileImage = uiState.profileImage,
                onAddStoryClick = { /*TODO*/ },
                onStoryClick = { /*TODO*/ },
                stories = stories
            )
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    val stories = listOf(
        Story(
            username = "pra_sidh_22"
        ),
        Story(
            username = "youtubeindia"
        ),
        Story(
            username = "virat.kohli"
        ),
        Story(
            username = "mustang"
        ),
        Story(
            username = "googlefordevs"
        )
    )
    HomeScreen(
        uiState = UiState(stories = stories)
    )
}